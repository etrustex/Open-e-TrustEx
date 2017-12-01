package eu.europa.ec.cipa.admin.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class CustomPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
	 /**
     * Return the J2EE user name.
     */
    @Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpRequest) {
        Object principal = httpRequest.getUserPrincipal() == null ? null : httpRequest.getUserPrincipal().getName();
        if (logger.isDebugEnabled()) {
            logger.debug("PreAuthenticated J2EE principal: " + principal);
        }
        
        if(principal != null) {
        	logger.debug("PreAuthenticated J2EE principal class: " + principal.getClass());
        }
        	
        return principal;
    }

    /**
     * For J2EE container-based authentication there is no generic way to
     * retrieve the credentials, as such this method returns a fixed dummy
     * value.
     */
    @Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpRequest) {
        return "N/A";
    }
}
