package eu.europa.ec.cipa.etrustex.integration.web;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;

/**
 * 
 */

/**
 * @author chiricr
 *
 */
public class LogFilter implements Filter {
	
	private ILogService logService;
	
	private IAuthorisationService authorisationService;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		HttpServletRequest httpRequest = (HttpServletRequest)request;
				
		if (httpRequest.getMethod().equalsIgnoreCase("GET")) {
			filterChain.doFilter(request, response);
			return;
		}
		

		Party issuer = null;
		String username = StringUtils.isNotBlank(httpRequest.getRemoteUser()) 
				? httpRequest.getRemoteUser() : (String)httpRequest.getAttribute(AuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE_NAME); 
		try {
			issuer = authorisationService.getMessageIssuer(username);
		} catch (UndefinedIdentifierException e) {
		}
		BusinessDomain businessDomain = issuer != null ? issuer.getBusinessDomain() : null;
		
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.CONNECTION, this.getClass().getName())
			.description("Inside LogFilter")
			.businessDomain(businessDomain)
			.urlContext(httpRequest.getRequestURI())
			.authIpAddress(request.getRemoteAddr())
			.value("Authenticated IP address: " + request.getRemoteAddr())
			.reinitializeCorrelationId(true)
			.build();
		logService.saveLog(logDTO);
		
		filterChain.doFilter(request, response);
				
		long elapsed = System.currentTimeMillis() - startTime;
		logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.CONNECTION, this.getClass().getName())
			.description("Inside LogFilter")
			.businessDomain(businessDomain)
			.urlContext(httpRequest.getRequestURI())
			.authIpAddress(request.getRemoteAddr())
			.value("Duration: " + elapsed + " ms")
			.build();
		logService.saveLog(logDTO);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setAuthorisationService(IAuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}
	
}
