package eu.europa.ec.etrustex.integration.dispatcher;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.ITrustExJmsMessageConverter;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.service.CannonicalServiceActivator;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.etrustex.types.MetaDataItemType;

/**
 * This class allows transforming an EtrustEx Message to an eProcurement back-office JMS message.
 * This class should translate messages
 * @author derveol
 *
 */
public class BackOfficeMessagConverter implements ITrustExJmsMessageConverter {

	private static final Logger logger = LoggerFactory.getLogger(BackOfficeMessagConverter.class);
	
	@Autowired
	private IPartyService partyService;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private CannonicalServiceActivator cannonicalService;
			
	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public Message toMessage(TrustExMessage<String> message, Session session)
			throws JMSException {
		try{
			
			logger.info("Translate message to JMS");
			TextMessage msg = session.createTextMessage();
			
			//retrieve org from receiver (always to the Contracting Authority back office)
			//retrieve lef from sender (always from the Economic Operator)	
									
			Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long)null, null, null, null, null);
			String separator = ":";
			if(metadata.containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)){
				separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
			}else{
				logger.error("--> 'SCHEME_ID_SEPARATOR' Metadata is not configured so ':' will be used");
			}
			
			String organization = "";
			Party receiver = partyService.getParty(message.getHeader().getReceiverPartyId());
			for (PartyIdentifier id: receiver.getIdentifiers()){
				if (IdentifierIssuingAgency.EC_ORG.getISO6523Code().equals(id.getSchemeId().getISO6523Code())){				
					organization = IdentifierIssuingAgency.EC_ORG.getSchemeID() + separator + id.getValue();
					break;
				}
			}
			logger.info("Organization:"+organization);
			String lef="";
			String senderGLN="";
			Party sender= partyService.getParty(message.getHeader().getSenderPartyId());
			for (PartyIdentifier id: sender.getIdentifiers()){
				if (IdentifierIssuingAgency.EC_LEF.getISO6523Code().equals(id.getSchemeId().getISO6523Code())){				
					lef = IdentifierIssuingAgency.EC_LEF.getSchemeID() + separator +id.getValue();					
				}else if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(id.getSchemeId().getISO6523Code())){				
					senderGLN = id.getValue();					
				}
			}
			logger.info("Lef:"+lef);
			logger.info("senderGLN:"+senderGLN);
			
			//TODO ode :use enum (header types)
			
			msg.setStringProperty("CUSTOMER_ORGANIZATION", organization);
			msg.setStringProperty("SUPPLIER_LEF_ID", lef);
			msg.setStringProperty("SUPPLIER_GLN_ID", senderGLN);
			
			//retrieve canonical content
			
			eu.europa.ec.etrustex.domain.Message dbMsg = messageService.retrieveMessage(message.getHeader().getMessageId());
			//

			
			String localName = dbMsg.getTransaction().getDocument().getLocalName();
			
			switch (localName){
			
				case "CallForTenders":
				case "Shortlist":					
					msg.setText(cannonicalService.createCanonical(message, dbMsg,null));
					break;
					
				default: 	
					if (msg.getText()==null){
						msg.setText(message.getPayload()); // if still equal to null we provide the payload so normally the raw content
					}			
			}
			
			
			
			logger.info("Translation of message to JMS done");
			return msg;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new JMSException( "Unable to create JMS message");
		}		
	}

	@Override
	public TrustExMessage<String> fromMessage(Message message)
			throws JMSException {
		throw new RuntimeException("Not implemented!");
	}

	public IPartyService getPartyService() {
		return partyService;
	}

	public void setPartyService(IPartyService partyService) {
		this.partyService = partyService;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}



}
