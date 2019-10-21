package eu.europa.ec.etrustex.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.dao.exception.StateMachineConfigException;
import eu.europa.ec.etrustex.dao.exception.StateMachineException;
import eu.europa.ec.etrustex.dao.exception.StateMachineTransitionException;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

public class SubmitApplicationResponse_2 extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(SubmitApplicationResponse_2.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		// in the case of the application response the sender of the application
		// response must be the receiver of the
		// the message he submits the application repose for
		try {
			final Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));

			String referencedDocumentId = queryForSingle("//*:ApplicationResponse/*:DocumentResponse/*:DocumentReference/*:ID", docInfo, config);
			if (referencedDocumentId != null) {
				referencedDocumentId = referencedDocumentId.trim();
			}
			logger.trace("referencedDocumentId is [{}]", referencedDocumentId);
			String referencedDocumentTypeCode = queryForSingle("//*:ApplicationResponse/*:DocumentResponse/*:DocumentReference/*:DocumentTypeCode", docInfo, config);
			logger.trace("referencedDocumentTypeCode is [{}]", referencedDocumentTypeCode);
			String responseCode = queryForSingle("//*:ApplicationResponse/*:DocumentResponse/*:Response/*:ResponseCode", docInfo, config);
			logger.trace("responseCode is [{}]", responseCode);
			logger.debug("queries are done");
			logger.trace("retrieving message(s)");
			
			MessageQueryDTO mq = new MessageQueryDTO();
			mq.setReceiverPartyId(message.getHeader().getSenderPartyId());
			mq.setSenderPartyId( message.getHeader().getReceiverPartyId());
			mq.setMessageDocumentId(referencedDocumentId);
			mq.setDocumentTypeCode(referencedDocumentTypeCode);
			
			List<Message> msgList = messageService.retrieveMessages(mq);
			
			//ETRUSTEX-993 app response is sent by the sender of the RequestForQuotation
			if (msgList.size() == 0) {
				mq = new MessageQueryDTO();
				mq.setReceiverPartyId(message.getHeader().getReceiverPartyId());
				mq.setSenderPartyId( message.getHeader().getSenderPartyId());
				mq.setMessageDocumentId(referencedDocumentId);
				mq.setDocumentTypeCode(referencedDocumentTypeCode);				
				msgList.addAll(messageService.retrieveMessages(mq));
			}

			int messageCount = msgList.size();
			logger.debug("retrieved {} message(s)", messageCount);

			if (messageCount == 0) {
				logger.error("expected exactly one message, but query returned no message");
				throw new BusinessException("soapenv:Client", ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getDescription(), null, ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST, "307",
						ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getDescription());
			}
			if (messageCount != 1) {
				logger.error("expected exactly one message, got {} message(s) instead", messageCount);
				throw new BusinessException("soapenv:Client", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, ErrorResponseCode.TECHNICAL_ERROR, "307",
						ErrorResponseCode.TECHNICAL_ERROR.getDescription());
			}

			Message referencedMessage = msgList.get(0);
			logger.debug("retrieved referenced message");
			Long referencedMessageId = referencedMessage.getId();
			logger.trace("referencedMessageId is [{}]", referencedMessageId);

			Long agreementId = referencedMessage.getAgreement() == null ? null : referencedMessage.getAgreement().getId();
			logger.trace("agreementId is [{}]", agreementId);

			Transaction referencedTransaction = referencedMessage.getTransaction();
			Document referencedDocument = referencedTransaction.getDocument();

			String eventName = responseCode.substring(responseCode.indexOf(":") + 1);
			logger.trace("eventName is [{}]", eventName);

			String fromState = referencedMessage.getStatusCode();

			// conditional example code using variables
			// Map<String, Object> variables = new HashMap<String, Object>();
			// variables.put("flag", Boolean.TRUE);
			// variables.put("referencedMessage", referencedMessage);
			// String targetState =
			// stateMachineService.getNextState(referencedDocument, fromState,
			// eventName, variables);
			logger.trace("querying state machine for target state");
			String targetState = stateMachineService.getNextState(referencedDocument, fromState, eventName);
			logger.debug("target state defined by state machine is [{}]", targetState);

			logger.trace("about to update message status");
			messageService.updateMessageStatus(referencedMessageId, targetState);
			// keep parent status in the header
			message.getHeader().setParentStatusCode(targetState);
			if (referencedMessageId != null) {
				message.getHeader().setParentMessageId(referencedMessageId);
			}

			logger.debug("message status was updated");

			logger.trace("about to link response message to referenced message parent");
			//chiricr: The link with the parent is already added in ParentDocumentHandlerServiceActivator
//			applicationResponse.addParentMessage(referencedMessage);

			message.getHeader().setResponseCode(responseCode);

			// messageService.updateMessage(applicationResponse);
			// messageService.updateMessageResponseCode(applicationResponse.getId(),
			// responseCode);
			logger.debug("linked response message to referenced message parent");
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			detachParent(message);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve parent information with query ", null, "301", "Technical Error Occured");
		} catch (StateMachineConfigException e) {
			logger.error(e.getMessage(), e);
			//ETRUSTEX-1179 remove parent if this application response is put into status ERROR			
			detachParent(message);	
			throw new TechnicalErrorException("soapenv:Server", "Error occured in state machine configuration", null, "301", "Technical Error Occured");
		} catch (StateMachineTransitionException e) {
			logger.error(e.getMessage(), e);
			detachParent(message);
			throw new BusinessException("soapenv:Server", ErrorResponseCode.PARENT_DOCUMENT_IS_NOT_IN_SPECIFIC_STATE.getDescription(), null,
					ErrorResponseCode.PARENT_DOCUMENT_IS_NOT_IN_SPECIFIC_STATE, "301", ErrorResponseCode.PARENT_DOCUMENT_IS_NOT_IN_SPECIFIC_STATE.getDescription());
		} catch (StateMachineException e) {
			logger.error(e.getMessage(), e);
			detachParent(message);
			throw new TechnicalErrorException("soapenv:Server", "Unhandled state machine exception", null, "301", "Technical Error Occured");
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			detachParent(message);
			throw new TechnicalErrorException("soapenv:Server", "Unable to update the message status", null, "301", "Technical Error Occured");
		}
		return message;

	}

	/**
	 * @param message
	 * @throws TechnicalErrorException
	 */
	private void detachParent(TrustExMessage<String> message) throws TechnicalErrorException {
		//ETRUSTEX-1179 remove parent if this application response is put into status ERROR			
		messageService.detachParent(message.getHeader().getMessageId());
	}

}
