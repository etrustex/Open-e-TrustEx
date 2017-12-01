package eu.europa.ec.cipa.admin.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {  
	private final Log logger = LogFactory.getLog(this.getClass());
	 private String accessDeniedUrl;  
	   
	 public CustomAccessDeniedHandler() {  
	 }  
	   
	 public CustomAccessDeniedHandler(String accessDeniedUrl) {  
	  this.accessDeniedUrl = accessDeniedUrl;  
	 }  
	   
	 @Override  
	 public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) 
			 throws IOException, ServletException {  
		 logger.error(accessDeniedException);
//		 SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//		 request.getSession().invalidate();
//		 request.getSession().setAttribute("message", " Sorry  You don't have privileges to view this page!!!");  //TODO: batrian: hard coded message
		 response.sendRedirect(accessDeniedUrl);  
	 }  
	   
	 public String getAccessDeniedUrl() {  
	  return accessDeniedUrl;  
	 }  
	   
	 public void setAccessDeniedUrl(String accessDeniedUrl) {  
	  this.accessDeniedUrl = accessDeniedUrl;  
	 }  
	}  