package eu.europa.ec.etrustex.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableIntegration
@EnableTransactionManagement
public class ChannelsConfig {

	public static final String ASYNCH_SUBMIT_IN 	 		= "trustExASynchSubmitIN";

	public static final String MESSAGE_BINARY_GW_OUT 	 	= "messageBinaryGatewayOut";
	public static final String MESSAGE_BINARY_GW_STORE_IN  	= "messageBinaryGatewayStoreIn";
	public static final String MESSAGE_BINARY_GW_DELETE_IN 	= "messageBinaryGatewayDeleteIn";
	public static final String MESSAGE_PROCESSING_GW_OUT 	= "messageProcessingGatewayOut";
	public static final String SUBMIT_SOAP_IN 				= "trustExSubmitSoapIN";
	public static final String SUBMIT_SOAP_OUT 				= "trustExSubmitSoapOut";
	public static final String SUBMIT_TRANS_TO_SOAP_OUT 	= "trustExSubmitTransformToSoapOut";
	public static final String READ_SOAP_IN 				= "trustExReadSoapIN";
	public static final String READ_SOAP_OUT 				= "trustExReadSoapOut";
	public static final String READ_TRANS_TO_SOAP_OUT 		= "trustExReadTransformToSoapOut";
	public static final String SYNC_SUBMIT_RESPONSE_ROUTER = "synchSubmitResponseRouterChannel";
	public static final String SYNC_RESPONSE_ROUTER 		= "synchResponseRouterChannel";
	public static final String REDELIVERY_ROUTER 			= "redeliveryRouterChannel";
	public static final String SYNC_SUBMIT_IN 				= "trustSynchSubmitIN";
	public static final String READ_IN 						= "trustExReadIN";
	public static final String REDELIVERY_PARENT_NOT_FOUND 	= "trustExRedeliveryParentNotFound";
	public static final String REDELIVERY_ROUTING_FAILED 	= "trustExRedeliveryRoutingFailed";
	public static final String CONTROL_BUS 					= "controlBusChannel";
	public static final String DISPATCHER					= "dispatcherChannel";
	public static final String SAR_20_BGW_IN 				= "SubmitApplicationResponse-2.0BusinessGatewayIn";
	public static final String SAR_20_BGW_OUT 				= "SubmitApplicationResponse-2.0BusinessGatewayOut";
	public static final String MAIL_SENDER 					= "mailSenderChannel";
	public static final String LOGGER 						= "loggerChannel";
	public static final String ERROR_CHANNEL 				= "errorChannel";
	public static final String NULL							= "nullChannel";
	public static final String SUBMIT_DOC_REQUEST_CHANNEL	= "submitDocumentRequestChannel";
	public static final String SUBMIT_DOC_TRANSF_REPLY_CHANNEL		= "submitDocumentTransformReplyChannel";

	@Autowired
	private WireTap wireTap;

	@Bean(ERROR_CHANNEL)
	public MessageChannel errorChannel() {
		return MessageChannels.direct()
				.interceptor(wireTap)
				.get();
	}

	/*
	 * ******************
	 * Message Processing GW
	 * ******************
	 */
	//The Gateway is annotated in eu.europa.ec.etrustex.integration.api.IMessageProcessingGateway
	@Bean(MESSAGE_PROCESSING_GW_OUT)
	public MessageChannel messageProcessingGatewayOut() {
		return new DirectChannel();
	}

	/*
	 * ******************
	 * Message Binary GW
	 * ******************
	 */
	//The Gateway is annotated in eu.europa.ec.etrustex.integration.api.IMessageBinaryGateway

	@Bean(MESSAGE_BINARY_GW_OUT)
	public MessageChannel messageBinaryGatewayOut() {
		return new DirectChannel();
	}

	@Transactional
	@Bean(MESSAGE_BINARY_GW_STORE_IN)
	public MessageChannel messageBinaryGatewayStoreIn() {
		return new DirectChannel();
	}

	@Transactional
	@Bean(MESSAGE_BINARY_GW_DELETE_IN)
	public MessageChannel messageBinaryGatewayDeleteIn() {
		return new DirectChannel();
	}

	/*
	 * *************************
	 * Endpoint Message Channels
	 * **************************
	 */

	@Bean(MESSAGE_BINARY_GW_STORE_IN)
	public MessageChannel appTypeUpgradeInput() {
		return new DirectChannel();
	}

