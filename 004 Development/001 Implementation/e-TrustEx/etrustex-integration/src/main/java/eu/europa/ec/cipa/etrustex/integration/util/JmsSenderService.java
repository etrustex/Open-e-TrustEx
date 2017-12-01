/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import ec.schema.xsd.eventnotification_1.EventNotificationType;
import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;

/**
 * @author chiricr
 *
 */
@Component("jmsSenderService")
public class JmsSenderService implements IMessageSenderService {
	
	@Autowired
	@Qualifier("replyJmsTemplate")
	private JmsTemplate replyJmsTemplate;
	
	@Autowired
	@Qualifier("toDocumentQueueTemplate")
	private JmsTemplate toDocumentQueueTemplate;
	
	@Autowired
	private IMetadataService metadataService;
	
	private static final Map<Class<?>,JAXBContext> jaxbContextMap = new HashMap<>();
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static DatatypeFactory dtf = null;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendMessage(SaajSoapMessage soapMessage, MessageHeaders headers) throws Exception {	
		if (((JmsHeaders)headers).getReplyTo() == null) {
			return;
		}
		if (soapMessage != null && StringUtils.isNotEmpty(headers.getCorrelationId())) {
			soapMessage.getSaajMessage().getSOAPHeader().setAttribute(org.springframework.integration.jms.JmsHeaders.CORRELATION_ID, headers.getCorrelationId());
		}
		replyJmsTemplate.convertAndSend(((JmsHeaders)headers).getReplyTo(), soapMessage != null ? soapMessage : "");
	}	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void sendEventNotification(EventType eventType, PartyIdentifier receiverId, PartyIdentifier senderId) throws JAXBException {
		EventNotificationType eventNotificationType = new EventNotificationType();
		ec.schema.xsd.eventnotification_1.ObjectFactory objectFactory = new ec.schema.xsd.eventnotification_1.ObjectFactory();


		Marshaller mar = getJaxBContext(eventNotificationType.getClass()).createMarshaller();

		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		mar.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		final StringWriter w = new StringWriter();
		eventNotificationType.getEvent().add(eventType);

		IDType idType = new IDType();
		idType.setValue( UUID.randomUUID().toString());
		eventNotificationType.setID(idType);

		GregorianCalendar gregorian = new GregorianCalendar();

		IssueDateType issueDateType = new IssueDateType();
		issueDateType.setValue(getXMLGregorianCalendarInstance(gregorian, DATE_FORMAT));
		eventNotificationType.setIssueDate(issueDateType);

		IssueTimeType issueTimeType = new IssueTimeType();
		issueTimeType.setValue(getXMLGregorianCalendarInstance(gregorian, TIME_FORMAT));
		eventNotificationType.setIssueTime(issueTimeType);

		JAXBElement<EventNotificationType> jaxbElement = objectFactory.createEventNotification(eventNotificationType);
		mar.marshal(jaxbElement, w);

		List<MetaDataItem> schemeIdSeparatorMetadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
		String schemeIdSeparator = CollectionUtils.isNotEmpty(schemeIdSeparatorMetadata) 
				? schemeIdSeparatorMetadata.get(0).getValue() : "#";
		String senderIdWithScheme = senderId.getSchemeId().getSchemeID() + schemeIdSeparator + senderId.getValue();
		String receiverIdWithScheme = receiverId.getSchemeId().getSchemeID() + schemeIdSeparator + receiverId.getValue();
		
		TrustExMessage<String> msg = new TrustExMessage<>(w.toString());
		msg.setHeader(new TrustExMessageHeader());
		msg.getHeader().setSenderIdWithScheme(senderIdWithScheme);
		msg.getHeader().setReceiverIdWithScheme(receiverIdWithScheme);
		msg.getHeader().setSender(senderId.getParty());

		toDocumentQueueTemplate.convertAndSend(msg);
	}
	
	private static JAXBContext getJaxBContext(Class<?> c) throws JAXBException {
		if (jaxbContextMap.containsKey(c)){
			return jaxbContextMap.get(c);
		}else{
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
			jaxbContextMap.put(c, jaxbContext);
			return jaxbContext;
		}
	}	

	private XMLGregorianCalendar getXMLGregorianCalendarInstance(GregorianCalendar gregorian, String format) {
		try {
			return getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(format).format(gregorian.getTime()));
		} catch (DatatypeConfigurationException e) {
			logger.error("XMLGregorianCalendar instance creation failed: {}", e.getMessage());
			throw new RuntimeException("XMLGregorianCalendar instance creation failed", e);
		} catch (IllegalArgumentException e) {
			logger.error("XMLGregorianCalendar instance creation failed: {}", e.getMessage());
			throw new RuntimeException("XMLGregorianCalendar instance creation failed", e);
		}
	}
	
	private static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null) {
			dtf = DatatypeFactory.newInstance();
		}
		return dtf;
	}	
}
