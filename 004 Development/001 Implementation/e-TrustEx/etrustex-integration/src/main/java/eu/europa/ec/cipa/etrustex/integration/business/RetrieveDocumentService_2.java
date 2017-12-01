package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ReceivedDateType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ec.schema.xsd.commonaggregatecomponents_2.DocumentContentType;
import ec.schema.xsd.retrieverequest_2.RetrieveRequestType;
import ec.schema.xsd.retrieveresponse_2.ObjectFactory;
import ec.schema.xsd.retrieveresponse_2.RetrieveResponseType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

public class RetrieveDocumentService_2 extends TrustExBusinessService implements
		ISynchBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(RetrieveDocumentService_2.class);	
	
	private static JAXBContext jaxbContext = null;
	
	private static DatatypeFactory dtf = null;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(
			TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
		try {
			
			Source s= new StreamSource(new StringReader(message.getPayload()));
			JAXBElement<RetrieveRequestType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s,RetrieveRequestType.class);
			RetrieveRequestType request = requestElement.getValue();
			
			Party senderParty = null;			
			//build the string sender id with scheme
			String partyIdWithScheme = "";
			if (StringUtils.isNotEmpty(request.getSenderParty().getEndpointID().getSchemeID())) {
				partyIdWithScheme += request.getSenderParty().getEndpointID().getSchemeID() + "#";
			}
			partyIdWithScheme += request.getSenderParty().getEndpointID().getValue();
			try {				
				senderParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());
				
			} catch (UndefinedIdentifierException e) {
				// IF exception 
			}
			
			Party receiverParty=null;
			//build the string receiver id with scheme
			partyIdWithScheme = "";
			if (StringUtils.isNotEmpty(request.getReceiverParty().getEndpointID().getSchemeID())) {
				partyIdWithScheme += request.getReceiverParty().getEndpointID().getSchemeID() + "#";
			}
			partyIdWithScheme += request.getReceiverParty().getEndpointID().getValue();

			try {
				receiverParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());
				
			} catch (Exception e) {
			}
			
			if (receiverParty == null && senderParty == null){
				
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null, 
						ErrorResponseCode.AUTHENTICATION_FAILED,null, null);
			}
			if ((receiverParty != null  && receiverParty.getId().equals(message.getHeader().getSenderPartyId())) || 
					(senderParty!= null &&  senderParty.getId().equals(message.getHeader().getSenderPartyId()))){
				
				String messageDocumentId = request.getDocumentReferenceRequest().getID().getValue();
				if(messageDocumentId != null){
					messageDocumentId = messageDocumentId.trim();
				}
				String documentTypeCode = request.getDocumentReferenceRequest().getDocumentTypeCode().getValue();
				Set<Transaction> transactions = null;
				if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0){
					transactions= retrieveThirdPartyAuthorisedTransactions(message.getHeader());
				}
				List<Message> queryResult =null;
				if (receiverParty != null && senderParty != null){
					queryResult = messageService.retrieveMessages(receiverParty.getId(), 
							senderParty.getId(), null, null, transactions, null, messageDocumentId,documentTypeCode,null, false, true,false);
				}
				
				if (queryResult != null && queryResult.size() >1){
					throw new BusinessException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
							ErrorResponseCode.TECHNICAL_ERROR, null, null);
				}
				ObjectFactory fact = new ObjectFactory();
				RetrieveResponseType response = fact.createRetrieveResponseType();
				
				DocumentContentType contentType = new DocumentContentType();
				
				response.setDocumentContent(contentType);
				responseMessage.setHeader(message.getHeader());
				if (queryResult != null && queryResult.size() >0) {
					response.setSenderParty(populateParty(senderParty.getIdentifiers()));
					response.setReceiverParty(populateParty(receiverParty.getIdentifiers()));
					Message retrieved = queryResult.get(0);
					ReceivedDateType receptionDate = new ReceivedDateType();
					XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(retrieved.getReceptionDate()));
					receptionDate.setValue(xgcal);
					response.setReceivedDate(receptionDate);
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					factory.setNamespaceAware(true);

			
					Set<MessageBinary> binaries = retrieved.getBinaries();
					for (MessageBinary messageBinary : binaries) {
						if (MessageBinaryType.RAW_MESSAGE.name().equals(messageBinary.getBinaryType())){
							//Document doc =   factory.newDocumentBuilder().parse(
								//	messageService.getMessageBinaryAsStream(messageBinary.getId()));
							
							
						
						InputStreamReader in = new InputStreamReader(
								messageService.getMessageBinaryAsStream(messageBinary.getId()), "utf-8" );
		
							BufferedReader reader = new BufferedReader(in); // CHANGED
		
							InputSource input = new InputSource(reader);
		
						
							Document doc =   factory.newDocumentBuilder().parse(
								input);
					
							
							
							contentType.setAny(doc.getDocumentElement());
						}
						if (MessageBinaryType.BINARY_ATTACHMENT.name().equals(messageBinary.getBinaryType())){
							TrustExMessageBinary bin = new TrustExMessageBinary();
							bin.setMimeType(messageBinary.getMimeCode());
							bin.setBinaryContent(messageBinary.getBinary());
							responseMessage.addBinary(bin);
						}
					}
					if (request.getRetrieveIndicator().isValue() && (receiverParty != null  && receiverParty.getId().equals(message.getHeader().getSenderPartyId()))){
						retrieved.setRetrieveIndicator(true);
						messageService.updateMessage(retrieved);
					}
				}
				Transaction t= authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
				String responseElementLocalName =fact.createRetrieveResponse(response).getName().getLocalPart();
				
				JAXBElement<RetrieveResponseType> responseElement = new JAXBElement<RetrieveResponseType>(
						new QName(t.getNamespace(),
								responseElementLocalName, "ec"), 
								RetrieveResponseType.class,response);
				responseMessage.setPayload(responseElement);
				//responseMessage.setPayload(fact.createRetrieveResponse(response));
				return responseMessage;
			}else{
				
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription()
						, null,ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
		}catch (BusinessException e) {
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} 

	}
	private static JAXBContext getJaxBContext(){
		if (jaxbContext == null){
			try {
				jaxbContext = JAXBContext.newInstance(RetrieveRequestType.class);				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}
	
	private static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null){			
			dtf = DatatypeFactory.newInstance();									
		}
		return dtf;
	}
}