	@Bean(SUBMIT_SOAP_IN)
	public MessageChannel trustExSubmitSoapIN() {
		return new DirectChannel();
	}

	@Bean(SUBMIT_SOAP_OUT)
	public MessageChannel trustExSubmitSoapOut() {
		return new DirectChannel();
	}

	@Bean(SUBMIT_TRANS_TO_SOAP_OUT)
	public MessageChannel trustExSubmitTransformToSoapOut() {
		return new DirectChannel();
	}

	@Bean(READ_SOAP_IN)
	public MessageChannel trustExReadSoapIN() {
		return new DirectChannel();
	}

	@Bean(READ_SOAP_OUT)
	public MessageChannel trustExReadSoapOut() {
		return new DirectChannel();
	}

	@Bean(READ_TRANS_TO_SOAP_OUT)
	public MessageChannel trustExReadTransformToSoapOut() {
		return new DirectChannel();
	}

	@Bean(SUBMIT_DOC_REQUEST_CHANNEL)
	public MessageChannel submitDocumentRequestChannel() {
		return new DirectChannel();
	}

	@Bean
	public ThreadPoolTaskExecutor executor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(10);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

//	@Bean
//	public IntegrationFlow logFlow() {
//		LoggingHandler loggingHandler = new LoggingHandler(LoggingHandler.Level.DEBUG.name());
//		loggingHandler.setLoggerName("TEST_LOGGER");
//		return IntegrationFlows.from(SUBMIT_DOC_REPLY_CHANNEL)
//				.handle(loggingHandler)
//				.get();
//	}

	@Bean(SUBMIT_DOC_TRANSF_REPLY_CHANNEL)
	public MessageChannel submitDocumentTransformReplyChannel() {
		return new DirectChannel();
	}

	/*
	 * *************************
	 * Routing Message Channels
	 * *************************
	 */

	@Bean(SYNC_SUBMIT_RESPONSE_ROUTER)
	public MessageChannel synchSubmitResponseRouterChannel() {
		return new DirectChannel();
	}

	@Bean(SYNC_RESPONSE_ROUTER)
	public MessageChannel synchResponseRouterChannel() {
		return new DirectChannel();
	}

	@Bean(REDELIVERY_ROUTER)
	public MessageChannel redeliveryRouterChannel() {
		return new DirectChannel();
	}

	/*
	 * ***************************
	 * SyncSubmit Message Channels
	 * ***************************
	 */

	//@Transactional
	@Bean(SYNC_SUBMIT_IN)
	public MessageChannel trustExSynchSubmitIN() {
		return MessageChannels.direct()
				.interceptor(wireTap)
				.get();
	}

	/*
	 * ***************************
	 * SyncSubmit Message Channels
	 * ***************************
	 */
	//@Transactional
	@Bean(READ_IN)
	public MessageChannel trustExASynchReadIN() {
		return MessageChannels.direct()
				.interceptor(wireTap)
				.get();
	}

	/*
	 * ***************************
	 * Redelivery Message Channels
	 * ***************************
	 */

	@Bean(REDELIVERY_PARENT_NOT_FOUND)
	public MessageChannel trustExRedeliveryParentNotFound() {
		return new DirectChannel();
	}

	@Bean(REDELIVERY_ROUTING_FAILED)
	public MessageChannel trustExRedeliveryRoutingFailed() {
		return new DirectChannel();
	}

	/*
	 * ***************************
	 * Common Message Channels
	 * ***************************
	 */

	@Bean(CONTROL_BUS)
	public MessageChannel controlBusChannel() {
		return new DirectChannel();
	}

	@Bean(DISPATCHER)
	public MessageChannel dispatcherChannel() {
		return new DirectChannel();
	}

	@Bean(SAR_20_BGW_IN)
	public MessageChannel sarBusinessGatewayIn() {
		return new DirectChannel();
	}

	@Bean(SAR_20_BGW_OUT)
	public MessageChannel sarBusinessGatewayOut() {
		return new DirectChannel();
	}

	/*
	 * ***************************
	 * ASyncSubmit Message Channels
	 * ***************************
	 */

	@Bean(ASYNCH_SUBMIT_IN)
	public MessageChannel trustExASynchSubmitIN() {
		return MessageChannels.direct()
				.interceptor(wireTap)
				.get();
	}

	@Bean(MAIL_SENDER)
	public MessageChannel mailSenderChannel() {
		return MessageChannels.direct()
				.interceptor(wireTap)
				.get();
	}

}
