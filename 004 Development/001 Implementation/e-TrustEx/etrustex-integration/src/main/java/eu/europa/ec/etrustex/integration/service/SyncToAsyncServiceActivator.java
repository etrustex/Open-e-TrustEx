package eu.europa.ec.etrustex.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.integration.message.TrustExMessage;

public class SyncToAsyncServiceActivator extends TrustExServiceActivator {

	@Autowired
	private JmsTemplate toAsynchProcessingTemplate;
		
	private static final Logger logger = LoggerFactory.getLogger(SyncToAsyncServiceActivator.class);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Message<TrustExMessage<String>> dispatchToIntegrationQueue(Message<TrustExMessage<String>> message) {
		
		try{
			toAsynchProcessingTemplate.setPriority(message.getPayload().getHeader().getMessageHeaders().getPriority());
			toAsynchProcessingTemplate.convertAndSend(message.getPayload());
		} catch (JmsException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Unable to dispatch message to Integration Queue", e);
		}
		
		return message;
	}
	
	
	
	public JmsTemplate getToAsynchProcessingTemplate() {
		return toAsynchProcessingTemplate;
	}
	public void setToAsynchProcessingTemplate(JmsTemplate toAsynchProcessingTemplate) {
		this.toAsynchProcessingTemplate = toAsynchProcessingTemplate;
	}
	
}
