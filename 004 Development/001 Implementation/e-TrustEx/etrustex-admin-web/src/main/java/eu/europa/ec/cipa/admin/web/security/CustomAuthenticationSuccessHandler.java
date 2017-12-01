package eu.europa.ec.cipa.admin.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {
	 protected final Log logger = LogFactory.getLog(this.getClass());

	    @Override
	    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
	      return request.getServletPath();
	    }

	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
	    	logger.debug("Redirecting to: " + request.getServletPath());
	    	request.getRequestDispatcher(request.getServletPath()).forward(request, response);
	    }
}
