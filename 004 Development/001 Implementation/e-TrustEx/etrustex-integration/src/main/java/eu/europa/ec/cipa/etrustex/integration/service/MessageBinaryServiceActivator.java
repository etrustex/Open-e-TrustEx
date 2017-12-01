package eu.europa.ec.cipa.etrustex.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.services.dto.CreateMessageBinaryDTO;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageCreationException;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

/**
 * 
 * This Servuce activator is used to store and retrieve large binaries stored in
 * the platform this class can be configured to store the to the file system
 * (recommended option) or to the
 * 
 * @author orazisa
 *
 */
public class MessageBinaryServiceActivator extends TrustExServiceActivator {

	private static final Logger logger = LoggerFactory.getLogger(MessageBinaryServiceActivator.class);

	// @Transactional(propagation=Propagation.REQUIRED, timeout=3000)
	public Message<Long> storeMessageBinary(Message<TrustExMessageBinary> messageBinary) {
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.CRUD, this.getClass().getName())
			.description("Inside MessageBinaryServiceActivator")
			.build();
		try {
			
			logDTO.setBusinessDomain(messageBinary.getPayload().getIssuerParty().getBusinessDomain());
			logDTO.setIssuerParty(messageBinary.getPayload().getIssuerParty());
			logDTO.setDocumentId(messageBinary.getPayload().getMessageDocumentId());
			logDTO.setSenderParty(messageBinary.getPayload().getSenderParty());			
			
			Long id = messageService.createMessageBinary(
				new CreateMessageBinaryDTO.Builder()
					.binaryType(messageBinary.getPayload().getBinaryType())
					.mimeType(messageBinary.getPayload().getMimeType())
					.inputStream(messageBinary.getPayload().getBinaryContentIS())
					.encryptBinary(true)
					.senderIdWithScheme(messageBinary.getPayload().getSubmitterIdWithScheme())
					.expectedSize(messageBinary.getPayload().getExpectedSize())
					.issuer(messageBinary.getPayload().getIssuerParty())
					.documentId(messageBinary.getPayload().getMessageDocumentId())
					.build());
			
			logDTO.setMessageBinaryId(id);

			
			MessageBuilder<Long> builder = MessageBuilder.withPayload(id).copyHeaders(messageBinary.getHeaders());
			return builder.build();
		} catch (MessageCreationException e) {
			logger.error(e.getMessage(), e);
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			throw new MessageProcessingException(e.getResponseCode().getFaultCode(), e.getResponseCode().getDescription(), null, e.getResponseCode(), null, null);
		} 
	}

	public Message<Long> deleteMessageBinary(Message<Long> messageBinaryId) {
		BusinessDomain businessDomain = null;

		MessageBinary messageBinary = messageService.retrieveMessageBinary(messageBinaryId.getPayload());
		if (messageBinary != null && messageBinary.getMessage() != null && messageBinary.getMessage().getSender() != null) {
			businessDomain = messageBinary.getMessage().getSender().getBusinessDomain();
		}

		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.CRUD, this.getClass().getName())
			.businessDomain(businessDomain)
			.senderParty(messageBinary.getMessage() != null ? messageBinary.getMessage().getSender() : null)
			.description("Inside MessageBinaryServiceActivator")
			.build();
		try {
			messageService.deleteMessageBinary(messageBinaryId.getPayload());
			logDTO.setMessageBinaryId(messageBinaryId.getPayload());
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
		return messageBinaryId;
	}
}
