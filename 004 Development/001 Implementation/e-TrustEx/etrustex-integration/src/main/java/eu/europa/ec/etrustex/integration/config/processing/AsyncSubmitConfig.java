package eu.europa.ec.etrustex.integration.config.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.service.BusinessServiceActivator;
import eu.europa.ec.etrustex.integration.service.DataExtractorServiceActivator;
import eu.europa.ec.etrustex.integration.service.HumanReadableServiceActivator;
import eu.europa.ec.etrustex.integration.service.LogServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageDispatcherServiceActivator;
import eu.europa.ec.etrustex.integration.service.ParentDocumentHandlerServiceActivator;
import eu.europa.ec.etrustex.integration.service.RetriveMessageServiceActivator;
import eu.europa.ec.etrustex.integration.service.SchematronServiceActivator;
import eu.europa.ec.etrustex.integration.service.XSDValidationServiceActivator;
import eu.europa.ec.etrustex.integration.transformers.MessageToMailTransformer;

@Configuration
@EnableIntegration
@EnableTransactionManagement
public class AsyncSubmitConfig {
	
	@Autowired
	private JmsTemplate toErrorQueueTemplate;
	@Autowired
	private RetriveMessageServiceActivator retriveMessageServiceActivator;
	@Autowired
	private XSDValidationServiceActivator validationServiceActivator;
	@Autowired
	private SchematronServiceActivator schematronServiceActivator;
	@Autowired
	private ParentDocumentHandlerServiceActivator parentDocumentHandlerServiceActivator;
	@Autowired
	private DataExtractorServiceActivator dataExtractorServiceActivator;
	@Autowired
	private BusinessServiceActivator businessServiceActivator;
	@Autowired
	private MessageDispatcherServiceActivator dispatcherServiceActivator;
	@Autowired
	private LogServiceActivator logServiceActivatorReturnNull;
	@Autowired
	private HumanReadableServiceActivator humanReadableServiceActivator;
	@Qualifier(ChannelsConfig.ERROR_CHANNEL)
	@Autowired MessageChannel errorChannel;
	
	@Bean("mailTransformer")
	public MessageToMailTransformer mailTransformer(){
		return new MessageToMailTransformer();
	}
	
	@Bean("jmsErrorOut")
	@ServiceActivator(inputChannel = ChannelsConfig.ERROR_CHANNEL)
	public MessageHandler jmsErrorOut() {
	   JmsSendingMessageHandler handler = new JmsSendingMessageHandler(toErrorQueueTemplate);
	   return handler;
	}
	
	@Transactional
	@SuppressWarnings("rawtypes")
	@ServiceActivator(inputChannel=ChannelsConfig.ASYNCH_SUBMIT_IN)
	public Message asyncSubmit(Message message) throws BusinessException, TechnicalErrorException {
		@SuppressWarnings("unchecked")
		Message<TrustExMessage<String>> m = retriveMessageServiceActivator.retrieveMessageContent(message);
		m = (m != null) ? validationServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? schematronServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? parentDocumentHandlerServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? dataExtractorServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? businessServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? humanReadableServiceActivator.processMessage(m) : null;
		m = (m != null) ? dispatcherServiceActivator.processASynchMessage(m) : null;
		m = (m != null) ? logServiceActivatorReturnNull.logSuccessfulProcessing(m) : null;
		return m;
	}

}
