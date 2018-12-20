package eu.europa.ec.etrustex.integration.config.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.router.HeaderValueRouter;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;

@Configuration
@EnableIntegration
public class RoutersConfig {

	@Bean
	@Router(inputChannel="synchSubmitResponseRouterChannel")
	 public HeaderValueRouter syncSubmitRouter() {
	    HeaderValueRouter router = new HeaderValueRouter("replyTO");
		router.setChannelMapping("SOAP"			,ChannelsConfig.SUBMIT_TRANS_TO_SOAP_OUT);
		router.setChannelMapping("MSG_GATEWAY"	,ChannelsConfig.MESSAGE_PROCESSING_GW_OUT);
	    return router;
	}
	
	@Bean
	@Router(inputChannel="synchResponseRouterChannel")
	 public HeaderValueRouter syncReadRouter() {
	    HeaderValueRouter router = new HeaderValueRouter("replyTO");
		router.setChannelMapping("SOAP"			,ChannelsConfig.READ_TRANS_TO_SOAP_OUT);
		router.setChannelMapping("MSG_GATEWAY"	,ChannelsConfig.MESSAGE_PROCESSING_GW_OUT);
	    return router;
	}
	
	@Bean
	@Router(inputChannel="redeliveryRouterChannel")
	 public HeaderValueRouter router() {
	    HeaderValueRouter router = new HeaderValueRouter("MSG_REDELIVERY_REASON");
		router.setChannelMapping("PARENT_ID_NOT_FOUND" ,ChannelsConfig.REDELIVERY_PARENT_NOT_FOUND);
		router.setChannelMapping("ROUTING_FAILED"	   ,ChannelsConfig.REDELIVERY_ROUTING_FAILED);
	    return router;
	}
	

}
