package eu.europa.ec.cipa.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;

public class ETrustExWSResponseCallback implements WebServiceMessageCallback {
	
	private static final Logger s_aLogger = LoggerFactory.getLogger (ETrustExWSResponseCallback.class);
	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException,
			TransformerException {
		ByteArrayOutputStream messageBytes=new ByteArrayOutputStream();
		message.writeTo(messageBytes);
		String messageString =  messageBytes.toString();
		//System.out.println(messageString);    
		 
	} 

}
