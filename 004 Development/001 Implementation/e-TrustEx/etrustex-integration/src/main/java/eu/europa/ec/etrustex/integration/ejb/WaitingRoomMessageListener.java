package eu.europa.ec.etrustex.integration.ejb;

import java.util.HashMap;
import java.util.Map;

import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.etrustex.integration.util.TrustExMessageConverter;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.types.MessageHeaderType;

@Interceptors(CustomAutowiringInterceptor.class)
public class WaitingRoomMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(WaitingRoomMessageListener.class);
	
	@Autowired
	@Qualifier("redeliveryRouterChannel")
	private MessageChannel redeliveryProcessingChannel;
	
	@Autowired
	private IBusinessDomainService businessDomainService;	
		
	@Autowired
	private ILogService logService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;	
	
	public MessageChannel getRedeliveryProcessingChannel() {
		return redeliveryProcessingChannel;
	}

	public void setRedeliveryProcessingChannel(
			MessageChannel redeliveryProcessingChannel) {
		this.redeliveryProcessingChannel = redeliveryProcessingChannel;
	}

	@Override
	public void onMessage(Message message) {
		try {
			TrustExMessageConverter trustexMessageConverter = new TrustExMessageConverter();
			TrustExMessage<String> trustExMessage =(TrustExMessage<String>)trustexMessageConverter.fromMessage(message);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(MessageHeaderType.MSG_REDELIVERY_REASON.getCode(), trustExMessage.getHeader().getRedeliveryReason().name());
			MessageBuilder<TrustExMessage<String>> builder =  MessageBuilder.withPayload(trustExMessage).copyHeaders(headers);
			// for routing
			long startTime = System.currentTimeMillis();
			redeliveryProcessingChannel.send(builder.build());
			long elapsed = System.currentTimeMillis() - startTime;
			LogDTO logDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG, 
					"Inside WaitingRoomMessageListener", 
					this.getClass().getName()); 
			logDTO.setValue("Duration: " + elapsed + " ms");
			logService.saveLog(logDTO);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Unable to read the incoming JMS message.", e);			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Unable to read the incoming JMS message.", e);			
		}

	}
		
}
