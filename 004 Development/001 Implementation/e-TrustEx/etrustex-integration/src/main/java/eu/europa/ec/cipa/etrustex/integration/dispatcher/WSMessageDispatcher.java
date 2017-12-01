package eu.europa.ec.cipa.etrustex.integration.dispatcher;

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
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.ws.ETrustExWSRequestCallback;
import eu.europa.ec.cipa.etrustex.integration.ws.ETrustExWSResponseCallback;
import eu.europa.ec.cipa.etrustex.integration.ws.FaultResolver;
import eu.europa.ec.cipa.etrustex.integration.ws.PreemptiveBasicAuthInterceptor;
import eu.europa.ec.cipa.etrustex.integration.ws.RemoveSoapHeadersInterceptor;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;

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
			
			if ( endpoint.getWsEndpointURL()!= null){
				
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
				
			}else{
				logger.error("WsEndpointURL can't be null");
				throw new RuntimeException("WsEndpointURL can't be null");
			}		
					
			
			client.addRequestInterceptor(new RemoveSoapHeadersInterceptor(),0);
			//HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);
			HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(client);
			if (BooleanUtils.isTrue(endpoint.getIsAuthenticationRequired()) && endpoint.getCredentials() != null){
				// endpoint.getCredentials().getPassword()
				Credentials creds = new UsernamePasswordCredentials(endpoint.getCredentials().getUser(),endpoint.getCredentials().getPassword());
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
			                    new UsernamePasswordCredentials(endpoint.getProxyCredential().getUser(),endpoint.getProxyCredential().getPassword()));
				}
			}
			
			
			ws.setMessageSender(messageSender);
			ETrustExWSRequestCallback requestCallBack = new ETrustExWSRequestCallback(message);
			ETrustExWSResponseCallback responseCallback = new ETrustExWSResponseCallback();
			ws.setFaultMessageResolver(new FaultResolver());
			ws.sendAndReceive(requestCallBack, responseCallback);
		//	long elapsedTimeInfo 	= System.currentTimeMillis() - start;

		} catch (Exception e) {
			logger.error("Error occured when calling WS endpoint", e);
			throw new MessageRoutingException(e.getMessage(),e);
		}
		
		//ws.
	}
	@Override
	protected TrustExMessage<String> transformMessage(
			TrustExMessage<String> message, Transaction transaction) throws MessageRoutingException {
		String payload =  message.getPayload();
		try {
			//TODO add support for other transformations
			switch(endpoint.getEndointMessageType()){
				// we call back the same webservice
				case TYPED :
					payload= createWebserviceRootMessage(transaction.getNamespace(), transaction.getRequestLocalName(),payload, transaction.getDocument().getNamespace());
				// we send back an application response notification
				case NOTIFICATION :
					;
					
			}
		} catch (Exception e) {
			logger.error("Error occured transforming the message befor dispatching", e);
			throw new MessageRoutingException("Error occured transforming the message befor dispatching",e);
		} 
		message.setPayload(payload);
		return message;
	}

}
