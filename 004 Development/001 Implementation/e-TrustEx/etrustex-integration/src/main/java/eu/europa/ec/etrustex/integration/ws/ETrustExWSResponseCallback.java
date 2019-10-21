package eu.europa.ec.etrustex.integration.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class ETrustExWSResponseCallback implements WebServiceMessageCallback {
	
	private static final Logger logger = LoggerFactory.getLogger (ETrustExWSResponseCallback.class);
	private Boolean hasFault= false;
	
	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException,
			TransformerException {
		SaajSoapMessage soapMessage = (SaajSoapMessage) message;
		hasFault = soapMessage.hasFault();
		ByteArrayOutputStream messageBytes=new ByteArrayOutputStream();
		if (hasFault){
			message.writeTo(messageBytes);
			logger.error( "Error Occured forwarding the message to the final recipient ws endpoint:");
			logger.error( messageBytes.toString());
		} else {
			message.writeTo(messageBytes);
			logger.debug("Successful forwarding the message to the final recipient ws endpoint:");
			logger.debug(messageBytes.toString());
		}
		 
	} 
	
	public Boolean getHasFault() {
		return hasFault;
	}

	public void setHasFault(Boolean hasFault) {
		this.hasFault = hasFault;
	}


}
