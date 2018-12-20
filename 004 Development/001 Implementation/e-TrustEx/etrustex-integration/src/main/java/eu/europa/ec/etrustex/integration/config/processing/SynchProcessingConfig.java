package eu.europa.ec.etrustex.integration.config.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.service.AuthorisationServiceActivator;
import eu.europa.ec.etrustex.integration.service.BusinessServiceActivator;
import eu.europa.ec.etrustex.integration.service.LogServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageBinaryServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageIdValidatorServiceActivator;
import eu.europa.ec.etrustex.integration.service.SchematronServiceActivator;
import eu.europa.ec.etrustex.integration.service.XSDValidationServiceActivator;

@Configuration
@EnableIntegration
@EnableTransactionManagement
public class SynchProcessingConfig {
	
	@Autowired
	private AuthorisationServiceActivator authorisationServiceActivator;
	@Autowired
	private MessageIdValidatorServiceActivator messageIdValidatorReadServiceActivator;
	@Autowired
	private XSDValidationServiceActivator validationServiceActivator;
	@Autowired
	SchematronServiceActivator schematronServiceActivator;
	@Autowired
	private BusinessServiceActivator businessServiceActivator;
	@Autowired
	private LogServiceActivator logServiceActivatorReturnMessage;
	@Autowired
	private MessageBinaryServiceActivator messageBinaryServiceActivator;
	
	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel=ChannelsConfig.READ_IN, outputChannel=ChannelsConfig.SYNC_RESPONSE_ROUTER)
	public Message asyncSubmit(Message message){
		Message m = authorisationServiceActivator.checkSynchAuthorisation(message);
		m = (m != null) ? validationServiceActivator.processSynchMessage(m) : null;
		m = (m != null) ? messageIdValidatorReadServiceActivator.validateMessageId(m) : null;
		m = (m != null) ? schematronServiceActivator.processSynchMessage(m) : null;
		m = (m != null) ? messageBinaryServiceActivator.storeMessageBinary(m) : null;
		m = (m != null) ? businessServiceActivator.processSynchMessage(m) : null;
		m = (m != null) ? logServiceActivatorReturnMessage.logSuccessfulProcessing(m) : null;
		return m;
	}

}
