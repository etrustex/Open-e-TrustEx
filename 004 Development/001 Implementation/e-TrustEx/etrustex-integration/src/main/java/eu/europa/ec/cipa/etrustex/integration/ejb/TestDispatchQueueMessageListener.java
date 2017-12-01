/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.ejb;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.transport.jms.WebServiceMessageListener;

/**
 * @author chiricr
 *
 */
public class TestDispatchQueueMessageListener extends WebServiceMessageListener {

	private Logger logger = LoggerFactory.getLogger(TestDispatchQueueMessageListener.class);
		
	@Override
	public void onMessage(Message message, Session session) {
		
		try {
			System.out.println("--------------------------------------------------");
			System.out.println("AMQP test dispatch queue, received: " + ((TextMessage)message).getText());
			System.out.println("--------------------------------------------------");
			
		} catch (Exception e) {
			logger.error("Error during message processing:" + e.getMessage(), e);
		}
		
	}
}
