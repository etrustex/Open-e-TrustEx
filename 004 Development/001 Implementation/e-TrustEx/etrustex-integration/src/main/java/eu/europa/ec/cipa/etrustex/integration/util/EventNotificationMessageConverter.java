/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.util;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chiricr
 *
 */
@Component("eventNotificationMessageConverter")
public class EventNotificationMessageConverter implements MessageConverter {
	
	private final static String CONFIGURATION_NOTIFICATION_TEMPLATE = "templates/eventNotificationMessageTemplate.flt";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	@Autowired
	public Configuration freemarkerConfig;
	@Autowired
	private IMetadataService metadataService;
	

	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#toMessage(java.lang.Object, javax.jms.Session)
	 */
	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		TrustExMessage<String> msg =(TrustExMessage<String>)object;
		TextMessage jmsMessage = session.createTextMessage();

		Party senderParty = msg.getHeader().getSender();
		String usernamePassword = senderParty.getCredentials().getUser() + ":" + "etrustex"; 
		jmsMessage.setStringProperty(AUTHORIZATION_HEADER, new String(Base64.encode(usernamePassword.getBytes()))); // login:passwod base64 encode

		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

		Map<String, Object> root = new HashMap<>();
		StringWriter sr = new StringWriter();
		try {
			Template temp = freemarkerConfig.getTemplate(CONFIGURATION_NOTIFICATION_TEMPLATE);
			
			List<MetaDataItem> schemeIdSeparatorMetadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
			String schemeIdSeparator = CollectionUtils.isNotEmpty(schemeIdSeparatorMetadata) 
					? schemeIdSeparatorMetadata.get(0).getValue() : "#";
			String[] senderIdWithScheme = msg.getHeader().getSenderIdWithScheme().split(schemeIdSeparator);
			String[] receiverIdWithScheme = msg.getHeader().getReceiverIdWithScheme().split(schemeIdSeparator);			
			root.put("SENDER_IDENTIFIER_SCHEME_ID", senderIdWithScheme[0]);
			root.put("SENDER_IDENTIFIER", senderIdWithScheme[1]);
			root.put("RECEIVER_IDENTIFIER_SCHEME_ID", receiverIdWithScheme[0]);
			root.put("RECEIVER_IDENTIFIER", receiverIdWithScheme[1]);
			
			root.put("BODY", msg.getPayload());

			temp.process(root, sr);

			jmsMessage.setText(sr.toString());

			return jmsMessage;
		} catch (Exception e) {
			throw new MessageConversionException("Error generating JMS message", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#fromMessage(javax.jms.Message)
	 */
	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		// TODO Auto-generated method stub
		return null;
	}

}
