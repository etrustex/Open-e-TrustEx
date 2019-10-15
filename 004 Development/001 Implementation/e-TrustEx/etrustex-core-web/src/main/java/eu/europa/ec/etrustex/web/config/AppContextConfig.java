package eu.europa.ec.etrustex.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.europa.ec.etrustex.integration.web.AuthenticationFilter;
import eu.europa.ec.etrustex.integration.web.LogFilter;
import eu.europa.ec.etrustex.services.IAuthenticationService;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.ILogService;

@Configuration
public class AppContextConfig {
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	@Autowired
	private IAuthenticationService authenticationService;


	@Bean(name="logFilter")
	public LogFilter getLogFilter(){
		LogFilter logFilter = new LogFilter();
		logFilter.setLogService(logService);
		logFilter.setAuthorisationService(authorisationService);
		return logFilter;
	}
	
	@Bean(name="authenticationFilter")
	public AuthenticationFilter getAuthenticationFilter(){
		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
		authenticationFilter.setAuthenticationService(authenticationService);
		return authenticationFilter;
	}
	
	
	
}
