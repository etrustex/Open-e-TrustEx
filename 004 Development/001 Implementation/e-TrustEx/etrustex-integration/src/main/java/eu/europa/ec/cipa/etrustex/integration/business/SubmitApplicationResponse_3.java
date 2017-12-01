package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.applicationresponse_3.ApplicationResponseType;
import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineConfigException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineTransitionException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class SubmitApplicationResponse_3 extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(SubmitApplicationResponse_3.class);
	
	private static JAXBContext jaxbContext = null;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		// in the case of the application response the sender of the application
		// response must be the receiver of the
		// the message he submits the application repose for
		try {
			Source s = new StreamSource(new StringReader(message.getPayload()));
			JAXBElement<ApplicationResponseType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, ApplicationResponseType.class);
			ApplicationResponseType request = requestElement.getValue();

			String referencedDocumentId = request.getDocumentResponse().getDocumentReference().getInstanceIdentifier();
			if (referencedDocumentId != null) {
				referencedDocumentId = referencedDocumentId.trim();
			}
			logger.trace("referencedDocumentId is [{}]", referencedDocumentId);
			String referencedDocumentTypeCode = request.getDocumentResponse().getDocumentReference().getType();
			logger.trace("referencedDocumentTypeCode is [{}]", referencedDocumentTypeCode);
			String responseCode = request.getDocumentResponse().getResponse().getResponseCode().getValue();
			logger.trace("responseCode is [{}]", responseCode);
			logger.debug("queries are done");

			logger.trace("retrieving message(s)");
			List<Message> msgList = messageService.retrieveMessages(message.getHeader().getSenderPartyId(), message.getHeader().getReceiverPartyId(), null, null, null, null, referencedDocumentId,
					referencedDocumentTypeCode, null, Boolean.FALSE, Boolean.FALSE, true);
			
			//ETRUSTEX-993 app response is sent by the sender of the RequestForQuotation
			if (msgList.size() == 0) {
				msgList.addAll(messageService.retrieveMessages(message.getHeader().getReceiverPartyId(), message.getHeader().getSenderPartyId(), 
						null, null, null, null, referencedDocumentId, referencedDocumentTypeCode, null, Boolean.FALSE, Boolean.FALSE, true));
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

			message.getHeader().setResponseCode(responseCode);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			detachParent(message);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve request", null, "301", "Technical Error Occured");			
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
	
	private static JAXBContext getJaxBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(ApplicationResponseType.class);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}

}
