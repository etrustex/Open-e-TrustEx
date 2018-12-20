package eu.europa.ec.etrustex.integration.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;

import eu.europa.ec.etrustex.integration.message.TrustExMessage;

public class ETrustExWSRequestCallback implements WebServiceMessageCallback {

	private static final Logger s_aLogger = LoggerFactory
			.getLogger(ETrustExWSRequestCallback.class);

	private TrustExMessage<String> trusExMessage;

	public ETrustExWSRequestCallback(TrustExMessage<String> message) {
		super();
		this.trusExMessage = message;
	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException,
			TransformerException {
		SaajSoapMessage soapMessage = (SaajSoapMessage) message;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		docFactory.setNamespaceAware(true);
		try {
			SOAPFactory sFactory = SOAPFactory.newInstance();
			String wsdlNamespace = trusExMessage.getHeader()
					.getTransactionNamespace();
			QName ns = new QName(wsdlNamespace, "Header", "ec");
			QName businessHeaderNS = new QName(
					"ec:schema:xsd:CommonAggregateComponents-2", "BusinessHeader",
					"ec1");
			QName senderNS = new QName(
					"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader",
					"Sender", "stan");
			QName receiverNS = new QName(
					"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader",
					"Receiver", "stan");
			QName identifierNS = new QName(
					"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader",
					"Identifier", "stan");
			soapMessage
					.getSaajMessage()
					.getSOAPHeader()
					.addNamespaceDeclaration("stan",
							"http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader");
			soapMessage
					.getSaajMessage()
					.getSOAPHeader()
					.addNamespaceDeclaration("ec1",
							"ec:schema:xsd:CommonAggregateComponents-2");
			soapMessage.getSaajMessage().getSOAPHeader()
					.addNamespaceDeclaration("ec", wsdlNamespace);
			
			soapMessage.getSaajMessage().setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

			// soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec1",
			// "ec:schema:xsd:CommonAggregateComponents-2");
			// soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec",
			// wsdlNamespace);

			SOAPElement soapElement = soapMessage.getSaajMessage().getSOAPHeader()
					.addChildElement(ns);
			SOAPElement businessHeaderSoapElement = sFactory
					.createElement(businessHeaderNS);
			SOAPElement senderSoapElement = sFactory.createElement(senderNS);
			SOAPElement idSoapElement = sFactory.createElement(identifierNS);
			//TODO ode: I doubt it is valid we should instead use scheme attribute..
			idSoapElement.addTextNode(trusExMessage.getHeader().getSenderIdWithScheme());
			senderSoapElement.addChildElement(idSoapElement);
			businessHeaderSoapElement.addChildElement(senderSoapElement);
			//ETRUSTEX-1264: multicast support 
			if (trusExMessage.getHeader().isMulticastSupported()){
				for (String idWithScheme : trusExMessage.getHeader().getReceiverIdWithSchemeList()){
					SOAPElement receiverSoapElement = sFactory
							.createElement(receiverNS);
					idSoapElement = sFactory.createElement(identifierNS);
					//TODO ode: I doubt it is valid we should instead use scheme attribute..
					idSoapElement.addTextNode(trusExMessage.getHeader().getReceiverIdWithScheme());
					receiverSoapElement.addChildElement(idSoapElement);
					businessHeaderSoapElement.addChildElement(receiverSoapElement);
				}
				
			}else{			
				if (trusExMessage.getHeader().getReceiverIdWithScheme() != null) {
					SOAPElement receiverSoapElement = sFactory
							.createElement(receiverNS);
					idSoapElement = sFactory.createElement(identifierNS);
					//TODO ode: I doubt it is valid we should instead use scheme attribute..
					idSoapElement.addTextNode(trusExMessage.getHeader().getReceiverIdWithScheme());
					receiverSoapElement.addChildElement(idSoapElement);
					businessHeaderSoapElement.addChildElement(receiverSoapElement);
				}
			}
			soapElement.addChildElement(businessHeaderSoapElement);
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			ByteArrayInputStream bodySource = new ByteArrayInputStream(
					trusExMessage.getPayload().getBytes("UTF-8"));
			Document doc = docBuilder.parse(bodySource);
			soapMessage.getSaajMessage().getSOAPBody()
					.addNamespaceDeclaration("ec", wsdlNamespace);
			soapMessage.getSaajMessage().getSOAPBody().addDocument(doc);
			
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			message.writeTo(stream);
//			System.out.println(stream.toString());
		} catch (Exception e) {
			throw new TransformerException(e);
		} 
	}

}