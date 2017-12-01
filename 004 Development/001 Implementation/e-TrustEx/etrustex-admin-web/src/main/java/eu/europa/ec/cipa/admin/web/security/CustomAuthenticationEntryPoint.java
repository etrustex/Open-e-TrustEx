package eu.europa.ec.cipa.admin.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns a authentication error page.
     */
    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException,
            ServletException {
        log.debug("Pre-authenticated entry point called. Rejecting access");
//        SecurityContextHolder.getContext().setAuthentication(null);
//        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        HttpServletResponse httpResponse = response;
        httpResponse.sendRedirect(request.getContextPath() + "/authenticationError.do");
    }
}