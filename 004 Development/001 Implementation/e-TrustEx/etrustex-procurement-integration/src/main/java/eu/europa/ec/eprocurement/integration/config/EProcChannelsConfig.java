package eu.europa.ec.eprocurement.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class EProcChannelsConfig {
	
	public static final String SUBMIT_SUPPLIER_SOAP_IN 	 	= "trustExSubmitSupplierSoapIN";
	public static final String SUBMIT_CUSTOMER_SOAP_IN 	 	= "trustExSubmitCustomerSoapIN";
	public static final String READ_SUPPLIER_SOAP_IN 	 	= "trustExSupplierReadSoapIN";
	public static final String READ_CUSTOMER_SOAP_IN 	 	= "trustExCustomerReadSoapIN";
	
	@Bean("trustExSubmitSupplierSoapIN")
	public MessageChannel trustExSubmitSupplierSoapIN() {
		return new DirectChannel();
	}
	
	@Bean("trustExSubmitCustomerSoapIN")
	public MessageChannel trustExSubmitCustomerSoapIN() {
		return new DirectChannel();
	}
	
	@Bean("trustExSupplierReadSoapIN")
	public MessageChannel trustExSupplierReadSoapIN() {
		return new DirectChannel();
	}
	
	@Bean("trustExCustomerReadSoapIN")
	public MessageChannel trustExCustomerReadSoapIN() {
		return new DirectChannel();
	}

}
