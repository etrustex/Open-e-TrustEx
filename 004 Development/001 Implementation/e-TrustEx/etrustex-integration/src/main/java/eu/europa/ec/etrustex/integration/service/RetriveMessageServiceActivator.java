package eu.europa.ec.etrustex.integration.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class RetriveMessageServiceActivator  extends TrustExServiceActivator {
	
	private static final Logger logger = LoggerFactory.getLogger(RetriveMessageServiceActivator.class);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Message<TrustExMessage<String>> retrieveMessageContent(Message<TrustExMessage<String>> message) {
		TrustExMessageHeader header = message.getPayload().getHeader();
		eu.europa.ec.etrustex.domain.Message msg = messageService.retrieveMessage(header.getMessageId());

		if (header.getMessageId() == null) {
			logger.error("Message ID null in incoming JMS message");
		}

		if (msg == null) {
			logger.error("Message not found in DB, ID: " + header.getMessageId());
		}

		if (msg != null && msg.getAgreement() == null) {
			logger.error("Message with null ICA, ID: " + header.getMessageId());
		}

		if (msg != null && msg.getTransaction() == null) {
			logger.error("Message with null transaction, ID: " + header.getMessageId());
		}
		
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.VALIDATION, 
				"Inside RetriveMessageServiceActivator", 
				this.getClass().getName());	
		
		if((msg == null) || (msg.getAgreement() ==null) || (msg.getAgreement().getProfile() == null) || (msg.getTransaction() == null) ||  (msg.getTransaction().getDocument() == null)){
			logger.error("Corrupt DB Message with ID:"+header.getMessageId()+"\n"+msg.toString());
			logDTO.setDescription(logDTO.getDescription() + "Corrupt DB Message with ID:" + header.getMessageId() + "\n"+msg.toString());
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
					null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		}
		
		Map<MetaDataItemType, MetaDataItem > md = metadataService.retrieveMetaData(msg.getAgreement().getId(),msg.getTransaction().getId(),  msg.getTransaction().getDocument().getId() ,msg.getAgreement().getProfile().getId(), null);
		header.setMetadata(md);
		Date receptionDate =msg.getReceptionDate();
		header.setReceivedDate(receptionDate);
		String raw=  messageService.getMessageBinaryAsString(header.getMessageId(), MessageBinaryType.RAW_MESSAGE);
		message.getPayload().setPayload(raw);
		//System.out.println("The Raw message is :" +raw);
		MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
		return builder.build();
	}
}
