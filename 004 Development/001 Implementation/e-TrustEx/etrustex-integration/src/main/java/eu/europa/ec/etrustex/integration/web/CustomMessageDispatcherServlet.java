package eu.europa.ec.etrustex.integration.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.soap.SoapMessageCreationException;
import org.springframework.ws.soap.saaj.SaajSoapBodyException;
import org.springframework.ws.soap.saaj.SaajSoapEnvelopeException;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import eu.europa.ec.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.etrustex.integration.util.SOAPService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;


public class CustomMessageDispatcherServlet extends MessageDispatcherServlet {

	private static final long serialVersionUID = -6162021294030508377L;
	
	private SOAPService soapService;
	
	public CustomMessageDispatcherServlet() {
		super();
	}

	public CustomMessageDispatcherServlet(
			WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
	   super.init(config);
	   ApplicationContext ac = (ApplicationContext) config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	   this.soapService = (SOAPService)ac.getBean("soapService");
	}	

	@Override
	protected void doService(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		
		try{		
			super.doService(httpServletRequest, httpServletResponse);
		} catch (SaajSoapBodyException | SoapMessageCreationException e){
			httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (SaajSoapEnvelopeException  e){
			if (e.getCause() instanceof SOAPException && e.getMessage().contains("InputStream does not represent a valid SOAP 1.1 Message")) {
				//not a SOAP 1.1 message 
				String soapFault = soapService.createSoapFault(ErrorResponseCode.SOAP_VERSION_MISMATCH);
				httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, soapFault);
			} else if (e.getCause() instanceof SOAPException && e.getMessage().contains("Unable to create envelope from given source")) {
				//SOAP envelope not being the root element
				httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (NullPointerException npe) {			
			if (npe.getStackTrace()[0].getClassName().startsWith("org.springframework.ws.soap.saaj")) {
				//for SOAP body not present in the envelope
				httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (BadRequestException bre) {
			//for SOAP header not first child of envelope or no SOAP body
			httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (RequestEntityTooLargeException retle) {
			//for using chunking on any transaction other than wrappers
			httpServletResponse.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		} catch (Exception e) {
			httpServletRequest.getInputStream().close();			
			httpServletResponse.getOutputStream().close();
			httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

}
