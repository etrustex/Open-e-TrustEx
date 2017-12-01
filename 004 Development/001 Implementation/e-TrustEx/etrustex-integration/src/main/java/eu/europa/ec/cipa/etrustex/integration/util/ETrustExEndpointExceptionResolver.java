package eu.europa.ec.cipa.etrustex.integration.util;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.security.xwss.XwsSecurityValidationException;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

/**
 * 
 * Exception Resolver used only with XwsSecurityInterceptor for now. 
 * Need to be re-adapted in case it needs to handle different types of events. 
 *
 */
public class ETrustExEndpointExceptionResolver implements EndpointExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(ETrustExEndpointExceptionResolver.class);
	
	@Override
	public boolean resolveException(MessageContext messageContext, Object o, Exception exc) {
		
		logger.error("Unauthorized Access Exception",exc);
		
		SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
		SoapFault fault;
		fault = response.getSoapBody().addClientOrSenderFault(ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null);
		
		if (exc instanceof XwsSecurityValidationException) {
			return true;
		}
		
		SoapFaultDetail detail = fault.addFaultDetail();
		detail.addNamespaceDeclaration("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
		
		QName wsRootQname = null;
		try {
			wsRootQname = PayloadRootUtils.getPayloadRootQName(messageContext.getRequest().getPayloadSource(), TransformerFactory.newInstance());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String incomingNameSpace  = wsRootQname.getNamespaceURI();
		logger.info("Incoming namespace: " + incomingNameSpace);
		
		
//		DOMResult res =(DOMResult)detail.addFaultDetailElement(new QName("ec:services:wsdl:Fault-0.1","Fault","ec")).getResult();
		DOMResult res =(DOMResult)detail.addFaultDetailElement(new QName(incomingNameSpace, "Fault", "ec")).getResult();
		SOAPElement responseCode =(SOAPElement) res.getNode().getOwnerDocument().createElementNS("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc:ResponseCode");
		
		responseCode.setValue(ErrorResponseCode.AUTHENTICATION_FAILED.getCode());
		res.getNode().appendChild(responseCode);
		return true;
	}
}
