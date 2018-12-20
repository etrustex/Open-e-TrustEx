package eu.europa.ec.etrustex.integration.business.justice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.policy.CountSlaPolicy;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.DocumentWrapperDTO;
import eu.europa.ec.etrustex.integration.util.XMLUtil;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.ResponseCode;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;

public class SubmitDocumentBundleJustice_2 extends TrustExBusinessService
		implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory
			.getLogger(SubmitDocumentBundleJustice_2.class);
		
	private final static String PORTAL_PTY_ID = "DG_JUSTICE";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		final Configuration config = new Configuration();
		
		try {
			Message parentMessage = messageService.retrieveMessage(message
					.getHeader().getMessageId());
			
			String doctypeCode = parentMessage.getTransaction().getDocument()
					.getDocumentTypeCode();
			
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			
			String userID = queryForSingle("//*:DocumentBundle/*:UserID",docInfo, config);

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
			RetrieveMessagesForQueryRequestDTO dto = new RetrieveMessagesForQueryRequestDTO.Builder()
					.setDocumentTypeCodes(typeCodes)
					.setRetrievedInd(false)
					.setCorrelationId(correlationId)
					.setFetchParents(false)
					.setFetchBinaries(false)
					.setIsSenderAlsoReceiver(false)
					.setFilterOutMessagesInError(true)
					.setUserId(userID)
					.build();					
			if (messageService.existsEjusticeMessageForCorrelationId(dto)) {
				throw new BusinessException(
						"soapenv:Client",
						"Message with the same CorrelationID has been sent by a different Originator",
						null,ErrorResponseCode.DOCUMENT_BUNDLE_JUST_ALREADY_EXISTS, null,
						ErrorResponseCode.DOCUMENT_BUNDLE_JUST_ALREADY_EXISTS.getDescription());
			}			

			SequenceIterator<NodeInfo> wrapperNodesIterator = queryForNode("//*:DocumentBundle/*:DocumentWrapperReference/*:ID",docInfo, config);
			List<Message> messagesToUpdate = new ArrayList<Message>();
			List<DocumentWrapperDTO> wrappersIDs = new ArrayList<>();
			while (true) {
				NodeInfo wrapperIdNode = wrapperNodesIterator.next();
				if (wrapperIdNode == null) {
					break;
				}
				String wrapperDocId = wrapperIdNode.getStringValue();
				String docTypeCode = XMLUtil.getSiblingByName(wrapperIdNode, "DocumentTypeCode").getStringValue();
				DocumentWrapperDTO documentWrapperDTO = new DocumentWrapperDTO(wrapperDocId, docTypeCode);
							
				if (wrappersIDs.contains(documentWrapperDTO)) {
					throw new BusinessException(
							"soapenv:Client",
							"Duplicate document wrapper referenced",
							null, ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR,doctypeCode,
							ErrorResponseCode.DOCUMENT_BUNDLE_JUST_HARD_BR.getDescription()+" - Duplicate document wrapper referenced");
				} else {
					wrappersIDs.add(documentWrapperDTO);
				}
				
				Message msg = messageService.retrieveMessage(wrapperDocId, docTypeCode, message.getHeader().getSenderPartyId(), null);
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
			Party sender = partyService.getParty(message.getHeader().getSenderPartyId());
			if (!validateNumberOfWrappers(sender, wrappersIDs.size())) {
				throw new BusinessException( "soapenv:Client","Maximum number of wrappers exceeded", 
						null,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED,
						doctypeCode ,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED.getDescription());
			}			
			
			if(PORTAL_PTY_ID.equals(parentMessage.getSender().getName())){
				parentMessage.setRetrievedDate(new Date());
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
                String appResponseId = generateMessageId(message.getHeader().getMessageDocumentId(), null, MSG_ID_OUTCOME.OK);
				createApplicationResponse( message,appResponseId, ResponseCode.DOCUMENT_BUNDLE_JUST_OK.getCode(),"Notification: processing ok",message.getHeader().getReplyTo());
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
		PolicySearchDTO policySearchDTO = new PolicySearchDTO();
		policySearchDTO.setSender(sender);
		policySearchDTO.setPolicyType(CountSlaPolicy.class);
		return policyService.validateNumberOfWrappers(policySearchDTO, wrapperCount);
	}
	
}
