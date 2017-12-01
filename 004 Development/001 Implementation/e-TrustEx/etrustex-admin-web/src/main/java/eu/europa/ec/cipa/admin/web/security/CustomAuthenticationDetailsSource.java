package eu.europa.ec.cipa.admin.web.security;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;


public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {  
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired private MessageSource  messageSource;
	@Autowired private IUserService userService;
	@Autowired private IBusinessDomainService businessDomainService;
	

	@Override
	public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(HttpServletRequest request) {
		return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(request, new ArrayList<SimpleGrantedAuthority>());
	}
}
