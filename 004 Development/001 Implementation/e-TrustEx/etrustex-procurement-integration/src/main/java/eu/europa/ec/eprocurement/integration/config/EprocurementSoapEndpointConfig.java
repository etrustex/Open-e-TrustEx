package eu.europa.ec.eprocurement.integration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.gateway.ETrustEXSoapInboundGateway;
import eu.europa.ec.etrustex.integration.transformers.SAAJSOAPToTrustExMessageTransformer;

@Configuration
@Import({ChannelsConfig.class, EProcChannelsConfig.class})
@EnableIntegration
public class EprocurementSoapEndpointConfig {
	
	@Autowired
	private MessageChannel trustExSubmitSupplierSoapIN;
	
	@Autowired
	private MessageChannel trustExSubmitCustomerSoapIN;
	
	@Autowired
	private MessageChannel trustExSupplierReadSoapIN;
	
	@Autowired
	private MessageChannel trustExCustomerReadSoapIN;
	
	@Autowired
	private MessageChannel trustExSubmitSoapOut;

	@Autowired
	private MessageChannel trustExReadSoapOut;
	
	@Autowired
	private MessageChannel trustSynchSubmitIN;
	
	@Autowired
	private MessageChannel trustExReadIN;
	
	/*
	 * Submit ENDPOINTS
	 */
	
	@Bean("etrustExSubmitSupplierSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExSubmitSupplierSoapEndpoint(){
		ETrustEXSoapInboundGateway gw = new ETrustEXSoapInboundGateway();
		gw.setRequestChannel(trustExSubmitSupplierSoapIN);
		gw.setReplyChannel(trustExSubmitSoapOut);
		gw.setReadService(Boolean.FALSE);
		return gw;
	}
	
	@Bean("etrustExSubmitCustomerSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExSubmitCustomerSoapEndpoint(){
		ETrustEXSoapInboundGateway gw = new ETrustEXSoapInboundGateway();
		gw.setRequestChannel(trustExSubmitCustomerSoapIN);
		gw.setReplyChannel(trustExSubmitSoapOut);
		gw.setReadService(Boolean.FALSE);
		return gw;
	}
	
	/*
	 * Read Endpoints
	 */
	
	@Bean("etrustExReadSupplierSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExReadSupplierSoapEndpoint(){
		ETrustEXSoapInboundGateway gw = new ETrustEXSoapInboundGateway();
		gw.setRequestChannel(trustExSupplierReadSoapIN);
		gw.setReplyChannel(trustExReadSoapOut);
		gw.setReadService(Boolean.TRUE);
		return gw;
	}
	
	@Bean("etrustExReadCustomerSoapEndpoint")
	public ETrustEXSoapInboundGateway etrustExReadCustomerSoapEndpoint(){
		ETrustEXSoapInboundGateway gw = new ETrustEXSoapInboundGateway();
		gw.setRequestChannel(trustExCustomerReadSoapIN);
		gw.setReplyChannel(trustExReadSoapOut);
		gw.setReadService(Boolean.TRUE);
		return gw;
	}
	
	
	/*
	 * PROCESSING CHAINS
	 */
	
	@Bean
	public IntegrationFlow submitSupplierFlow() {
		return IntegrationFlows.from(trustExSubmitSupplierSoapIN)
				.enrichHeaders(s -> s.header("replyTO", "SOAP"))                        
				.transform(supplierTransformer(), "transformIncomingMessage")
				.channel(trustSynchSubmitIN)
				.get();
	}
	
	@Bean
	public IntegrationFlow submitCustomerFlow() {
		return IntegrationFlows.from(trustExSubmitCustomerSoapIN)
				.enrichHeaders(s -> s.header("replyTO", "SOAP"))                        
				.transform(customerTransformer(), "transformIncomingMessage")
				.channel(trustSynchSubmitIN)
				.get();
	}
	
	@Bean
	public IntegrationFlow readSupplierFlow() {
		return IntegrationFlows.from(trustExSupplierReadSoapIN)
				.enrichHeaders(s -> s.header("replyTO", "SOAP"))                        
				.transform(supplierTransformer(), "transformIncomingMessage")
				.channel(trustExReadIN)
				.get();
	}
	
	@Bean
	public IntegrationFlow readCustomerFlow() {
		return IntegrationFlows.from(trustExCustomerReadSoapIN)
				.enrichHeaders(s -> s.header("replyTO", "SOAP"))                        
				.transform(customerTransformer(), "transformIncomingMessage")
				.channel(trustExReadIN)
				.get();
	}
	
	/*
	 *MESSAGE TRANSFORMERS 
	 * 
	 */
	
	@Bean("supplierTransformer")
	public SAAJSOAPToTrustExMessageTransformer supplierTransformer(){
		SAAJSOAPToTrustExMessageTransformer mt = new SAAJSOAPToTrustExMessageTransformer();
		mt.setSenderType(SAAJSOAPToTrustExMessageTransformer.SENDER_TYPE_SUPPLIER);
		return mt;
	}
	
	@Bean("customerTransformer")
	public SAAJSOAPToTrustExMessageTransformer customerTransformer(){
		SAAJSOAPToTrustExMessageTransformer mt = new SAAJSOAPToTrustExMessageTransformer();
		mt.setSenderType(SAAJSOAPToTrustExMessageTransformer.SENDER_TYPE_CUSTOMER);
		return mt;
	}
	
	
}
