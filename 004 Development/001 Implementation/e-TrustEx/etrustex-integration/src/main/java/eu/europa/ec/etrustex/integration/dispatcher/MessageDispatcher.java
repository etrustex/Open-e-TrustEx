package eu.europa.ec.etrustex.integration.dispatcher;

import eu.europa.ec.etrustex.domain.routing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.integration.TrustExIntegrationSupport;
import eu.europa.ec.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.etrustex.services.ILogService;

public abstract class MessageDispatcher  extends TrustExIntegrationSupport{


	@Autowired
	protected ILogService logService;

	@Autowired
	protected LogServiceHelper logServiceHelper;

	protected static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

	public static MessageDispatcher getMessageDispatcher(Endpoint endpoint){
		if (endpoint instanceof JMSEndpoint){
			return new JMSMessageDispatcher(endpoint);
		} else 	if (endpoint instanceof WSEndpoint){
			return new WSMessageDispatcher(endpoint);
		} else if (endpoint instanceof AMQPEndpoint) {
			return new AmqpMessageDispatcher((AMQPEndpoint)endpoint);
		}

		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void dispatchMessage(TrustExMessage<String> message,Transaction transaction) throws MessageRoutingException{
		sendMessage(transformMessage(message,transaction));
	}

	protected abstract TrustExMessage<String> transformMessage(TrustExMessage<String> message,Transaction transaction) throws MessageRoutingException;

	protected abstract void sendMessage(TrustExMessage<String> message)	throws MessageRoutingException;

}
