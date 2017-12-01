package eu.europa.ec.cipa.etrustex.integration.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class AttachedDocument_2 extends TrustExBusinessService implements IASynchBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(AttachedDocument_2.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		
		try {
			Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());			
			String doctypeCode = mainMessage.getTransaction().getDocument().getDocumentTypeCode();
							
			String parentDocId = message.getHeader().getMessageParentDocumentId();
			String parentTypeCode = message.getHeader().getMessageParentDocumentTypeCode();
			
			List<Message> msgs = messageService.retrieveMessages(mainMessage.getReceiver().getId(), mainMessage.getSender().getId(), 
					null, null, null, null, parentDocId, parentTypeCode, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
			
			//ETRUSTEX-997 for the case when the sender and receiver are switched
			if (msgs.size() == 0) {
				msgs.addAll(messageService.retrieveMessages(mainMessage.getSender().getId(), mainMessage.getReceiver().getId(),
					null, null, null, null, parentDocId, parentTypeCode, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
			}
		
			if(msgs.size() != 1){
				mainMessage.setParentMessages(null);
				messageService.updateMessage(mainMessage);
				logger.error(
						"expected exactly one message, but query returned no message or more than one + ::"+msgs.size());
				throw new BusinessException("soapenv:Client",
						ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST
								.getDescription(), null,
						ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST, doctypeCode,
						"The parent document must exist and both the customer and supplier id of the attachment must be identical to the customer and supplier id of the parent document");
			}		
			
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to attach parent document", null,
					"301", "Technical Error Occured");
		}
		return message;
	}
}
