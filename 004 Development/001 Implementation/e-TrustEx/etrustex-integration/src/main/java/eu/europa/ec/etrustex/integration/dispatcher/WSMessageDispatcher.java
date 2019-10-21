package eu.europa.ec.etrustex.integration.dispatcher;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.xml.soap.MessageFactory;

import org.apache.commons.lang.BooleanUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.ws.ETrustExWSRequestCallback;
import eu.europa.ec.etrustex.integration.ws.ETrustExWSResponseCallback;
import eu.europa.ec.etrustex.integration.ws.FaultResolver;
import eu.europa.ec.etrustex.integration.ws.PreemptiveBasicAuthInterceptor;
import eu.europa.ec.etrustex.integration.ws.RemoveSoapHeadersInterceptor;
import freemarker.template.TemplateException;

public class WSMessageDispatcher extends MessageDispatcher {
	
	private WSEndpoint endpoint;
	
	protected WSMessageDispatcher(Endpoint endpoint){
		super();
		this.endpoint= (WSEndpoint)endpoint;
	}
	
	
	
	@Override
	protected void sendMessage(TrustExMessage<String> message)
			throws MessageRoutingException {
		try {
		//	long start = System.currentTimeMillis();
			logger.debug("Entering webservice message dispatcher");
			LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.DISPATCHING, 
					"WS endpoint: " + endpoint.getWsEndpointURL(), 
					this.getClass().getName()); 
			logService.saveLog(logDTO);	
			
			WebServiceTemplate ws= new WebServiceTemplate();
			ws.setDefaultUri(endpoint.getWsEndpointURL());			
			ws.setMessageFactory(new SaajSoapMessageFactory(MessageFactory.newInstance()));
			
			
			DefaultHttpClient client = null;
			
			if (endpoint.getWsEndpointURL() == null) {
				logger.error("WsEndpointURL can't be null");
				throw new RuntimeException("WsEndpointURL can't be null");				
			}
							
			URL url = new URL(endpoint.getWsEndpointURL());
			
			if(url.getProtocol()!=null && url.getProtocol().equalsIgnoreCase("https")){
				//strategy to always accept server certificate
				TrustStrategy easyStrategy = new TrustStrategy() {

			        @Override
			        public boolean isTrusted(X509Certificate[] certificate, String authType)
			                throws CertificateException {
			            return true;
			        }
			    };
			    try{
			    	SSLSocketFactory sf = new SSLSocketFactory(easyStrategy,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			    	SchemeRegistry registry = new SchemeRegistry();
		        
			    	if (url.getPort()!=-1){//if there is a port: use it
			    		registry.register(new Scheme("https", url.getPort(), sf));
			    	}else{ //use default port value otherwise
			    		registry.register(new Scheme("https", url.getDefaultPort(), sf));
			    	}
			    	registry.register( new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			    	ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
			    	client = new DefaultHttpClient(ccm);
			    	
			    } catch (Exception e1) {
			    	logger.error("Unable to create HTTPS client",e1);
			    	throw new RuntimeException("Unable to create HTTPS client");
			    } 
				
			}else{
				//assume it's http.
				client = new DefaultHttpClient();	
			}			
				
				
					
			
			client.addRequestInterceptor(new RemoveSoapHeadersInterceptor(),0);
			//HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);
			HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);
			if (endpoint.getCredentials() != null){
				Credentials creds = new UsernamePasswordCredentials(endpoint.getCredentials().getUser(),(endpoint.getCredentials().getDecryptedPassword()));
				messageSender.setCredentials(creds);
				messageSender.setAuthScope(AuthScope.ANY);
				client.getCredentialsProvider().setCredentials(AuthScope.ANY,creds);
				client.addRequestInterceptor(new PreemptiveBasicAuthInterceptor(),1);
	                    
			}
			if (BooleanUtils.isTrue(endpoint.getUseProxy()) && endpoint.getProxyHost() != null && endpoint.getProxyPort() != null ){
				HttpHost proxy = new HttpHost(endpoint.getProxyHost(), endpoint.getProxyPort());
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				if (endpoint.getProxyCredential() != null ){
					client.getCredentialsProvider().setCredentials(
			                    new AuthScope(endpoint.getProxyHost(),endpoint.getProxyPort()),
			                    new UsernamePasswordCredentials(endpoint.getProxyCredential().getUser(),endpoint.getProxyCredential().getDecryptedPassword()));
				}
			}
			
			
			ws.setMessageSender(messageSender);
			ETrustExWSRequestCallback requestCallBack = new ETrustExWSRequestCallback(message);
			ETrustExWSResponseCallback responseCallback = new ETrustExWSResponseCallback();
			ws.setFaultMessageResolver(new FaultResolver());
			ws.sendAndReceive(requestCallBack, responseCallback);

		} catch (Exception e) {
			logger.error("Error occured when calling WS endpoint", e);
			throw new MessageRoutingException(e.getMessage(),e);
		}
		
	}
	@Override
	protected TrustExMessage<String> transformMessage(TrustExMessage<String> message, Transaction transaction) throws MessageRoutingException {
		try {
			String payload = createWebserviceRootMessage(transaction.getNamespace(), transaction.getRequestLocalName(), message.getPayload(), transaction.getDocument().getNamespace());
			message.setPayload(payload);
		} catch (IOException | TemplateException e) {
			logger.error("Error occured transforming the message befor dispatching", e);
			throw new MessageRoutingException("Error occured transforming the message befor dispatching",e);
		}
		return message;
	}

}
