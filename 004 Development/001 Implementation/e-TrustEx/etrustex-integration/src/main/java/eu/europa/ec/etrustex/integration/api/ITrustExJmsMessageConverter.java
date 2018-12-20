package eu.europa.ec.etrustex.integration.api;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConverter;

import eu.europa.ec.etrustex.integration.message.TrustExMessage;


/**
 * 
 * This interface is mimic the spring {@link MessageConverter} but rtemoving dependencies to the spring framework
 * this allows implemeters to speci
 * @author orazisa
 *
 */
public interface ITrustExJmsMessageConverter  {
	
	/**
	 * Convert TrustExMessage object to a JMS Message using the supplied session
	 * to create the message object.
	 * @param object the object to convert
	 * @param session the Session to use for creating a JMS Message
	 * @return the JMS Message
	 * @throws javax.jms.JMSException if thrown by JMS API methods
	 */
	Message toMessage(TrustExMessage<String> message, Session session) throws JMSException;

	/**
	 * Convert from a JMS Message to a TrustExMessage object.
	 * @param message the message to convert
	 * @return the converted Java object
	 * @throws javax.jms.JMSException if thrown by JMS API methods
	 */
	TrustExMessage<String> fromMessage(Message message) throws JMSException;

}
