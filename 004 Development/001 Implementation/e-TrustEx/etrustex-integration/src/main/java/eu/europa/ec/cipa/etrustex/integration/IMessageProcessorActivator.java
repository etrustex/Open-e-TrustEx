package eu.europa.ec.cipa.etrustex.integration;

import org.springframework.integration.Message;

import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;

public interface IMessageProcessorActivator {
	
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message) throws MessageProcessingException;
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message)throws MessageProcessingException;

}
