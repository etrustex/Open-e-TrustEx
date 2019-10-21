package eu.europa.ec.etrustex.integration;

import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;

public interface IMessageProcessorActivator {
	
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message) throws MessageProcessingException;
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message)throws MessageProcessingException;

}
