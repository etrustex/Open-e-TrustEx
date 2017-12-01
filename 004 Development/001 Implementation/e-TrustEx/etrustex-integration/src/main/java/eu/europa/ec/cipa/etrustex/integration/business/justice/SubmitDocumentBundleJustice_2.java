package eu.europa.ec.cipa.etrustex.integration.business.justice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.DataExtractionTypes;
import eu.europa.ec.cipa.etrustex.types.DocumentStatusCode;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.ResponseCode;
import eu.europa.ec.cipa.etrustex.types.SlaType;

public class SubmitDocumentBundleJustice_2 extends TrustExBusinessService
		implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory
			.getLogger(SubmitDocumentBundleJustice_2.class);
	
	private final static String APP_RESPONSE_TRA_NAME = "SubmitApplicationResponse";
	
	private final static String PORTAL_PTY_ID = "DG_JUSTICE";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		final Configuration config = new Configuration();
		final StaticQueryContext sqc = config.newStaticQueryContext();
		Transaction documentWrappertransaction = authorisationService
				.getTransactionByNameSpace(
						"ec:services:wsdl:DocumentWrapper-2",
						"StoreDocumentWrapperRequest");
		
		try {
			Message parentMessage = messageService.retrieveMessage(message
					.getHeader().getMessageId());
			
			String doctypeCode = parentMessage.getTransaction().getDocument()
					.getDocumentTypeCode();
			
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			
			String userID = queryForSingle("//*:DocumentBundle/*:UserID",docInfo, config);
			
			String receiverContact  = queryForSingle("//*:DocumentBundle/*:ReceiverParty/*:Contact/*:ID",docInfo, config);
			String countrySubEnCode = queryForSingle("//*:DocumentBundle/*:ReceiverParty/*:PhysicalLocation/*:CountrySubentityCode",docInfo, config);
			
			// VALIDATIONS
			//--Header Info 2.1.3
//			if(message.getHeader().getInstanceIdentifier() == null || message.getHeader().getInstanceIdentifier().length() == 0){
//				throw new BusinessException(
//						"soapenv:Client",
//						"Instance Identifier not present in the header",
//						null,ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR,doctypeCode,
//						ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR.getDescription() + " - Instance Identifier not present in the header");
//			}
//			if(message.getHeader().getReplyTo() == null || message.getHeader().getReplyTo().length() == 0){
//				throw new BusinessException(
//						"soapenv:Client",
//						"ReplyTo not present in the header",
//						null,ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR,doctypeCode,
//						ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR.getDescription()+" - ReplyTo not present in the header");
//			}
			
			//--Correlation-Id 2.1.6
			String correlationId = message.getHeader().getCorrelationId() != null ? message.getHeader().getCorrelationId() : "";
			Set<String> typeCodes = new HashSet<String>(); 
			typeCodes.add(doctypeCode);
			List<Message> messages = messageService.retrieveMessages(null, null, null, null, typeCodes, null, Boolean.FALSE, null, null, correlationId, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, true, null);
			//String originator = userID;
			//Date oDate = parentMessage.getAccessInfo().getCreationDate();
			if(messages.size() > 0){				
				for (Message msg : messages) {
					if(!(DocumentStatusCode.SUBMITTED.name().equals(msg.getStatusCode())) && !(DocumentStatusCode.ERROR.name().equals(msg.getStatusCode())) && msg.getAdditionalInfoAsMap().get(DataExtractionTypes.JMS_USER_ID.name())!= null){						
						String currentUser =  msg.getAdditionalInfoAsMap().get(DataExtractionTypes.JMS_USER_ID.name()).toString();												
						if(!userID.equals(currentUser)){
							throw new BusinessException(
									"soapenv:Client",
									"Message with the same CorrelationID has been sent by a different Originator",
									null,ErrorResponseCode.DOCUMENT_BUNDLE_JUST_ALREADY_EXISTS, null,
									ErrorResponseCode.DOCUMENT_BUNDLE_JUST_ALREADY_EXISTS.getDescription());
						}
					}
				}
			}			

			List<String> dwr = queryFor("//*:DocumentBundle/*:DocumentWrapperReference/*:ID",docInfo, config);
			List<Message> messagesToUpdate = new ArrayList<Message>();
			List<String> wrappersIDs = new ArrayList<String>();
			for (String wrapperDocId : dwr) {				
				if (wrappersIDs.contains(wrapperDocId)) {
					throw new BusinessException(
							"soapenv:Client",
							"Duplicate document wrapper referenced",
							null, ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR,doctypeCode,
							ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR.getDescription()+" - Duplicate document wrapper referenced");
				} else {
					wrappersIDs.add(wrapperDocId);
				}
				Message msg = messageService
						.retrieveMessage(wrapperDocId, message.getHeader().getSenderPartyId(),
								documentWrappertransaction.getId());
				if (msg == null) {
					throw new BusinessException(
							"soapenv:Client",
							"Non Existing document Wrapper",
							null,ErrorResponseCode.DOCUMENT_BUNDLE_JUST_MISSING_ATT,doctypeCode,
							ErrorResponseCode.DOCUMENT_BUNDLE_JUST_MISSING_ATT.getDescription());
				}
				messagesToUpdate.add(msg);
			}
			
			//ETRUSTEX-623 validate number of wrappers against the SLA policy
			Party sender = authorisationService.getParty(message.getHeader().getSenderPartyId());
			if (!validateNumberOfWrappers(sender, wrappersIDs.size())) {
				throw new BusinessException( "soapenv:Client","Maximum number of wrappers exceeded", 
						null,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED,
						doctypeCode ,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED.getDescription());
			}			
			
			if(PORTAL_PTY_ID.equals(parentMessage.getSender().getName())){
				parentMessage.setRetrieveIndicator(true);
			}
			
			for (Message messageToUpdate : messagesToUpdate) {
				if (messageToUpdate.getParentMessages() != null
						&& !messageToUpdate.getParentMessages().contains(
								parentMessage)) {
					messageToUpdate.addParentMessage(parentMessage);
				}
				messageService.updateMessage(messageToUpdate);
			}
			
			
			//Sending the RESPONSE with Code JUS:8 only when the portal is the sender 
			//TO DO 
			try {
				//if (PORTAL_PTY_ID.equalsIgnoreCase(message.getHeader().getSenderIdWithScheme())){
					createApplicationResponse( message,message.getHeader()
							.getMessageDocumentId()+"_OK", ResponseCode.DOCUMENT_BUNDLE_JUST_OK.getCode(),"Notification: processing ok",message.getHeader().getReplyTo());
			//	}
			} catch (Exception e) {
				logger.error("Error sending the 2nd Acknowledgement - JUS:8", e);
			}	
			
			
			//TODO Check For message to forward and call the adaptor
			
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to retrieve parent information with query ", null,
					"301", "Technical Error Occured");
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to update the message status", null, "301",
					"Technical Error Occured");
		} 

		return message;
	}
	
	private boolean validateNumberOfWrappers(Party sender, int wrapperCount) {
		SlaPolicySearchDTO slaPolicySearchDTO = new SlaPolicySearchDTO();
		slaPolicySearchDTO.setSender(sender);
		slaPolicySearchDTO.setSlaType(SlaType.SLA_COUNT);
		return slaPolicyService.validateNumberOfWrappers(slaPolicySearchDTO, wrapperCount);
	}
	
}
