package eu.europa.ec.etrustex.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;

import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;

public class  ETrustExWSRequestCallback implements WebServiceMessageCallback {
	
	private static final Logger s_aLogger = LoggerFactory.getLogger (ETrustExWSRequestCallback.class);
	
	private String body;
	private String wsdlNamespace;
	private String sender;
	private String receiver;  
	private TrustExMessageHeader header;
	
	public ETrustExWSRequestCallback(String sender,String receiver,String body,String wsdlNamespace
			) {
		super();
		this.sender = sender;
		this.receiver = receiver; 
		this.body = body;
		this.wsdlNamespace = wsdlNamespace;
	}
	
	public ETrustExWSRequestCallback(TrustExMessageHeader header) {
		super();
	}
	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException,
			TransformerException {
		SaajSoapMessage soapMessage = (SaajSoapMessage)message;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(); 
		docFactory.setNamespaceAware(true);
		try {
		
			SOAPFactory sFactory = SOAPFactory.newInstance();
			QName ns = new QName(wsdlNamespace,"Header","ec");
			
			QName businessHeaderNS = new QName("ec:schema:xsd:CommonAggregateComponents-2","BusinessHeader","ec1");
			QName senderNS = new QName("http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader","Sender","stan");
			QName receiverNS = new QName("http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader","Receiver","stan");
			QName identifierNS = new QName("http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader","Identifier","stan");
			soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("stan", "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader");
			soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec1", "ec:schema:xsd:CommonAggregateComponents-2");
			soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec", wsdlNamespace);
//			soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec1", "ec:schema:xsd:CommonAggregateComponents-2");
//			soapMessage.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec", wsdlNamespace);
			
			SOAPElement soapElement =soapMessage.getSaajMessage().getSOAPHeader().addChildElement(ns);
			SOAPElement businessHeaderSoapElement =  sFactory.createElement(businessHeaderNS);
			SOAPElement senderSoapElement =  sFactory.createElement(senderNS);
			SOAPElement idSoapElement =  sFactory.createElement(identifierNS);
			idSoapElement.addTextNode(sender);
			senderSoapElement.addChildElement(idSoapElement);
			businessHeaderSoapElement.addChildElement(senderSoapElement);
			soapElement.addChildElement(businessHeaderSoapElement);
			if(receiver != null){
				SOAPElement receiverSoapElement =  sFactory.createElement(receiverNS);
				idSoapElement =  sFactory.createElement(identifierNS);
				idSoapElement.addTextNode(receiver);
				receiverSoapElement.addChildElement(idSoapElement);
				businessHeaderSoapElement.addChildElement(receiverSoapElement);
			}          
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			ByteArrayInputStream bodySource = new ByteArrayInputStream(body.getBytes()); 
			Document doc = docBuilder.parse(bodySource);
			soapMessage.getSaajMessage().getSOAPBody().addNamespaceDeclaration("ec", wsdlNamespace);
			soapMessage.getSaajMessage().getSOAPBody().addDocument(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		message.writeTo(stream);
	} 
	
}