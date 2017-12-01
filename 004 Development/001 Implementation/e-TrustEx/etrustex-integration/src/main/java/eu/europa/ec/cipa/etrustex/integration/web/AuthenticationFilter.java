package eu.europa.ec.cipa.etrustex.integration.web;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;

import eu.europa.ec.cipa.etrustex.services.IAuthenticationService;
import eu.europa.ec.cipa.etrustex.services.exception.AuthenticationFailedException;

public class AuthenticationFilter implements Filter {
	
	private IAuthenticationService authenticationService;
	public static final String AUTHENTICATED_USER_ATTRIBUTE_NAME = "username";
	private static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate";


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		if (httpRequest.getMethod().equals(HttpMethod.GET.name())) {
			//GET request, no authentication required
			filterChain.doFilter(request, response);
			return;
		}
		
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String authorization = httpRequest.getHeader("Authorization");
		if (StringUtils.isBlank(authorization)) {
			sendUnauthorizedResponse(httpResponse);
			return;
		}
		String encodedUsernamePassword = authorization.substring(6); //"Basic <Base64-encoded-string>"
		try {			
			String user = authenticationService.authenticate(encodedUsernamePassword);
			httpRequest.setAttribute(AUTHENTICATED_USER_ATTRIBUTE_NAME, user);
			filterChain.doFilter(request, response);
		} catch (AuthenticationFailedException e) {
			sendUnauthorizedResponse(httpResponse);
		} catch (NoSuchAlgorithmException e) {
			httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	
	private void sendUnauthorizedResponse(HttpServletResponse httpResponse) throws IOException {
		httpResponse.setHeader(WWW_AUTHENTICATE_HEADER, "Basic realm=\"myRealm\"");
		httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
		
	public void setAuthenticationService(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}



	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	@Override
	public void destroy() {}
	
	

}
