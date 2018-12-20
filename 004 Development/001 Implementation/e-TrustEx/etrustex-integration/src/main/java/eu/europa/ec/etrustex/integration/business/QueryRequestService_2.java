package eu.europa.ec.etrustex.integration.business;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ec.schema.xsd.commonaggregatecomponents_2.DocumentReferenceResponseParentDocsType;
import ec.schema.xsd.commonaggregatecomponents_2.RequestDocumentParameterType;
import ec.schema.xsd.queryrequest_2.QueryRequestType;
import ec.schema.xsd.queryresponse_2.ObjectFactory;
import ec.schema.xsd.queryresponse_2.QueryResponseType;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;

public class QueryRequestService_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(QueryRequestService_2.class);
	
	private static JAXBContext jaxbContext = null;
	private static final int MAX_NUMBER_INBOX_LINES = 500;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement<QueryResponseType>> processMessage(TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement<QueryResponseType>> responseMessage = new TrustExMessage<JAXBElement<QueryResponseType>>(null);
		responseMessage.setHeader(message.getHeader());
		JAXBElement<QueryRequestType> requestElement = null;
		try {			
			Source s = new StreamSource(new StringReader(message.getPayload()));
			requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, QueryRequestType.class);
		}catch (JAXBException e) {
			logger.error(e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} 			
		
		QueryRequestType request = requestElement.getValue();
		// we check the agreement between the sender of the message and the connected user party
		Set<Transaction> transactions = null;
		if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0){
			transactions= retrieveThirdPartyAuthorisedTransactions(message.getHeader());
		}
		List<Message> queryResults = new ArrayList<Message>();
		RequestDocumentParameterType requestParameter= request.getRequestDocumentParameter();
		
		
		Calendar oneYearBefore =  Calendar.getInstance();
		oneYearBefore.add (Calendar.YEAR, -1);
		if (requestParameter != null){
			queryResults = retrieveMessages(requestParameter, message, oneYearBefore.getTime(), transactions);
		} else {
			Set<Long> senderIds = new HashSet<Long>();
			senderIds.add(message.getHeader().getSenderPartyId());
			Date startDate = oneYearBefore.getTime();
			Date endDate =Calendar.getInstance().getTime();
			RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO = new RetrieveMessagesForQueryRequestDTO.Builder()			
				.setSenderPartyIds(senderIds)
				.setTransactions(transactions)
				.setRetrievedInd(false)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(message.getHeader().getCorrelationId())
				.setFetchParents(true)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(true)
				.setFilterOutMessagesInError(true)
				.build();
			queryResults = messageService.retrieveMessagesForQueryRequest(retrieveMessagesDTO); //don't limit to 500 because some messages are not considered
			
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
		return responseMessage;

	}
	
	private List<Message> retrieveMessages(RequestDocumentParameterType requestParameter, TrustExMessage<String> message, Date oneYearBefore,
			Set<Transaction> transactions) {
		// gernic rule if no period specified we do take the date of the query -1 one year as param
		Date startDate = requestParameter.getPeriod() != null ? requestParameter.getPeriod().getStartDate().getValue().toGregorianCalendar().getTime() :  oneYearBefore;
		Date endDate =requestParameter.getPeriod() != null ? requestParameter.getPeriod().getEndDate().getValue().toGregorianCalendar().getTime() :Calendar.getInstance().getTime();
		
		// see query use case A3 : the types codes are set in a in clause so it is safe to add also potentially invali documents types. this also implicitely implements the us Alternative flow 4 if only invalid
		// document types are specified system will retrun an empty response
		List<DocumentTypeCodeType> typecodes =requestParameter.getDocumentTypeCode();
		Set<String> codes = new HashSet<String>();
		for (DocumentTypeCodeType documentTypeCodeType : typecodes) {
			codes.add(documentTypeCodeType.getValue());
		}
		Boolean retrievedInd = false;
		if (requestParameter.getRetrievedDocumentsIndicator() != null && requestParameter.getRetrievedDocumentsIndicator().size() ==1){
			if (requestParameter.getRetrievedDocumentsIndicator().get(0).isValue()){
				retrievedInd = true;
			}
		}
		List<PartyType> senders= requestParameter.getSenderParty();
		// we do take into account only valid party ids
		List<PartyType> receivers= requestParameter.getReceiverParty();
		long invalidSendersCount=0;
		long invalidReceiversCount=0;
		boolean specifiedSelfAsSender = false;
		boolean specifiedSelfAsReceiver = false;
		Long queryingPartyId = message.getHeader().getSenderPartyId();
		String separator = message.getHeader().getMetadata().containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)?message.getHeader().getMetadata().get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue():":";
		Set<Long> senderIds = new HashSet<Long>();
		for (PartyType partyType : senders) {
			try {
				Party p = resolveParty(partyType, separator, message.getHeader().getIssuer().getBusinessDomain());
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
		Set<Long> receiverIds = new HashSet<Long>();
		for (PartyType partyType : receivers) {
			try {
				Party p = resolveParty(partyType, separator, message.getHeader().getIssuer().getBusinessDomain());
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
		
		RetrieveMessagesForQueryRequestDTO.Builder builder = new RetrieveMessagesForQueryRequestDTO.Builder()
			.setReceiverPartyIds(receiverIds)
			.setSenderPartyIds(senderIds)
			.setTransactions(transactions)
			.setDocumentTypeCodes(codes)
			.setRetrievedInd(retrievedInd)
			.setStartDate(startDate)
			.setEndDate(endDate)
			.setCorrelationId(message.getHeader().getCorrelationId())
			.setMaxResult(MAX_NUMBER_INBOX_LINES)
			.setFetchParents(true)
			.setFetchBinaries(false)
			.setIsSenderAlsoReceiver(false)
			.setFilterOutMessagesInError(true);
		List<Message> queryResults = new ArrayList<>();
		if (senders != null && receivers != null && (senders.size() > 0 || receivers.size() > 0) && senders.size()== invalidSendersCount && receivers.size() == invalidReceiversCount){
			// only invalid IDS specified see UC Alternative flow A6 we must return an empty response
			queryResults = new ArrayList<Message>();
		} else if ((senders == null ||senders.isEmpty()) && (receivers == null ||receivers.isEmpty()) || (senderIds.isEmpty() && receiverIds.isEmpty())){
			// the caller want to retrieve all messages that he sent or received matching the other criterieas
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
			builder.setIsSenderAlsoReceiver(isSenderAlsoReceiver);
			queryResults = messageService.retrieveMessagesForQueryRequest(builder.build());
			
		} else {
			// the caller wants to query on messages that he received and specified a restriction on the list of senders
			if( !senderIds.isEmpty()){
				Set<Long> receiverSet = new HashSet<Long>();
				receiverSet.add(queryingPartyId);
				builder.setReceiverPartyIds(receiverSet);
				queryResults.addAll(messageService.retrieveMessagesForQueryRequest(builder.build()));
			}
			// the caller also wants to query on messages that he received and specified a restriction on the list of receivers
			if(!receiverIds.isEmpty()){
				Set<Long> senderSet = new HashSet<Long>();
				senderSet.add(queryingPartyId);
				builder.setSenderPartyIds(senderSet);
				builder.setReceiverPartyIds(receiverIds);
				queryResults.addAll(messageService.retrieveMessagesForQueryRequest(builder.build()));

			}
		}		
		return queryResults;
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
	
	private Party resolveParty(PartyType partyType, String separator, BusinessDomain bd) throws UndefinedIdentifierException {
		String schemeId = partyType.getEndpointID().getSchemeID();
		Party p = null;
		if(StringUtils.isEmpty(schemeId)){
			p = authorisationService.getParty(partyType.getEndpointID().getValue(), bd);
		}else{					 					 
			 String edpValue = partyType.getEndpointID().getValue();
			 String partyIdWithScheme = null;
			 if(edpValue.indexOf(separator) != -1){
				 //schemeId attribute ignored
				 partyIdWithScheme = edpValue;
			 }else{
				 partyIdWithScheme = schemeId+separator+edpValue;
			 }
			 p = authorisationService.getParty(partyIdWithScheme, bd);
		}
		return p;
		
	}
}
