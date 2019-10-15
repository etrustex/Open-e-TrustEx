package eu.europa.ec.etrustex.services.util;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;

import eu.europa.ec.etrustex.integration.gateway.ETrustEXDefaultEndpoint;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.types.ErrorResponseCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ETrustExEndpointExceptionResolver implements
		EndpointExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(ETrustExEndpointExceptionResolver.class);

	@Override   
	public boolean resolveException(MessageContext messageContext, Object endpoint,
			Exception exc) {
		
		if(exc instanceof IllegalStateException){
			
			QName wsRootQname = null;
			try {
				wsRootQname = PayloadRootUtils.getPayloadRootQName(messageContext.getRequest().getPayloadSource(), TransformerFactory.newInstance());
			} catch (TransformerException | TransformerFactoryConfigurationError e) {
				logger.error(e.getMessage(),e);
			}
			String incomingNameSpace  = ETrustEXDefaultEndpoint.DEFAULT_SOAP_FAULT_NAMESPACE;
			
			MessageProcessingException e = new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), 
					null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE,null,"Undefined incoming message");
			SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
			SoapFault fault;
			if ("soapenv:Client".equalsIgnoreCase(e.getFault())) {
				fault = response.getSoapBody()
						.addClientOrSenderFault(e.getDescription(),
								e.getLocale());
			} else {
				fault = response.getSoapBody()
						.addServerOrReceiverFault(
								e.getDescription(), e.getLocale());
			}
			SoapFaultDetail detail = fault.addFaultDetail();
			detail.addNamespaceDeclaration("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
			DOMResult res =(DOMResult)detail.addFaultDetailElement(new QName(incomingNameSpace,"Fault","ec")).getResult();
			SOAPElement responseCode =(SOAPElement) res.getNode().getOwnerDocument().createElementNS("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc:ResponseCode");
			String responseCodeString = null;
			if (e.getDocumentTypeCode() != null){
				responseCodeString = e.getDocumentTypeCode()+":"+e.getFaultDetailResponseCode().getCode();
			}else{
				responseCodeString = e.getFaultDetailResponseCode().getCode();
			}
			responseCode.setValue(responseCodeString);
			res.getNode().appendChild(responseCode);
			return true;
		}
		  
		return false;     
	}

}
