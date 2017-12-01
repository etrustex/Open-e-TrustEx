package eu.europa.ec.cipa.etrustex.integration.util;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Component
public class AmqpMessageConverter {
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private IPartyService partyService;
	
	

	private final static String SOAP_JMS_OUT_TEMPLATE = "templates/soapJmsMessageTemplate.flt"; 	
	
	public Message convert(TrustExMessage<String> trustexMessage) {
		TrustExMessageHeader header = trustexMessage.getHeader();
		Party sender = partyService.getParty(header.getSenderPartyId()); 
		Party receiver = partyService.getParty(header.getReceiverPartyId());
		eu.europa.ec.cipa.etrustex.domain.Message dbMessage = messageService.retrieveMessageWithInitializedCollections(header.getMessageId());

		//retrieve application response		
		MessageBinary schematronResultBinary = dbMessage.getMessageBinaryByBinaryType(MessageBinaryType.SCHEMATRON_RESULT);
		
		//retrieve message raw content
		MessageBinary rawContentBinary = dbMessage.getMessageBinaryByBinaryType(MessageBinaryType.RAW_MESSAGE);
		
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> root = new HashMap<String, Object>();
		StringWriter sr = new StringWriter();
		try {
			Template temp = freemarkerConfig.getTemplate(SOAP_JMS_OUT_TEMPLATE);
			root.put("TRANSACTION_NS",header.getTransactionNamespace());
			root.put("SENDER_IDENTIFIERS", sender.getIdentifiers());
			root.put("RECEIVER_IDENTIFIERS", receiver.getIdentifiers());
			root.put("DOCUMENT_ID",header.getMessageDocumentId());
			root.put("RECEPTION_DATE", format.format(dbMessage.getReceptionDate()));
			root.put("SCHEMATRON_RESULTS", schematronResultBinary != null ? 
					new String(schematronResultBinary.getBinary(), "UTF-8").replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "") : "");
			root.put("TRANSACTION_QNAME", "{" + header.getTransactionNamespace() + "}" + header.getTransactionRequestLocalName());
			root.put("BODY", new String(rawContentBinary.getBinary(), "UTF-8"));
			
			temp.process(root, sr);

			Message message = Message.Factory.create();
			message.setBody(new AmqpValue(sr.toString()));
			return message;
		} catch (Exception e) {
			throw new MessageConversionException("Error generating AMQP message", e);
		}		
	}

}
