/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.ejb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.InvalidXmlException;
import org.springframework.ws.transport.jms.WebServiceMessageListener;

import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsHeaders;
import eu.europa.ec.cipa.etrustex.services.IAuthenticationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.AuthenticationFailedException;
import eu.europa.ec.cipa.etrustex.services.exception.EncodedBusinessException;

/**
 * @author chiricr
 *
 */
public class DocumentQueueMessageListener extends WebServiceMessageListener {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	private Logger logger = LoggerFactory.getLogger(DocumentQueueMessageListener.class);
	
	@Autowired
	private ILogService logService;
		
	@Autowired
	private IAuthenticationService authenticationService;
	
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	
	private static ThreadLocal<JmsHeaders> jmsHeadersThreadLocal;
		
	@Override
	public void onMessage(Message message, Session session) {
		
		try {
			//ETRUSTEX-1086 Log correlation id identical for multiple processings of JMS messages in documentQueue
			logService.reinitializeCorrelationId();
			//store JMS headers in a ThreadLocal
			JmsHeaders jmsHeaders = new JmsHeaders(message.getJMSDestination(), 
					message.getJMSReplyTo(), message.getJMSCorrelationID());
			jmsHeaders.setPriority(message.getJMSPriority());
			jmsHeadersThreadLocal = new ThreadLocal<JmsHeaders>();
			jmsHeadersThreadLocal.set(jmsHeaders);
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
				message.setJMSReplyTo(jmsHeadersThreadLocal.get().getReplyTo());
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
		if (jmsHeadersThreadLocal.get() != null && jmsHeadersThreadLocal.get().getReplyTo() != null) {
			jmsHeadersThreadLocal.get().setResponseCode(getResponseCode(e));
			jmsSenderService.sendMessage(null, jmsHeadersThreadLocal.get());
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
	
	public static JmsHeaders getJmsHeaders() {
		return jmsHeadersThreadLocal != null ? jmsHeadersThreadLocal.get() : null;
	}
	
	public static void removeJmsHeaders() {
		if (jmsHeadersThreadLocal != null) {
			jmsHeadersThreadLocal.remove();
		}
	}
}
