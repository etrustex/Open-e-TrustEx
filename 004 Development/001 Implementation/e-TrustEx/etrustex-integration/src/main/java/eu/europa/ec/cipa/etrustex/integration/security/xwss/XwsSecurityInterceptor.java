package eu.europa.ec.cipa.etrustex.integration.security.xwss;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.security.WsSecurityValidationException;
import org.springframework.ws.soap.security.xwss.XwsSecuritySecurementException;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.integration.util.SOAPService;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;

public class XwsSecurityInterceptor extends org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(XwsSecurityInterceptor.class);
	
	private IAuthorisationService authorisationService; 
	
	@Autowired
	private SOAPService soapService;
	
	public IAuthorisationService getAuthorisationService() {
		return authorisationService;
	}
	public void setAuthorisationService(IAuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}

	@Override
	protected void secureMessage(SoapMessage soapMessage,
			MessageContext messageContext)
			throws XwsSecuritySecurementException {
		Boolean sign = true;
		//TODO Use Fault [9]
		try {
			QName wsRootQname = PayloadRootUtils.getPayloadRootQName(messageContext.getRequest().getPayloadSource(),TransformerFactory.newInstance());
			if((wsRootQname != null) && wsRootQname.toString().contains("RetrieveDocumentWrapperRequestRequest")){
				sign = false;
			}
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if (!sign)
			return;
		
		String principal = soapService.getUserPrincipal();
		Boolean sigRequired = authorisationService.isSignatureRequired(principal);
		
		sign = false;
		//TODO ode: rework on exception handling
		try{
			Party issuerParty = authorisationService.getMessageIssuer(principal);
			sign = issuerParty.getBusinessDomain().getResponseSignatureRequired();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(sign || sigRequired){
			super.secureMessage(soapMessage, messageContext);
			Iterator<SoapHeaderElement> it = soapMessage.getSoapHeader().examineHeaderElements(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security"));
			while(it.hasNext()){
				SoapHeaderElement e = it.next();
				e.setMustUnderstand(false);
			}
		}		
	}

	@Override
	protected void validateMessage(SoapMessage soapMessage,
			MessageContext messageContext) throws WsSecurityValidationException {	
		Boolean validate = true;
		//TODO Use Fault [9]
		try {
			QName wsRootQname = PayloadRootUtils.getPayloadRootQName(messageContext.getRequest().getPayloadSource(),TransformerFactory.newInstance());
			if((wsRootQname != null) && wsRootQname.toString().contains("StoreDocumentWrapperRequest")){
				validate = false;
			}
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if(validate){
			String username = soapService.getUserPrincipal();
			Boolean sigRequired = authorisationService.isSignatureRequired(username);		
			logger.debug("The authenticated user is : " + username + " signature required : " + sigRequired);
			if (sigRequired){
				super.validateMessage(soapMessage, messageContext);				
			}
		}
		
	}

}
