package eu.europa.ec.cipa.etrustex.integration.dispatcher;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.ITrustExJmsMessageConverter;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * This class allows transforming an EtrustEx Message to an eProcurement back-office JMS message.
 * This class should translate messages
 * @author derveol
 *
 */
public class FrontOfficeMessagConverter implements ITrustExJmsMessageConverter {

	private static final Logger logger = LoggerFactory.getLogger(FrontOfficeMessagConverter.class);
	
	
	private static final String MSG_DOCUMENT_ID="MSG_DOCUMENT_ID";
	private static final String MSG_DOCUMENT_TYPE="MSG_DOCUMENT_TYPE";			
	private static final String MSG_DOCUMENT_STATUS="MSG_DOCUMENT_STATUS";
	private static final String MSG_DOCUMENT_RESPONSE_CODE="MSG_DOCUMENT_RESPONSE_CODE";
	private static final String MSG_DOCUMENT_SENDER_GLN="MSG_DOCUMENT_SENDER_GLN";
	private static final String MSG_DOCUMENT_RECEIVER_GLN="MSG_DOCUMENT_RECEIVER_GLN";
	private static final String MSG_DOCUMENT_RECEIPT_DATE="MSG_DOCUMENT_RECEIPT_DATE";
	private static final String MSG_DOCUMENT_ISSUE_DATE="MSG_DOCUMENT_ISSUE_DATE";
	private static final String MSG_PARENT_ID="MSG_PARENT_ID";
	private static final String MSG_PARENT_TYPE="MSG_PARENT_TYPE";
	private static final String MSG_PARENT_STATUS="MSG_PARENT_STATUS";
	private static final String MSG_PARENT_RESPONSE_CODE="MSG_PARENT_RESPONSE_CODE";
	private static final String MSG_PARENT_SENDER_GLN="MSG_PARENT_SENDER_GLN";
	private static final String MSG_PARENT_RECEIVER_GLN="MSG_PARENT_RECEIVER_GLN";
	
			
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMessageService messageService;
	
