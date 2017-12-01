package eu.europa.ec.cipa.etrustex.integration.ejb;

import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.cipa.etrustex.integration.dispatcher.MessageDispatcher;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.integration.util.TrustExMessageConverter;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageRoutingService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

/**
 * 
 * @author chiricr
 *
 *
 *
 */
@Interceptors(CustomAutowiringInterceptor.class)
public class RoutingQueueMessageListener implements MessageListener  {
		
	@Autowired
	private IMessageRoutingService messageRoutingService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	@Autowired
	private IBusinessDomainService businessDomainService;
		
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;
	
	private Logger logger = LoggerFactory.getLogger(RoutingQueueMessageListener.class);
	
	
	@Override
	public void onMessage(Message message) {
		TrustExMessageConverter trustexMessageConverter = new TrustExMessageConverter();
		TrustExMessage<String> trustExMessage = null;
		try {
			trustExMessage = (TrustExMessage<String>)trustexMessageConverter.fromMessage(message);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Unable to read the incoming JMS message.", e);	
		}
			
		String messageRoutingId = trustExMessage.getHeader().getMessageRoutingId();		
		LogDTO logDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG, 
				"Inside RoutingQueueMessageListener messageRouting id = " + messageRoutingId, 
				this.getClass().getName()); 
		logService.saveLog(logDTO);
		
		
		//add the content of message binary as payload
		MessageRouting messageRouting = messageRoutingService.findById(Long.valueOf(messageRoutingId));
		String raw =  messageService.getMessageBinaryAsString(messageRouting.getMessage().getId(), MessageBinaryType.RAW_MESSAGE);
		trustExMessage.setPayload(raw);
		
		if (messageRouting.getMessage()!=null ){
			
			trustExMessage.getHeader().setMessageStatusCode(messageRouting.getMessage().getStatusCode());
			
			// retrieve parent information to populate Front Office headers (see class FrontOfficeMessagConverter)
			if( messageRouting.getMessage().getParentMessages()!=null 
					&& messageRouting.getMessage().getParentMessages().size()>0){
				eu.europa.ec.cipa.etrustex.domain.Message parent = messageRouting.getMessage().getParentMessages().iterator().next();
				
				trustExMessage.getHeader().setParentMessageId(parent.getId());
				trustExMessage.getHeader().setParentStatusCode(parent.getStatusCode());
				
			}
		}
		
		Endpoint endpoint = messageRouting.getEndpoint();			
		if (endpoint.getIsActive()){
			logger.debug("Found active endpoint of type" +endpoint.getClass());
			MessageDispatcher dispatcher = MessageDispatcher.getMessageDispatcher(endpoint);
			applicationContext.getAutowireCapableBeanFactory().autowireBean(dispatcher);
			try {
				Transaction transaction = authorisationService.getTransactionById(trustExMessage.getHeader().getTransactionTypeId());
				dispatcher.dispatchMessage(trustExMessage, transaction);
				messageRoutingService.markAsProcessed(Long.valueOf(messageRoutingId));
			} catch (MessageRoutingException e) {
				logger.error(e.getMessage(), e);
				logDTO.setDescription(logDTO.getDescription() + " Unable to dispatch message " + e.getMessage());
				logService.saveLog(logDTO);
				
				throw new RuntimeException("Unable to dispatch message.");
			}
			
		}

	}
	
}
