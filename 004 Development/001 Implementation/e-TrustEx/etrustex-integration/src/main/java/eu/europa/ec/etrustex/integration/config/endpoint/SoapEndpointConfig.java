package eu.europa.ec.etrustex.integration.config.endpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Endpoint;

import eu.europa.ec.etrustex.integration.gateway.SubmitDocumentService;
import eu.europa.ec.etrustex.integration.util.CxfThreadPoolExecutor;
import eu.europa.ec.etrustex.integration.util.SubmitDocumentFaultOutInterceptor;
import eu.europa.ec.etrustex.integration.util.SubmitDocumentInterceptor;
import eu.europa.ec.etrustex.services.*;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.jms.JMSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.ExpressionControlBusFactoryBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.SoapEnvelopeLoggingInterceptor;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;

import com.sun.xml.messaging.saaj.soap.MessageFactoryImpl;

import eu.europa.ec.etrustex.integration.api.IMessageBinaryGateway;
import eu.europa.ec.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.gateway.ETrustEXSoapInboundGateway;
import eu.europa.ec.etrustex.integration.gateway.EtrustEXWrapperEndpoint;
import eu.europa.ec.etrustex.integration.security.xwss.XwsSecurityInterceptor;
import eu.europa.ec.etrustex.integration.service.MessageDispatcherServiceActivator;
import eu.europa.ec.etrustex.integration.transformers.SAAJSOAPToTrustExMessageTransformer;
import eu.europa.ec.etrustex.integration.util.ETrustExEndpointExceptionResolver;

@Configurable(value="soapConfig")
@IntegrationComponentScan("eu.europa.ec.etrustex.integration.api")
@EnableIntegration
@PropertySources({
		@PropertySource("classpath:/etrustex-integration.properties")
})
public class SoapEndpointConfig {

	private static final Logger logger = LoggerFactory.getLogger(SoapEndpointConfig.class);

	@Autowired
	private IXmlService xmlService;
	@Autowired
	private IAuthorisationService authorisationService;
	@Autowired
	private IAuthenticationService authenticationService;
	@Autowired
	private IMetadataService metadataService;
	@Autowired
	private ILogService logService;
	@Autowired
	private IMessageProcessingGateway messageProcessingGateway;
	@Autowired
	private IMessageBinaryGateway messageBinaryGateway;
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired @Qualifier(ChannelsConfig.SYNC_SUBMIT_IN)
	private MessageChannel trustSynchSubmitIN;
	@Autowired @Qualifier(ChannelsConfig.READ_IN)
	private MessageChannel trustExReadIN;
	@Autowired @Qualifier(ChannelsConfig.SUBMIT_SOAP_IN)
	private MessageChannel trustExSubmitSoapIN;
	@Autowired @Qualifier(ChannelsConfig.SUBMIT_SOAP_OUT)
	private MessageChannel trustExSubmitSoapOut;
	@Autowired @Qualifier(ChannelsConfig.READ_SOAP_IN)
	private MessageChannel trustExReadSoapIN;
	@Autowired @Qualifier(ChannelsConfig.READ_SOAP_OUT)
	private MessageChannel trustExReadSoapOut;
	@Autowired @Qualifier(ChannelsConfig.SUBMIT_DOC_REQUEST_CHANNEL)
	private MessageChannel submitDocumentRequestChannel;

	@Autowired
	private KeyStoreCallbackHandler keyStoreHandler;
	@Autowired @Qualifier("soapSubmitTransformer")
	private SAAJSOAPToTrustExMessageTransformer soapSubmitTransformer;
	@Autowired @Qualifier("soapReadTransformer")
	private SAAJSOAPToTrustExMessageTransformer soapReadTransformer;

	@Autowired
	private MessageDispatcherServiceActivator dispatcherServiceActivator;

	@Autowired @Qualifier("dispatcherChannel")
	private MessageChannel dispatcherChannel;

	@Autowired
	private Environment prop;

	public static final String REPLY_TO_HEADER_SOAP = "SOAP";
	public static final String REPLY_TO_HEADER_GATEWAY = "MSG_GATEWAY";
	public static final String REPLY_TO_HEADER_SUBMIT_DOC = "SUBMIT_DOC";
	/*
	 * *****************
	 * Message Factories
	 * *****************
	 */

	/**
	 * Sun Message Factory
	 * @return
	 */
	@Bean("sunMessageFactory")
	public MessageFactory sunMessageFactory(){
		MessageFactory mf = null;
		try {
			mf = MessageFactoryImpl.newInstance();
		} catch (SOAPException e) {
			logger.error("Sun message Factory creation",e);
		}
		((MessageFactoryImpl)mf).setLazyAttachmentOptimization(true);
		return mf;
	}

	/**
	 * Saaj Message Factory wrapping Sun one
	 * @return
	 */
	@Bean("messageFactory")
	public SaajSoapMessageFactory messageFactory(){
		SaajSoapMessageFactory mf = new SaajSoapMessageFactory();
		mf.setMessageFactory(sunMessageFactory());
		return mf;
	}

