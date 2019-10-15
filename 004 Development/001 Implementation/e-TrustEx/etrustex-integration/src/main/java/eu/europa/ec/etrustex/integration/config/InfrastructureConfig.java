package eu.europa.ec.etrustex.integration.config;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import eu.europa.ec.etrustex.integration.config.business.PlatformServicesConfig;
import eu.europa.ec.etrustex.integration.config.endpoint.SoapEndpointConfig;
import eu.europa.ec.etrustex.integration.config.processing.AsyncSubmitConfig;
import eu.europa.ec.etrustex.integration.config.processing.CommonConfig;
import eu.europa.ec.etrustex.integration.config.processing.RedeliveryConfig;
import eu.europa.ec.etrustex.integration.config.processing.SynchProcessingConfig;
import eu.europa.ec.etrustex.integration.config.processing.SynchSubmitProcessingConfig;
import eu.europa.ec.etrustex.integration.config.routers.RoutersConfig;

/**
 * Contains all the channels
 * @author almahfa
 *
 */
@Configuration
@ComponentScan(basePackages = {
		"eu.europa.ec.etrustex.integration.service",
		"eu.europa.ec.etrustex.integration.ack",
		"eu.europa.ec.etrustex.integration.gateway",
		"eu.europa.ec.etrustex.integration.task",
		"eu.europa.ec.etrustex.integration.util"})
@Import({ChannelsConfig.class,PlatformServicesConfig.class,RoutersConfig.class,SoapEndpointConfig.class,CommonConfig.class,AsyncSubmitConfig.class,RedeliveryConfig.class,SynchProcessingConfig.class,SynchSubmitProcessingConfig.class})
@EnableTransactionManagement
public class InfrastructureConfig {
	
	@Bean
	public AutowiredAnnotationBeanPostProcessor customAutowiring(){
		return new AutowiredAnnotationBeanPostProcessor();
	}
	
}
