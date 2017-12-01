package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.DocumentReferenceResponseParentDocsType;
import ec.schema.xsd.commonaggregatecomponents_2.RequestDocumentParameterType;
import ec.schema.xsd.queryrequest_2.QueryRequestType;
import ec.schema.xsd.queryresponse_2.ObjectFactory;
import ec.schema.xsd.queryresponse_2.QueryResponseType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class QueryRequestService_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(QueryRequestService_2.class);
	
	private static JAXBContext jaxbContext = null;
	private static final int MAX_NUMBER_INBOX_LINES = 500;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
		responseMessage.setHeader(message.getHeader());
		try {			
			Source s = new StreamSource(new StringReader(message.getPayload()));
			JAXBElement<QueryRequestType> requestElement = getJaxBContext()
			.createUnmarshaller().unmarshal(s, QueryRequestType.class);
			QueryRequestType request = requestElement.getValue();
			// we check the agreement between the sender of the message and the connected user party
			Set<Transaction> transactions = null;
			if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0){
				transactions= retrieveThirdPartyAuthorisedTransactions(message.getHeader());
			}
			Long queryingPartyId = message.getHeader().getSenderPartyId();
			List<Message> queryResults = new ArrayList<Message>();
			RequestDocumentParameterType requestParameter= request.getRequestDocumentParameter();
			Set<Long> senderIds = new HashSet<Long>();
			Set<Long> receiverIds = new HashSet<Long>();
			Set<String> codes = new HashSet<String>();
			String documentId = null;
			Calendar oneYearBefore =  Calendar.getInstance();
			oneYearBefore.add (Calendar.YEAR, -1);
			if (requestParameter != null){
				
				// gernic rule if no period specified we do take the date of the query -1 one year as param
				Date startDate = requestParameter.getPeriod() != null ? requestParameter.getPeriod().getStartDate().getValue().toGregorianCalendar().getTime() :  oneYearBefore.getTime();
				Date endDate =requestParameter.getPeriod() != null ? requestParameter.getPeriod().getEndDate().getValue().toGregorianCalendar().getTime() :Calendar.getInstance().getTime();
//				if(requestParameter.getOriginatorDocumentReference() != null && requestParameter.getOriginatorDocumentReference() .getID() != null){
//					  documentId =  requestParameter.getOriginatorDocumentReference() .getID().getValue();
//				}
				// see query use case A3 : the types codes are set in a in clause so it is safe to add also potentially invali documents types. this also implicitely implements the us Alternative flow 4 if only invalid
				// document types are specified system will retrun an empty response
				List<DocumentTypeCodeType> typecodes =requestParameter.getDocumentTypeCode();
				for (DocumentTypeCodeType documentTypeCodeType : typecodes) {
					codes.add(documentTypeCodeType.getValue());
				}
				Boolean retrievedInd = false;
				if (requestParameter.getRetrievedDocumentsIndicator() != null && requestParameter.getRetrievedDocumentsIndicator().size() ==1){
					if (requestParameter.getRetrievedDocumentsIndicator().get(0).isValue()){
						retrievedInd = null;
					}
				}
				List<PartyType> senders= requestParameter.getSenderParty();
				// we do take into account only valid party ids
				List<PartyType> receivers= requestParameter.getReceiverParty();
				long invalidSendersCount=0;
				long invalidReceiversCount=0;
				boolean specifiedSelfAsSender = false;
				boolean specifiedSelfAsReceiver = false;
				
				for (PartyType partyType : senders) {
					try {
						Party p =authorisationService.getParty(partyType.getEndpointID().getValue(), message.getHeader().getIssuer().getBusinessDomain());
						// if querying party  is present  in the  senders specified ine the UC we filter it out 
						if (p != null){
							if (p.getId().equals(queryingPartyId)) {
								specifiedSelfAsSender = true;
							} else {
								senderIds.add(p.getId());
							}
						}
					} catch (UndefinedIdentifierException e) {
						//if not a valid party we exclude it from the query as per uc A5
						invalidSendersCount++;
					}
				}
				for (PartyType partyType : receivers) {
					try {
						Party p =authorisationService.getParty(partyType.getEndpointID().getValue(), message.getHeader().getIssuer().getBusinessDomain());
						// if querying party  is present  in the  receivers specified ine the UC we filter it out 
						if (p != null){
							if (p.getId().equals(queryingPartyId)) {
								specifiedSelfAsReceiver = true;
							} else {
								receiverIds.add(p.getId());
							}
						}
					} catch (UndefinedIdentifierException e) {
						//if not a valid party we axcude it from the query as per uc A5
						invalidReceiversCount++;
						
					}
				}
				// only invalid IDS specified see UC Alternative flow A6 we must return an empty response
				if (senders != null && receivers != null && (senders.size() > 0 || receivers.size() > 0) && senders.size()== invalidSendersCount && receivers.size() == invalidReceiversCount){
					queryResults = new ArrayList<Message>();
				}
				// the caller want to retrieve all messages that he sent or received matching the other criterieas
				else if ((senders == null ||senders.isEmpty()) && (receivers == null ||receivers.isEmpty()) || (senderIds.isEmpty() && receiverIds.isEmpty())){
					boolean isSenderAlsoReceiver;
					if (specifiedSelfAsSender && !specifiedSelfAsReceiver) {
						isSenderAlsoReceiver = false;
						senderIds.add(queryingPartyId);
					} else {
						if (!specifiedSelfAsSender && specifiedSelfAsReceiver) {
							isSenderAlsoReceiver = false;
							receiverIds.add(queryingPartyId);
						} else {
							isSenderAlsoReceiver = true;
							senderIds.add(queryingPartyId);
						}
					}
					queryResults = messageService.retrieveMessages(receiverIds, senderIds,
							null, transactions, codes,documentId, retrievedInd, startDate, endDate, message.getHeader().getCorrelationId(), new Integer(500), true, false, isSenderAlsoReceiver, true,null);
					
				} 
				else
				{
					// the caller wants to query on messages that he received and specified a restriction on the list of senders
					if( !senderIds.isEmpty()){
					Set<Long> receiver = new HashSet<Long>();
					receiver.add(queryingPartyId);
					queryResults.addAll(messageService.retrieveMessages(receiver, senderIds,
							null, transactions, codes,documentId, retrievedInd, startDate, endDate, message.getHeader().getCorrelationId(), new Integer(500), true, false,false, true,null));
					}
					// the caller also wants to query on messages that he received and specified a restriction on the list of receivers
					if(!receiverIds.isEmpty()){
						Set<Long> sender = new HashSet<Long>();
						sender.add(queryingPartyId);
						queryResults.addAll(messageService.retrieveMessages(receiverIds, sender,
									null, transactions, codes,documentId, retrievedInd, startDate, endDate, message.getHeader().getCorrelationId(), new Integer(500),true, false,false,true,null));
	
					}
				}
			}	
			else {
				senderIds.add(message.getHeader().getSenderPartyId());
				Date startDate = oneYearBefore.getTime();
				Date endDate =Calendar.getInstance().getTime();
				queryResults = messageService.retrieveMessages(null, senderIds,
						null, transactions, codes,documentId, false, startDate, endDate, message.getHeader().getCorrelationId(), null, true, false, true,true,null); //don't limit to 500 because some messages are not considered
				
			}
			//requestParameter.getRetrievedDocumentsIndicator().get(0).isValue()
			QueryResponseType responseType = new QueryResponseType();
			List<DocumentReferenceResponseParentDocsType> documentReferenceResponseParentDocs =responseType.getDocumentReferenceResponseParentDocs();
			
			int counter = 0;
			for (Message msg : queryResults) {
				
				if (transactions != null && transactions.size()>0){//if no transactions -> no need to filter
					if (("916".equals(msg.getMessageDocumentTypeCode()) || "301".equals(msg.getMessageDocumentTypeCode()))&& msg.getParentMessages()!=null &&msg.getParentMessages().size()>0){
						Message parent = msg.getParentMessages().iterator().next(); //take first. currently we only store one parent.
						
						if ("916".equals(parent.getMessageDocumentTypeCode()) && parent.getParentMessages()!=null &&parent.getParentMessages().size()>0){//if parent is an attached document, check the parent!
							parent = parent.getParentMessages().iterator().next(); //take first. currently we only store one parent.
						}
						
						Transaction parentTransaction = parent.getTransaction();
						if (!transactions.contains(parentTransaction)){ //if parent transaction not in the list of authorized transactions
							continue; //just skip that message
						}
					}
				}
				
				
				if ( ! DOC_WRAPPER_DOC_NM.equalsIgnoreCase(msg.getTransaction().getDocument().getName())){
					DocumentReferenceResponseParentDocsType docParentRef = new DocumentReferenceResponseParentDocsType();
					documentReferenceResponseParentDocs.add(docParentRef);
					docParentRef.setDocumentReferenceResponse(populateDocumentReferenceResponseType(msg));
					Set<Message> parent = msg.getParentMessages();
					for (Message parentMsg : parent)  {
						if ( ! DOC_WRAPPER_DOC_NM.equalsIgnoreCase(parentMsg.getTransaction().getDocument().getName())){
							docParentRef.getParentDocument().add(populateDocumentReferenceResponseType(parentMsg));
						}
					}
					counter++;
					if (counter >= MAX_NUMBER_INBOX_LINES)
						break;
				}
			}
			Transaction t= authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName =new ObjectFactory().createQueryResponse(responseType).getName().getLocalPart();
			JAXBElement<QueryResponseType> responseElement = new JAXBElement<QueryResponseType>(
					new QName(t.getNamespace(),
							responseElementLocalName, "ec"), 
							QueryResponseType.class,responseType);
			
			responseMessage.setPayload(responseElement);
			//responseMessage.setPayload(new ObjectFactory().createQueryResponse(responseType));
			return responseMessage;
		}catch (JAXBException e) {
			logger.error(e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} 
	}
	
	private static JAXBContext getJaxBContext(){
		if (jaxbContext == null){
			try {
				jaxbContext = JAXBContext.newInstance(QueryRequestType.class);				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server",
						ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}
}
