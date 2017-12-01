package eu.europa.ec.cipa.etrustex.integration.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class RetriveMessageServiceActivator  extends TrustExServiceActivator{
	
	private static final Logger logger = LoggerFactory.getLogger(RetriveMessageServiceActivator.class);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Message<TrustExMessage<String>> retrieveMessageContent(Message<TrustExMessage<String>> message) {
		TrustExMessageHeader header = message.getPayload().getHeader();
		eu.europa.ec.cipa.etrustex.domain.Message msg = messageService.retrieveMessage(header.getMessageId());
		
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.VALIDATION, 
				"Inside RetriveMessageServiceActivator", 
				this.getClass().getName());	
		
		if((msg.getAgreement() ==null) || (msg.getAgreement().getProfile() == null) || (msg.getTransaction() == null) ||  (msg.getTransaction().getDocument() == null)){
			logger.error("Corrupt DB Message with ID:"+header.getMessageId()+"\n"+msg.toString());
			logDTO.setDescription(logDTO.getDescription() + "Corrupt DB Message with ID:" + header.getMessageId() + "\n"+msg.toString());
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
					null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		}
		Map<MetaDataItemType, MetaDataItem > md = metadataService.retrieveMetaData(msg.getAgreement().getId(),msg.getTransaction().getId(),  msg.getTransaction().getDocument().getId() ,msg.getAgreement().getProfile().getId());
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
