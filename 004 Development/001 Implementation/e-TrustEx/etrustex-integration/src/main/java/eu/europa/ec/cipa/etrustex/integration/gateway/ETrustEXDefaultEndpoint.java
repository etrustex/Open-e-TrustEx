/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.gateway;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MessageEndpoint;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import eu.europa.ec.cipa.etrustex.integration.ejb.DocumentQueueMessageListener;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsHeaders;
import eu.europa.ec.cipa.etrustex.integration.util.MessageHeaders;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

/**
 * @author chiricr
 * 
 * Default endpoint for the JMS interface. If the message arrives here, the operation is invalid.
 *
 */
@Component("defaultEndpoint")
public class ETrustEXDefaultEndpoint implements MessageEndpoint {
	
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	
	@Autowired
	@Qualifier("amqpSenderService")
	private IMessageSenderService amqpSenderService;
	
	
	@Autowired
	private SaajSoapMessageFactory saajSoapMessageFactory;
	
	public static final String DEFAULT_SOAP_FAULT_NAMESPACE = "urn:oasis:names:specification:ubl:schema:xsd:Fault-1";

	/* (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.MessageEndpoint#invoke(org.springframework.ws.context.MessageContext)
	 */
	@Override
	public void invoke(MessageContext messageContext) throws Exception {		
		MessageHeaders messageHeaders = DocumentQueueMessageListener.getJmsHeaders();
		if (messageHeaders == null || messageHeaders.getReplyTo() == null) {
			return;
		}
		
		SaajSoapMessage soapMessage = saajSoapMessageFactory.createWebServiceMessage();
		SOAPElement faultElement = soapMessage.getSaajMessage().getSOAPBody().addFault(
				new QName("http://schemas.xmlsoap.org/soap/envelope", "Client", "SOAP-ENV"), ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription());
		SOAPElement detailSoapElement = faultElement.addChildElement(
				new QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "detail", "cbc"));
		SOAPElement ecFaultElement = detailSoapElement.addChildElement(
				new QName(DEFAULT_SOAP_FAULT_NAMESPACE, "Fault", "ec"));	
		SOAPElement responseCodeElement = ecFaultElement.addChildElement(
				new QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ResponseCode", "cbc"));
		responseCodeElement.setTextContent(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getCode());
		
		messageHeaders.setResponseCode("500");
		sendMessage(soapMessage, messageHeaders);		

	}

	private void sendMessage(SaajSoapMessage soapMessage, MessageHeaders headers) throws Exception {
		if (headers == null) {
			return;
		}
		IMessageSenderService senderService = headers instanceof JmsHeaders ? jmsSenderService : amqpSenderService;
		senderService.sendMessage(soapMessage, headers);
	}
}
