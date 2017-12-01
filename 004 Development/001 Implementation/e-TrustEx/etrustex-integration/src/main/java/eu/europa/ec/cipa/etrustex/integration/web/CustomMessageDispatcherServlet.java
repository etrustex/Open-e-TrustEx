package eu.europa.ec.cipa.etrustex.integration.web;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.soap.saaj.SaajSoapEnvelopeException;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import eu.europa.ec.cipa.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.cipa.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.cipa.etrustex.integration.util.SOAPService;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;


public class CustomMessageDispatcherServlet extends MessageDispatcherServlet {

	private static final long serialVersionUID = -6162021294030508377L;
	private static final Logger logger = LoggerFactory.getLogger(CustomMessageDispatcherServlet.class);
	
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
		} catch (SaajSoapEnvelopeException e){
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

	private static class ServletInputStreamWrapper extends ServletInputStream {

		private ServletInputStream target;
		private long max = 0;
		private long count = 0;

		public ServletInputStreamWrapper(ServletInputStream target, long max) {
			this.target = target;
			this.max = max;
		}

		@Override
		public int readLine(byte[] b, int off, int len) throws IOException {
			int val =target.readLine(b, off, len);
			if (val != -1)
				count++;
			checkMax();
			return val;
		}
		
		@Override
		public int read() throws IOException {
			int val = target.read();
			if (val != -1)
				count++;
			checkMax();
			return val;
		}

		private void checkMax() throws IOException {
			if (count > max){
				try{
					target.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				throw new IOException("Maximum reached");
			}

		}

		@Override
		public int read(byte b[]) throws IOException {
			int val = target.read(b);
			if (val != -1)
				count += val;
			checkMax();
			return val;
		}

		@Override
		public int read(byte b[], int off, int len) throws IOException {
			int val = target.read(b, off, len);
			if (val != -1)
				count += val;
			checkMax();
			return val;

		}

		@Override
		public long skip(long n) throws IOException {
			long val = target.skip(n);
			count += val;
			checkMax();
			return val;
		}

		@Override
		public int available() throws IOException {
			return target.available();
		}

		@Override
		public void close() throws IOException {
			target.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			target.mark(readlimit);
		}

		@Override
		public synchronized void reset() throws IOException {
			target.reset();
		}

		@Override
		public boolean markSupported() {
			return target.markSupported();
		}

	}

	private static class RequestInvokeHandler implements InvocationHandler {

		private Object target;
		private long max = 0;

		public RequestInvokeHandler(Object target, long max) {
			this.target = target;
			this.max = max;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if ("getInputStream".equals(method.getName())) {
				ServletInputStream in = (ServletInputStream) method.invoke(target, args);
				return new ServletInputStreamWrapper(in, max);
			}
			return method.invoke(target, args);
		}

	}

}
