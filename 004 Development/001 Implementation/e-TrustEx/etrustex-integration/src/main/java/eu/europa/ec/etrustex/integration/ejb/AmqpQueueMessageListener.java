/**
 * 
 */
package eu.europa.ec.etrustex.integration.ejb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.qpid.amqp_1_0.jms.impl.DestinationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.InvalidXmlException;
import org.springframework.ws.transport.jms.WebServiceMessageListener;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.AuthenticationFailedException;
import eu.europa.ec.etrustex.dao.exception.EncodedBusinessException;
import eu.europa.ec.etrustex.integration.util.AmqpHeaders;
import eu.europa.ec.etrustex.integration.util.AmqpSenderService;
import eu.europa.ec.etrustex.services.IAuthenticationService;
import eu.europa.ec.etrustex.services.ILogService;

/**
 * @author chiricr
 *
 */
public class AmqpQueueMessageListener extends WebServiceMessageListener {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private Logger logger = LoggerFactory.getLogger(AmqpQueueMessageListener.class);
	
	private ILogService logService;
	private IAuthenticationService authenticationService;
	private AmqpSenderService amqpSenderService;
	
	private static ThreadLocal<AmqpHeaders> messageHeadersThreadLocal;
		
	@Override
	public void onMessage(Message message, Session session) {
		
		try {
			DestinationImpl destination = (DestinationImpl)message.getJMSReplyTo();			
			logService.reinitializeCorrelationId();
			//store JMS headers in a ThreadLocal
			AmqpHeaders messageHeaders = new AmqpHeaders(destination != null ? destination.getAddress() : null);
			messageHeaders.setPriority(message.getJMSPriority());
			messageHeadersThreadLocal = new ThreadLocal<AmqpHeaders>();
			messageHeadersThreadLocal.set(messageHeaders);
			//perform basic authentication
			String encodedUsernamePassword = message.getStringProperty(AUTHORIZATION_HEADER);
			authenticationService.authenticate(encodedUsernamePassword);
			//remove JMS reply to in order to avoid automatic reply done by Spring
			message.setJMSReplyTo(null);
			
			//send message to the SOAP endpoint
			super.onMessage(message, session);
			logger.info("Message " + message.getJMSMessageID() + " successfully processed.");
		} catch (Exception e) {
			logger.error("Error during message processing:" + e.getMessage(), e);
			try {
//				message.setJMSReplyTo(messageHeadersThreadLocal.get().getReplyTo());
				logErrorAndSendResponse(e, message, session);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error("Error while sending soap fault response : " + e1.getMessage(), e1);
			}
		}
		
	}
	
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void logErrorAndSendResponse(Exception e, Message message, Session session) throws Exception {
		
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG,
				this.getClass().getName())
			.description("Inside DocumentQueueMessageListener Message id: " + message.getJMSMessageID() + " Exception: " + e.getMessage())
			.build();
		logService.saveLog(logDTO);		
		if (messageHeadersThreadLocal.get() != null) {
			messageHeadersThreadLocal.get().setResponseCode(getResponseCode(e));
			amqpSenderService.sendMessage(null, messageHeadersThreadLocal.get());
		}
		
	}
	
	private String getResponseCode(Exception e) {
		String responseCode = "";
		if (e instanceof JMSException && ((JMSException)e).getLinkedException() instanceof InvalidXmlException) {
			//XML not well formed
			responseCode = "400";
		} else if (e instanceof AuthenticationFailedException || e instanceof IOException) {
			//failed authentication (AuthenticationFailedException) or token is invalid Base64-encoded string (IOException)
			responseCode = "401";
		} else if(e instanceof EncodedBusinessException) {
			EncodedBusinessException ebe = (EncodedBusinessException)e;
			responseCode = ebe.getResponseCode().getCode();
		} else if (e instanceof NoSuchAlgorithmException) {
			//exception in EncryptionService - this should never happen
			responseCode = "500";
		}
		return responseCode;
	}	
	
	public static AmqpHeaders getAmqpHeaders() {
		return messageHeadersThreadLocal != null ? messageHeadersThreadLocal.get() : null;
	}
	
	public static void removeAmqpHeaders() {
		if (messageHeadersThreadLocal != null) {
			messageHeadersThreadLocal.remove();
		}
	}	
	
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setAuthenticationService(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setAmqpSenderService(AmqpSenderService amqpSenderService) {
		this.amqpSenderService = amqpSenderService;
	}		
}
