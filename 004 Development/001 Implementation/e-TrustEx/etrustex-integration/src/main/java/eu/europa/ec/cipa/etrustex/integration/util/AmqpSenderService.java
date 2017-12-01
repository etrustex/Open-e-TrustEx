package eu.europa.ec.cipa.etrustex.integration.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.amqp.messaging.ApplicationProperties;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.messenger.Messenger;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.integration.ejb.AmqpQueueMessageListener;
import eu.europa.ec.cipa.etrustex.integration.ejb.DocumentQueueMessageListener;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;

@Component("amqpSenderService")
public class AmqpSenderService implements IMessageSenderService {
	
	public void sendMessage(SaajSoapMessage soapMessage, MessageHeaders headers) throws Exception {
		AmqpHeaders amqpHeaders = (AmqpHeaders)headers;
		if (StringUtils.isBlank(amqpHeaders.getReplyTo())) {
			return;
		}
		//create AMQP message from SAAJ message
		Message amqpMessage = Message.Factory.create();
		amqpMessage.setCorrelationId(headers.getCorrelationId());
		Map<String, String> props = new HashMap<>();
		props.put(eu.europa.ec.cipa.etrustex.integration.util.MessageHeaders.HEADER_RESPONSE_CODE, AmqpQueueMessageListener.getAmqpHeaders().getResponseCode());
		amqpMessage.setApplicationProperties(new ApplicationProperties(props));
		writeSOAPBody(soapMessage.getSaajMessage(), amqpMessage);
		AmqpQueueMessageListener.removeAmqpHeaders();
		
		//send message
		Messenger messenger = Messenger.Factory.create();
		try {
			messenger.start();
		} catch (IOException e) {
			throw new MessageProcessingException(e.getMessage(), "");
		}
		amqpMessage.setAddress(amqpHeaders.getReplyTo());
		messenger.put(amqpMessage);
		messenger.send();
		messenger.stop();
	}	
	
  
	private void writeSOAPBody(SOAPMessage soapMessage, Message amqpMessage) throws Exception {
        ByteArrayOutputStream bodyOutput = new ByteArrayOutputStream();
        soapMessage.writeTo(bodyOutput);
        amqpMessage.setBody(new AmqpValue(bodyOutput.toString("UTF-8")));
        bodyOutput.close();
	}  	

	public void sendEventNotification(EventType eventType, PartyIdentifier receiverId, PartyIdentifier senderId) throws JAXBException {
		throw new UnsupportedOperationException();
	}
}
