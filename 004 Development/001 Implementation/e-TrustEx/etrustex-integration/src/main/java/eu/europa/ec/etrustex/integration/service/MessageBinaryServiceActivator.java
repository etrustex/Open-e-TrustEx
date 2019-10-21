package eu.europa.ec.etrustex.integration.service;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;

/**
 * 
 * This Service activator is used to store and retrieve large binaries stored in
 * the platform this class can be configured to store to the file system
 * (recommended option) or to the database
 * 
 * @author orazisa
 *
 */
@Component("messageBinaryServiceActivator")
public class MessageBinaryServiceActivator extends TrustExServiceActivator {

	private static final Logger logger = LoggerFactory.getLogger(MessageBinaryServiceActivator.class);

	@Transactional
	public Message<TrustExMessage<String>> storeMessageBinary(Message<TrustExMessage<String>> message) {
		if(CollectionUtils.isEmpty(message.getPayload().getBinaries())) {
			return message;
		}
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.CRUD, this.getClass().getName())
			.description("Inside MessageBinaryServiceActivator")
			.build();
		TrustExMessageBinary messageBinary = message.getPayload().getBinaries().iterator().next();
		try {
			
			logDTO.setBusinessDomain(messageBinary.getIssuerParty().getBusinessDomain());
			logDTO.setIssuerParty(messageBinary.getIssuerParty());
			logDTO.setDocumentId(messageBinary.getMessageDocumentId());
			logDTO.setSenderParty(messageBinary.getSenderParty());			
			
			Long id = messageService.createMessageBinary(
				new CreateMessageBinaryDTO.Builder()
					.binaryType(messageBinary.getBinaryType())
					.mimeType(messageBinary.getMimeType())
					.inputStream(messageBinary.getBinaryContentIS())
					.encryptBinary(true)
					.senderIdWithScheme(messageBinary.getSubmitterIdWithScheme())
					.expectedSize(messageBinary.getExpectedSize())
					.issuer(messageBinary.getIssuerParty())
					.documentId(messageBinary.getMessageDocumentId())
					.build());
			
			logDTO.setMessageBinaryId(id);
			messageBinary.setBinaryId(id);
			
			return message;
		} catch (MessageCreationException e) {
			logger.error(e.getMessage(), e);
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			throw new MessageProcessingException(e.getResponseCode().getFaultCode(), e.getResponseCode().getDescription(), null, e.getResponseCode(), null, e.getDetailedDescription());
		} 
	}	
}
