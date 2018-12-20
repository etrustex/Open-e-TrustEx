package eu.europa.ec.etrustex.integration.config.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.service.BusinessServiceActivator;
import eu.europa.ec.etrustex.integration.service.DataExtractorServiceActivator;
import eu.europa.ec.etrustex.integration.service.LogServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageDispatcherServiceActivator;
import eu.europa.ec.etrustex.integration.service.ParentDocumentHandlerServiceActivator;
import eu.europa.ec.etrustex.integration.service.RetriveMessageServiceActivator;

@Configuration
@EnableIntegration
public class RedeliveryConfig {
	
	@Autowired
	private RetriveMessageServiceActivator retriveMessageServiceActivator;
	@Autowired
	ParentDocumentHandlerServiceActivator parentDocumentHandlerServiceActivator;
	@Autowired
	DataExtractorServiceActivator dataExtractorServiceActivator;
	@Autowired
	BusinessServiceActivator businessServiceActivator;
	@Autowired @Qualifier("dispatcherServiceActivator")
	MessageDispatcherServiceActivator dispatcherServiceActivator;
	@Autowired
	LogServiceActivator logServiceActivatorReturnMessage;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel=ChannelsConfig.REDELIVERY_PARENT_NOT_FOUND, outputChannel=ChannelsConfig.NULL)
	public Message redeliveryParentNotFound(Message message){
		Message m = retriveMessageServiceActivator.retrieveMessageContent(message);
		m = (m != null) ? retriveMessageServiceActivator.retrieveMessageContent(m) : null;
		m = (m != null) ? parentDocumentHandlerServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? dataExtractorServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? businessServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? dispatcherServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? logServiceActivatorReturnMessage.logSuccessfulProcessing(m) : null;
		return m;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel=ChannelsConfig.REDELIVERY_ROUTING_FAILED, outputChannel=ChannelsConfig.NULL)
	public Message redeliveryRoutingFailed(Message message){
		Message m = retriveMessageServiceActivator.retrieveMessageContent(message);
		m = (m != null) ? retriveMessageServiceActivator.retrieveMessageContent(m) : null;
		m = (m != null) ? businessServiceActivator.processASynchMessage(m) : null;
		return m;
	}
	
	
}
