package eu.europa.ec.etrustex.integration.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;

public class FaultResolver implements FaultMessageResolver {
	
	private static final Logger s_aLogger = LoggerFactory.getLogger (FaultResolver.class);	
	@Override
	public void resolveFault(WebServiceMessage message) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		message.writeTo(stream);   
		throw new RuntimeException(stream.toString());
	}

}
