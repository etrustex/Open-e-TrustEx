package eu.europa.ec.cipa.etrustex.integration.ejb;

import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.integration.util.TrustExMessageConverter;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;


/**
 * @author rubinfa
 * 
 * Entry point for all the asynchronous requests from the Proxy Services.
 */
//@MessageDriven(
//		name = "IntegrationQueueMessageListener", 
//		mappedName="queue/integrationQueue",
//		activationConfig = { 
//			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue" ), 
//			 @ActivationConfigProperty(propertyName="destination", propertyValue="queue/integrationQueue")})
//@MessageDriven(
//		activationConfig = { 
//			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue" ), 
//			 @ActivationConfigProperty(propertyName="destination", propertyValue="java:/queue/integrationQueue")})
// @TransactionManagement(TransactionManagementType.CONTAINER)
 @Interceptors(CustomAutowiringInterceptor.class)
public class IntegrationQueueMessageListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(IntegrationQueueMessageListener.class);
	
	@Autowired
	@Qualifier("trustExASynchSubmitIN")
	private MessageChannel asychProcessingChannel;
		
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IBusinessDomainService businessDomainService;
	
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