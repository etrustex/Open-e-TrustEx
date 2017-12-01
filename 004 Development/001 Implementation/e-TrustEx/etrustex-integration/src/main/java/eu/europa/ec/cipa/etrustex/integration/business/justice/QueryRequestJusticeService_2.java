package eu.europa.ec.cipa.etrustex.integration.business.justice;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import net.sf.saxon.Configuration;
import net.sf.saxon.trans.XPathException;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.ExtendedDocumentReferenceResponseParentDocsType;
import ec.schema.xsd.commonaggregatecomponents_2.ExtendedRequestDocumentParameterType;
import ec.schema.xsd.queryrequestjustice_2.QueryRequestType;
import ec.schema.xsd.queryresponsejustice_2.ObjectFactory;
import ec.schema.xsd.queryresponsejustice_2.QueryResponseType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.SortFieldTypeCode;

public class QueryRequestJusticeService_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(QueryRequestJusticeService_2.class);
	private static final Integer MAX_RESULT = new Integer(1000);
	
	private static JAXBContext jaxbContext = null;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(
			TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(
				null);
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
			MessagesListVO queryResults = new MessagesListVO(new ArrayList<Message>(), 0);
			ExtendedRequestDocumentParameterType requestParameter= request.getExtendedRequestDocumentParameter();
			
			Set<String> codes = new HashSet<String>();
			Set<String> businessDocumentTypes = new HashSet<String>();
			String documentId = null;
			
			BigDecimal paginationFrom = null;
			BigDecimal paginationTo = null;
			
			SortFieldTypeCode sortField = SortFieldTypeCode.SUBMISSION_DATE;
			
			Date startDate = null;
			Date endDate = null;
			Set<Long> senders = new HashSet<Long>();
			if (requestParameter != null){
				
				if( requestParameter.getPeriod() != null){
					startDate = requestParameter.getPeriod().getStartDate().getValue().toGregorianCalendar().getTime();
				}
				
				if(requestParameter.getPeriod() != null){
					endDate = requestParameter.getPeriod().getEndDate().getValue().toGregorianCalendar().getTime();
				}
				
				// Use default sorting (Issue date) if the user is looking for a conversation
				if(requestParameter.getSortFieldTypeCode()!= null && message.getHeader().getCorrelationId() == null){
					sortField = SortFieldTypeCode.valueOf(requestParameter.getSortFieldTypeCode().getValue());
				}

				List<DocumentTypeCodeType> typecodes = requestParameter.getDocumentTypeCode();
				for (DocumentTypeCodeType documentTypeCodeType : typecodes) {
					codes.add(documentTypeCodeType.getValue());
				}
				
				Boolean retrievedInd = false;
				if (requestParameter.getRetrievedDocumentsIndicator() != null && requestParameter.getRetrievedDocumentsIndicator().size() ==1){
					if (requestParameter.getRetrievedDocumentsIndicator().get(0).isValue()){
						retrievedInd = null;
					}
				}
				
				Boolean filterOutMessagesInError = true;
				if (requestParameter.getIncludeMessagesInErrorIndicator() != null ){
					filterOutMessagesInError = !requestParameter.getIncludeMessagesInErrorIndicator().isValue();
				}
				String userId = requestParameter.getUserID().getValue();
				
				
			
				final Configuration config = new Configuration();
				businessDocumentTypes.addAll(queryFor("//*:QueryRequest/*:ExtendedRequestDocumentParameter/*:BusinessDocumentType", config.buildDocument(new StreamSource(new StringReader(message.getPayload()))), config));
				
				//For Case 2 (Conversation search), pagination is skipped
				paginationFrom = ((requestParameter.getPaginationRange() != null) && (message.getHeader().getCorrelationId() == null))? requestParameter.getPaginationRange().getFrom():null;
				paginationTo = ((requestParameter.getPaginationRange() != null) && (message.getHeader().getCorrelationId() == null)) ? requestParameter.getPaginationRange().getTo():null;				
				
				// for justice receiver is irrelevant
				
				senders.add(message.getHeader().getSenderPartyId());
				// firs		
				queryResults = messageService.retrieveMessagesWithConversation(null, senders, null, null,
						null, transactions, codes,documentId, retrievedInd, startDate, endDate, message.getHeader().getCorrelationId(), MAX_RESULT,true, false,false, userId, businessDocumentTypes, paginationFrom, paginationTo, sortField,filterOutMessagesInError);
				
			}else {
					senders.add(message.getHeader().getSenderPartyId());
					queryResults.getMessages().addAll(messageService.retrieveMessages(null, senders,
							null, transactions, codes,documentId, false, startDate, endDate, message.getHeader().getCorrelationId(), MAX_RESULT, true, false, true,true,null));
					
				}	
			
			//Response building
			
			QueryResponseType responseType = new QueryResponseType();
			
			List<ExtendedDocumentReferenceResponseParentDocsType> documentReferenceResponseParentDocs = responseType.getExtendedDocumentReferenceResponseParentDocs();
			for (Message msg : queryResults.getMessages()) {
				ExtendedDocumentReferenceResponseParentDocsType docParentRef = new ExtendedDocumentReferenceResponseParentDocsType();
				documentReferenceResponseParentDocs.add(docParentRef);
				docParentRef.setExtendedDocumentReferenceResponse(populateExtendedDocumentReferenceResponseType(msg));
			}
			responseType.setNumberOfItems(BigDecimal.valueOf(queryResults.getSize()));
			
			Transaction t= authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName =new ObjectFactory().createQueryResponse(responseType).getName().getLocalPart();
			JAXBElement<QueryResponseType> responseElement = new JAXBElement<QueryResponseType>(
					new QName(t.getNamespace(),
							responseElementLocalName, "ec"), 
							QueryResponseType.class,responseType);
			
			responseMessage.setPayload(responseElement);
			return responseMessage;
		}catch (JAXBException e) {
			logger.error(e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
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
