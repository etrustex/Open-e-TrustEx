package eu.europa.ec.etrustex.integration.config.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.service.AuthorisationServiceActivator;
import eu.europa.ec.etrustex.integration.service.GenerateAckServiceActivator;
import eu.europa.ec.etrustex.integration.service.LogServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageIdValidatorServiceActivator;
import eu.europa.ec.etrustex.integration.service.StoreMessageServiceActivator;
import eu.europa.ec.etrustex.integration.service.SyncToAsyncServiceActivator;
import eu.europa.ec.etrustex.integration.service.XSDValidationServiceActivator;

@Configuration
@EnableIntegration
@EnableTransactionManagement
public class SynchSubmitProcessingConfig {

	@Autowired
	private AuthorisationServiceActivator authorisationServiceActivator;
	@Autowired
	private MessageIdValidatorServiceActivator messageIdValidatorSubmitServiceActivator;
	@Autowired
	private XSDValidationServiceActivator validationServiceActivator;
	@Autowired
	private StoreMessageServiceActivator storeMessageServiceActivator;
	@Autowired
	private GenerateAckServiceActivator generateAckServiceActivator;
	@Autowired
	private LogServiceActivator logServiceActivatorReturnMessage;
	@Autowired
	private SyncToAsyncServiceActivator syncToAsyncServiceActivator;

	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel=ChannelsConfig.SYNC_SUBMIT_IN, outputChannel=ChannelsConfig.SYNC_SUBMIT_RESPONSE_ROUTER)
	public Message asyncSubmit(Message message) throws TechnicalErrorException{
		Message m = authorisationServiceActivator.checkSubmitAuthorisation(message);
		m = (m != null) ? messageIdValidatorSubmitServiceActivator.validateMessageId(m) : null;
		m = (m != null) ? validationServiceActivator.processSynchMessage(m) : null;
		m = (m != null) ? storeMessageServiceActivator.storeSynchMessage(m) : null;
		m = (m != null) ? generateAckServiceActivator.processMessage(m) : null;
		m = (m != null) ? logServiceActivatorReturnMessage.logSuccessfulProcessing(m) : null;
		m = (m != null) ? syncToAsyncServiceActivator.dispatchToIntegrationQueue(m) : null;
		return m;
	}

}
