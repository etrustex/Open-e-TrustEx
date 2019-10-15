package eu.europa.ec.etrustex.integration.config.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.service.AuthorisationServiceActivator;
import eu.europa.ec.etrustex.integration.service.BusinessServiceActivator;
import eu.europa.ec.etrustex.integration.service.DataExtractorServiceActivator;
import eu.europa.ec.etrustex.integration.service.GenerateAckServiceActivator;
import eu.europa.ec.etrustex.integration.service.HumanReadableServiceActivator;
import eu.europa.ec.etrustex.integration.service.LogServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageBinaryServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageDispatcherServiceActivator;
import eu.europa.ec.etrustex.integration.service.MessageIdValidatorServiceActivator;
import eu.europa.ec.etrustex.integration.service.ParentDocumentHandlerServiceActivator;
import eu.europa.ec.etrustex.integration.service.RetriveMessageServiceActivator;
import eu.europa.ec.etrustex.integration.service.SchematronServiceActivator;
import eu.europa.ec.etrustex.integration.service.StoreMessageServiceActivator;
import eu.europa.ec.etrustex.integration.service.SyncToAsyncServiceActivator;
import eu.europa.ec.etrustex.integration.service.XSDValidationServiceActivator;
import eu.europa.ec.etrustex.integration.transformers.SAAJSOAPToTrustExMessageTransformer;
import eu.europa.ec.etrustex.services.IHumanReadableTemplateService;
import eu.europa.ec.etrustex.services.IXmlService;

@Configuration
@EnableTransactionManagement
@EnableIntegration
public class CommonConfig {
	
	@Autowired 
	IXmlService xmlService;
	@Autowired
	IHumanReadableTemplateService humanReadableTemplateService;
	
	//Service Activators

	@Bean("authorisationServiceActivator")
	public AuthorisationServiceActivator authorisationServiceActivator() {
		return new AuthorisationServiceActivator();
	}

	@Bean("validationServiceActivator")
	public XSDValidationServiceActivator validationServiceActivator() {
		return new XSDValidationServiceActivator();
	}

	@Bean("storeMessageServiceActivator")
	public StoreMessageServiceActivator storeMessageServiceActivator() {
		return new StoreMessageServiceActivator();
	}

	@Bean("retriveMessageServiceActivator")
	public RetriveMessageServiceActivator retriveMessageServiceActivator() {
		return new RetriveMessageServiceActivator();
	}

	@Bean("schematronServiceActivator")
	public SchematronServiceActivator schematronServiceActivator() {
		return new SchematronServiceActivator();
	}

	@Bean("parentDocumentHandlerServiceActivator")
	public ParentDocumentHandlerServiceActivator parentDocumentHandlerServiceActivator() {
		return new ParentDocumentHandlerServiceActivator();
	}

	@Bean("dataExtractorServiceActivator")
	public DataExtractorServiceActivator dataExtractorServiceActivator() {
		return new DataExtractorServiceActivator();
	}

	@Bean("businessServiceActivator")
	public BusinessServiceActivator businessServiceActivator() {
		return new BusinessServiceActivator();
	}

	@Bean("dispatcherServiceActivator")
	public MessageDispatcherServiceActivator dispatcherServiceActivator() {
		return new MessageDispatcherServiceActivator();
	}

	@Bean("messageBinaryServiceActivator")
	public MessageBinaryServiceActivator messageBinaryServiceActivator() {
		return new MessageBinaryServiceActivator();
	}
	
	@Bean("generateAckServiceActivator")
	public GenerateAckServiceActivator generateAckServiceActivator() {
		return new GenerateAckServiceActivator();
	}
	
	@Bean("syncToAsyncServiceActivator")
	public SyncToAsyncServiceActivator syncToAsyncServiceActivator() {
		return new SyncToAsyncServiceActivator();
	}
	
	@Bean("logServiceActivatorReturnMessage")
	public LogServiceActivator logServiceActivatorReturnMessage() {
		LogServiceActivator sa = new LogServiceActivator();
		sa.setReturnMessage(true);
		return sa;
	}
	
	@Bean("logServiceActivatorReturnNull")
	public LogServiceActivator logServiceActivatorReturnNull() {
		LogServiceActivator sa = new LogServiceActivator();
		sa.setReturnMessage(false);
		return sa;
	}
	
	@Bean("messageIdValidatorSubmitServiceActivator")
	public MessageIdValidatorServiceActivator messageIdValidatorSubmitServiceActivator() {
		MessageIdValidatorServiceActivator sa = new MessageIdValidatorServiceActivator();
		sa.setReadService(false);
		return sa;
	}
	
	@Bean("messageIdValidatorReadServiceActivator")
	public MessageIdValidatorServiceActivator messageIdValidatorReadServiceActivator() {
		MessageIdValidatorServiceActivator sa = new MessageIdValidatorServiceActivator();
		sa.setReadService(true);
		return sa;
	}
	
	@Bean("humanReadableServiceActivator")
	public HumanReadableServiceActivator humanReadableServiceActivator() {
		HumanReadableServiceActivator sa = new HumanReadableServiceActivator();
		sa.setXmlService(xmlService);
		sa.setHumanReadableTemplateService(humanReadableTemplateService);
		return sa;
	}

	//Integration
	
	/*Added to use WireTap and LoggingHandler
	 * 
	 * create 2 methods : wireTap() & loggerChannel()
	 * added @ServiceActivator(inputChannel = "loggerChannel") to logger
	 */
	@Bean("wireTap")
    public WireTap wireTap() {
        return new WireTap(loggerChannel());
    }

	@Bean(ChannelsConfig.LOGGER)
    public MessageChannel loggerChannel() {
        return MessageChannels.direct().get();
    }
	
	@Bean("logger")
	@ServiceActivator(inputChannel = ChannelsConfig.LOGGER)
	public MessageHandler logger() {
	     LoggingHandler loggingHandler =  new LoggingHandler(LoggingHandler.Level.DEBUG.name());
	     loggingHandler.setShouldLogFullMessage(true);
	     return loggingHandler;
	}
	
	/**
	 * Custom Message Transformer
	 * @return
	 */
	@Bean("soapSubmitTransformer")
	public SAAJSOAPToTrustExMessageTransformer soapSubmitTransformer(){
		SAAJSOAPToTrustExMessageTransformer mt = new SAAJSOAPToTrustExMessageTransformer();
		mt.setEndpointName("etrustExSubmitSoapEndpoint");
		return mt;
	}
	
	@Bean("soapReadTransformer")
	public SAAJSOAPToTrustExMessageTransformer soapReadTransformer(){
		SAAJSOAPToTrustExMessageTransformer mt = new SAAJSOAPToTrustExMessageTransformer();
		mt.setEndpointName("etrustExReadSoapEndpoint");
		return mt;
	}

}