	/**
	 * Message Receiver Adapter
	 * @return
	 */
	@Bean("wsMessageReceiverHandlerAdapter")
	public WebServiceMessageReceiverHandlerAdapter wsMessageReceiverHandlerAdapter(){
		WebServiceMessageReceiverHandlerAdapter ha = new WebServiceMessageReceiverHandlerAdapter();
		ha.setMessageFactory(messageFactory());
		return ha;
	}


	/*
	 * ******************
	 * Interceptors
	 * ******************
	 */
	/**
	 * Logging Interceptor
	 * @return
	 */
	@Bean("httpLoggingInterceptor")
	public SoapEnvelopeLoggingInterceptor httpLoggingInterceptor(){
		SoapEnvelopeLoggingInterceptor li = new SoapEnvelopeLoggingInterceptor();
		li.setLogRequest(true);
		li.setLogResponse(true);
		li.setLogFault(true);
		li.setLoggerName("httplog");
		return li;
	}

	/**
	 * Security Interceptor
	 * @return
	 */
	@Bean("wsSecurityInterceptor")
	public XwsSecurityInterceptor wsSecurityInterceptor(){
		XwsSecurityInterceptor xsi = new XwsSecurityInterceptor();
		xsi.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
		xsi.setAuthorisationService(authorisationService);
		xsi.setExceptionResolver(resolver());
		xsi.setCallbackHandler(keyStoreHandler);
		return xsi;
	}

	/*
	 * ******************
	 * Resolvers
	 * ******************
	 */
	/**
	 * Endpoint Resover
	 * @return
	 */
	@Bean("resolver")
	public ETrustExEndpointExceptionResolver resolver(){
		ETrustExEndpointExceptionResolver res = new ETrustExEndpointExceptionResolver();
		return res;
	}

	/**
	 * Soap fault resolver
	 * @return
	 */
	@Bean("exceptionResolver")
	public SoapFaultMappingExceptionResolver exceptionResolver(){
		SoapFaultMappingExceptionResolver res = new SoapFaultMappingExceptionResolver();
		SoapFaultDefinition sf = new SoapFaultDefinition();
		sf.setFaultCode(SoapFaultDefinition.SERVER);
		res.setDefaultFault(sf);
		Properties config = new Properties();
		config.setProperty("com.sun.xml.messaging.saaj.SOAPExceptionImpl", "CLIENT,Request xml was not well formed");
		res.setExceptionMappings(config);
		return res;
	}

	/*
	 * ******************
	 * WS Template
	 * ******************
	 */
	/**
	 * Spring Template for outgoing Webservices
	 * @return
	 */
	@Bean("outgoingWebServiceTemplate")
	public WebServiceTemplate outgoingWebServiceTemplate(){
		WebServiceTemplate wst = new WebServiceTemplate(messageFactory());
		wst.setInterceptors(new ClientInterceptor[]{wsSecurityInterceptor()});
		return wst;
	}

	//Integration

	/*
	 * ******************
	 * Message ENDPOINTS
	 * ******************
	 */

	/**
	 * Main Submit MessageEndpoint
	 * @return
	 */
	@Bean("etrustExSubmitSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExSubmitSoapEndpoint(){
		ETrustEXSoapInboundGateway ig = new ETrustEXSoapInboundGateway();
		ig.setRequestChannel(trustExSubmitSoapIN);
		ig.setReplyChannel(trustExSubmitSoapOut);
		ig.setReadService(false);
		return ig;
	}

	/**
	 * Read MessageEndpoint
	 * @return
	 */
	@Bean("etrustExReadSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExReadSoapEndpoint(){
		ETrustEXSoapInboundGateway ig = new ETrustEXSoapInboundGateway();
		ig.setRequestChannel(trustExReadSoapIN);
		ig.setReplyChannel(trustExReadSoapOut);
		ig.setReadService(true);
		return ig;
	}


	/**
	 * DocumentWrapper MessageEndpoint
	 * @return
	 */
	@Bean("etrustExWrapperSoapEndpoint")
	public EtrustEXWrapperEndpoint etrustExWrapperSoapEndpoint(){
		EtrustEXWrapperEndpoint wrp = new EtrustEXWrapperEndpoint();
		wrp.setMessageProcessingGateway(messageProcessingGateway);
		wrp.setMessageBinaryGateway(messageBinaryGateway);
		wrp.setAuthorisationService(authorisationService);
		wrp.setMetadataService(metadataService);
		wrp.setXmlService(xmlService);
		return wrp;
	}

	@Bean("submitDocumentService")
	public SubmitDocumentService submitDocumentService() {
		SubmitDocumentService submitDocumentService = new SubmitDocumentService();
		submitDocumentService.setAuthenticationService(authenticationService);
		submitDocumentService.setLogService(logService);
		submitDocumentService.setRequestChannel(submitDocumentRequestChannel);
		submitDocumentService.setAutoStartup(true);
		autowireCapableBeanFactory.autowireBean(submitDocumentService);
		return submitDocumentService;
	}

