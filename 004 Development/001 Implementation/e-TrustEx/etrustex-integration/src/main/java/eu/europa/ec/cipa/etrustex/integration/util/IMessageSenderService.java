package eu.europa.ec.cipa.etrustex.integration.util;

import javax.xml.bind.JAXBException;

import org.springframework.ws.soap.saaj.SaajSoapMessage;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;

public interface IMessageSenderService {
	
	public void sendMessage(SaajSoapMessage soapMessage, MessageHeaders headers) throws Exception;	
	
	public void sendEventNotification(EventType eventType, PartyIdentifier receiverId, PartyIdentifier senderId) throws JAXBException;

}
