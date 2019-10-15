package eu.europa.ec.etrustex.integration.ejb;

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
import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.etrustex.integration.util.TrustExMessageConverter;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ILogService;


/**
 * @author rubinfa
 * 
 * Entry point for all the asynchronous requests from the Proxy Services.
 */

@Interceptors(CustomAutowiringInterceptor.class)
public class IntegrationQueueMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(IntegrationQueueMessageListener.class);
	
	@Autowired
	@Qualifier(ChannelsConfig.ASYNCH_SUBMIT_IN)
	private MessageChannel asychProcessingChannel;
		
	@Autowired
	private ILogService logService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;
	
	/**
     * Default constructor.
     */
    public IntegrationQueueMessageListener() {
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    @SuppressWarnings("unchecked")
	public void onMessage(Message message) {
    				
		try {
			TrustExMessageConverter trustexMessageConverter = new TrustExMessageConverter();
			TrustExMessage<String> trustExMessage = (TrustExMessage<String>)trustexMessageConverter.fromMessage(message);
			MessageBuilder<TrustExMessage<String>> builder =  MessageBuilder.withPayload(trustExMessage);
			long startTime = System.currentTimeMillis();
			asychProcessingChannel.send(builder.build());
			long elapsed = System.currentTimeMillis() - startTime;
			LogDTO logDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG, 
					"Inside IntegrationQueueMessageListener", 
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
    public MessageChannel getAsychProcessingChannel() {
		return asychProcessingChannel;
	}

	public void setAsychProcessingChannel(MessageChannel asychProcessingChannel) {
		this.asychProcessingChannel = asychProcessingChannel;
	}
	
}