	@Bean("submitDocumentEndpoint")
	public Endpoint submitDocumentEndpoint() {
		EndpointImpl endpoint = new EndpointImpl(submitDocumentService());
		endpoint.getFeatures().add(loggingFeature());
		Map<String, Object> busProperties = endpoint.getBus().getProperties();
		if (busProperties == null) {
			busProperties = new HashMap<>();
		}
		busProperties.put(JMSFactory.JMS_DESTINATION_EXECUTOR, CxfThreadPoolExecutor.getInstance(prop));
		String address =
				"jms:jndi:" + prop.getProperty("documentqueue.name") +
						"?jndiInitialContextFactory=" + prop.getProperty(Context.INITIAL_CONTEXT_FACTORY) +
						"&jndiURL=" + prop.getProperty(Context.PROVIDER_URL) +
						"&jndiConnectionFactoryName=" + prop.getProperty("jms.connection.factory.name") +
						"&username=" + prop.getProperty(Context.SECURITY_PRINCIPAL) +
						"&password=" + prop.getProperty(Context.SECURITY_CREDENTIALS) +
						"&jndiTransactionManager=" + prop.getProperty("transactionmanager.name") +
						"&maxConcurrentConsumers=6";
		endpoint.setAddress(address);
		endpoint.getInInterceptors().add(new SubmitDocumentInterceptor());
		endpoint.getInInterceptors().add(new SAAJInInterceptor());
		endpoint.getOutFaultInterceptors().add(new SubmitDocumentFaultOutInterceptor());
		endpoint.publish();
		return endpoint;
	}


	@Bean
	public LoggingFeature loggingFeature() {
		LoggingFeature loggingFeature = new LoggingFeature();
		loggingFeature.setPrettyLogging(true);
		return loggingFeature;
	}


	@Bean
	public IntegrationFlow submitFlow() {
		return IntegrationFlows.from(trustExSubmitSoapIN)
				.enrichHeaders(s -> s.header("replyTO", SoapEndpointConfig.REPLY_TO_HEADER_SOAP))
				.transform(soapSubmitTransformer, "transformIncomingMessage")
				.channel(trustSynchSubmitIN)
				.get();
	}

	@Bean
	public IntegrationFlow readFlow() {
		return IntegrationFlows.from(trustExReadSoapIN)
				.enrichHeaders(s -> s.header("replyTO", SoapEndpointConfig.REPLY_TO_HEADER_SOAP))
				.transform(soapReadTransformer, "transformIncomingMessage").channel(trustExReadIN)
				.get();
	}

	@Bean
	public IntegrationFlow submitDocumentFlow() {
		return IntegrationFlows.from(submitDocumentRequestChannel)
				.enrichHeaders(s -> s.header("replyTO", SoapEndpointConfig.REPLY_TO_HEADER_SUBMIT_DOC))
				.transform(soapSubmitTransformer, "transformIncomingMessage")
				.channel(trustSynchSubmitIN)
				.get();
	}

	/*
	 * ******************
	 * Message TRANSFORMER
	 * ******************
	 */



	/**
	 * Transforms outgoing Submit Messages
	 * @param outgoing
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transformer(inputChannel=ChannelsConfig.SUBMIT_TRANS_TO_SOAP_OUT, outputChannel=ChannelsConfig.SUBMIT_SOAP_OUT)
	public Message transformOutgoingSubmitMessage(Message outgoing){
		return soapSubmitTransformer.transformOutgoingMessage(outgoing);
	}

	/**
	 * Transforms outgoing Submit Messages
	 * @param outgoing
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transformer(inputChannel=ChannelsConfig.SUBMIT_DOC_TRANSF_REPLY_CHANNEL)
	public Message transformSubmitDocumentResponse(Message outgoing){
		return soapSubmitTransformer.transformSubmitDocumentResponse(outgoing);
	}

	/**
	 * Transforms outgoing Read Messages
	 * @param outgoing
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transformer(inputChannel=ChannelsConfig.READ_TRANS_TO_SOAP_OUT, outputChannel=ChannelsConfig.READ_SOAP_OUT)
	public Message transformOutgoingReadMessage(Message outgoing){
		return soapReadTransformer.transformOutgoingMessage(outgoing);
	}



	@ServiceActivator(inputChannel = ChannelsConfig.CONTROL_BUS)
	public ExpressionControlBusFactoryBean controlBus() {
		return new ExpressionControlBusFactoryBean();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel=ChannelsConfig.DISPATCHER, outputChannel=ChannelsConfig.NULL)
	public Message dispatcherServiceActivatorC(Message message){
		return dispatcherServiceActivator.processASynchMessage(message);
	}


}
