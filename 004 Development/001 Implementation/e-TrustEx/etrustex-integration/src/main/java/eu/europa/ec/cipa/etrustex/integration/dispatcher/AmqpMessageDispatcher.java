package eu.europa.ec.cipa.etrustex.integration.dispatcher;

import java.io.IOException;

import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.messenger.Messenger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.routing.AMQPEndpoint;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.AmqpMessageConverter;

public class AmqpMessageDispatcher extends MessageDispatcher implements ApplicationContextAware {
	
	private AMQPEndpoint endpoint;
	
	private AmqpMessageConverter amqpMessageConverter;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public AmqpMessageDispatcher(AMQPEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	protected TrustExMessage<String> transformMessage(TrustExMessage<String> message, Transaction transaction)
			throws MessageRoutingException {
		return message;
	}

	@Override
	protected void sendMessage(TrustExMessage<String> message) throws MessageRoutingException {
		amqpMessageConverter = applicationContext.getBean(AmqpMessageConverter.class);
		Message amqpMessage = amqpMessageConverter.convert(message);
		Messenger messenger = Messenger.Factory.create();
		try {
			messenger.start();
		} catch (IOException e) {
			throw new MessageProcessingException(e.getMessage(), "");
		}
		String address = endpoint.getProviderUrl();
		if (endpoint.getIsAuthenticationRequired() && endpoint.getCredentials() != null) {
			//add username:password to URL
			address = address.replace("amqp://", 
					"amqp://" + endpoint.getCredentials().getUser() + ":" + endpoint.getCredentials().getPassword() + "@");
		}
		amqpMessage.setAddress(address);
		messenger.put(amqpMessage);
		messenger.send();
		messenger.stop();
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;		
	}

}