	@Override
	public Message toMessage(TrustExMessage<String> message, Session session)
			throws JMSException {
		try{
			
			logger.info("Translate message to JMS");
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			
			TextMessage msg = session.createTextMessage();
			eu.europa.ec.cipa.etrustex.domain.Message msgDB = messageService.retrieveMessage(message.getHeader().getMessageId());
			
			Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long)null, null, null, null);
			String separator = ":";
			if(metadata.containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)){
				separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
			}else{
				logger.error("--> 'SCHEME_ID_SEPARATOR' Metadata is not configured so ':' will be used");
			}
							
						
			msg.setStringProperty(MSG_DOCUMENT_ID ,msgDB.getDocumentId());			
			logger.info(MSG_DOCUMENT_ID + ","+msgDB.getDocumentId());
			
			msg.setStringProperty(MSG_DOCUMENT_TYPE ,msgDB.getMessageDocumentTypeCode());
			logger.info(MSG_DOCUMENT_TYPE + ","+msgDB.getMessageDocumentTypeCode());
			
			eu.europa.ec.cipa.etrustex.domain.Message parentMsg = null;
			if (message.getHeader().getParentMessageId() !=null){
				parentMsg = messageService.retrieveMessage(message.getHeader().getParentMessageId() );
			}
			
			String codeStatus = translateStatusCodeValue(message.getHeader().getMessageStatusCode() ,msgDB, parentMsg);
			msg.setStringProperty(MSG_DOCUMENT_STATUS ,codeStatus);
			logger.info(MSG_DOCUMENT_STATUS + ","+codeStatus);
			
			msg.setStringProperty(MSG_DOCUMENT_RESPONSE_CODE,message.getHeader().getResponseCode());
			logger.info(MSG_DOCUMENT_RESPONSE_CODE + ","+message.getHeader().getResponseCode());
			
			String partyId = getGLN( msgDB.getSender());
			msg.setStringProperty(MSG_DOCUMENT_SENDER_GLN ,partyId);
			logger.info(MSG_DOCUMENT_SENDER_GLN + ","+partyId);
			
			partyId = getGLN( msgDB.getReceiver());
			msg.setStringProperty(MSG_DOCUMENT_RECEIVER_GLN,partyId);
			logger.info(MSG_DOCUMENT_SENDER_GLN + ","+partyId);
			
			String dateStr = msgDB.getReceptionDate()!=null?sdfDateTime.format(msgDB.getReceptionDate()):null;
			msg.setStringProperty(MSG_DOCUMENT_RECEIPT_DATE, dateStr);
			logger.info(MSG_DOCUMENT_RECEIPT_DATE + ","+dateStr);
			
			dateStr = msgDB.getIssueDate()!=null?sdfDate.format(msgDB.getIssueDate()):null;
			msg.setStringProperty(MSG_DOCUMENT_ISSUE_DATE, dateStr);
			logger.info(MSG_DOCUMENT_ISSUE_DATE + ","+dateStr);
			
			if (parentMsg !=null){
				//eu.europa.ec.cipa.etrustex.domain.Message parentMsg = messageService.retrieveMessage(message.getHeader().getParentMessageId() );  								
				msg.setStringProperty(MSG_PARENT_ID ,parentMsg.getDocumentId());
				logger.info(MSG_PARENT_ID + ","+parentMsg.getDocumentId());
				
				msg.setStringProperty(MSG_PARENT_TYPE ,parentMsg.getMessageDocumentTypeCode());
				logger.info(MSG_PARENT_TYPE + ","+parentMsg.getMessageDocumentTypeCode());
				
				codeStatus = translateStatusCodeValue(message.getHeader().getParentStatusCode(),  parentMsg,null);//don't take into account the parent of the parent
				msg.setStringProperty(MSG_PARENT_STATUS ,codeStatus);
				logger.info(MSG_PARENT_STATUS + ","+codeStatus);
				
				msg.setStringProperty(MSG_PARENT_RESPONSE_CODE,parentMsg.getResponseCode());
				logger.info(MSG_PARENT_RESPONSE_CODE + ","+parentMsg.getResponseCode());
				
				partyId = getGLN( parentMsg.getSender());
				msg.setStringProperty(MSG_PARENT_SENDER_GLN ,partyId);
				logger.info(MSG_PARENT_SENDER_GLN + ","+partyId);
				
				partyId = getGLN( parentMsg.getReceiver());
				msg.setStringProperty(MSG_PARENT_RECEIVER_GLN,partyId);
				logger.info(MSG_PARENT_RECEIVER_GLN + ","+partyId);
			}else{
				msg.setStringProperty(MSG_PARENT_ID ,null);
				msg.setStringProperty(MSG_PARENT_TYPE ,null);
				msg.setStringProperty(MSG_PARENT_STATUS ,null);
				msg.setStringProperty(MSG_PARENT_RESPONSE_CODE,null);
				msg.setStringProperty(MSG_PARENT_SENDER_GLN ,null);
				msg.setStringProperty(MSG_PARENT_RECEIVER_GLN,null);
			}
			
			
			msg.setText(message.getPayload());
			logger.info("Translation of message to JMS done");
			return msg;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new JMSException( "Unable to create JMS message");
		}		
	}
	
	private String translateStatusCodeValue(String statusCode, eu.europa.ec.cipa.etrustex.domain.Message message, eu.europa.ec.cipa.etrustex.domain.Message parentMessage) {
		Map<String, MessageResponseCode> respcodes = metadataService
				.retrieveMessageResponseCodes(null, null, message
						.getTransaction().getDocument().getId(), null);
		logger.debug("Response count:" + respcodes.size());
		MessageResponseCode code = respcodes.get(statusCode);
		logger.debug("Code to translate:" + statusCode);
		String translatedCode = code == null ? "" : code.getStatusCode();
		
		
		//TODO (ode) rework this for services 2.0!
		//call for tenders, tender receipt, qualification response and attached document (on tender receipt and qualification response) should have the state "SENT" instead of "RECEIVED".
		switch(message.getMessageDocumentTypeCode()){
			case "443":
			case "ATR":
			case "TQR":
				if ("RECEIVED".equals(statusCode) ){
					logger.debug("Transform Received into Sent for backward compatibility");
					translatedCode = "4"; //SENT instead of received
				}
				break;
			case "916":
				if (parentMessage!=null){
					switch(parentMessage.getMessageDocumentTypeCode()){
						case "ATR":
						case "TQR":
							if ("RECEIVED".equals(statusCode) ){
								logger.debug("Transform Received into Sent for backward compatibility");
								translatedCode = "4"; //SENT instead of received
							}
					}
				}
		}
		
		logger.debug("Translated code:" + translatedCode);
		return translatedCode;
	}

	private String getGLN(Party party) {
		String partyGLN=null; 
		if (party==null){
			return null;
		}
		for (PartyIdentifier id: party.getIdentifiers()){
			if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(id.getSchemeId().getISO6523Code())){				
				partyGLN = id.getValue();
				break;
			}
		}
		return partyGLN;
	}

	@Override
	public TrustExMessage<String> fromMessage(Message message)
			throws JMSException {
		throw new RuntimeException("Not implemented!");
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

}
