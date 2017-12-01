/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.deletedocument_1.DeleteDocumentType;
import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageRoutingService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * @author chiricr
 *
 */
public class DeleteDocument_1 extends TrustExBusinessService implements ISynchBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(DeleteDocument_1.class);
	private static final String DELETE_DOCUMENT_RESPONSE_OK = "<ec:Acknowledgment xmlns:ec='ec:schema:xsd:Ack-2' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Acknowledgment>";
	
	@Autowired
	private IPartyService partyService;
	
	@Autowired
	private IInterchangeAgreementService interchangeAgreementService;
	
	@Autowired
	private ITransactionService transactionService;
	
	@Autowired
	private ILogService logService;	
	
	@Autowired
	protected LogServiceHelper logServiceHelper;
	
	@Autowired
	private IMessageRoutingService messageRoutingService;

    @Autowired
    private IEventNotificationService eventNotificationService;

    @Autowired
    @Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
		
		LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.BUSINESS, 
				"Inside DeleteDocument_1", this.getClass().getName());
		logService.saveLog(logDTO);
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(DeleteDocumentType.class.getPackage().getName());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(message.getPayload());
			JAXBElement<DeleteDocumentType> bodyObject = (JAXBElement)unmarshaller.unmarshal(reader);
			DeleteDocumentType deleteDocument = bodyObject.getValue();
			
			//extract data for the document to be deleted
			String documentId = deleteDocument.getDocumentReference().getID().getValue().trim();
			String documentTypeCode = deleteDocument.getDocumentReference().getDocumentTypeCode() != null 
					? deleteDocument.getDocumentReference().getDocumentTypeCode().getValue() : null;
			
			if (StringUtils.isBlank(documentTypeCode)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			documentTypeCode = documentTypeCode.trim();		
			String senderPartyId = StringUtils.isNotBlank(deleteDocument.getDocumentSenderParty().getEndpointID().getValue())
					? deleteDocument.getDocumentSenderParty().getEndpointID().getValue().trim() : "";
			String senderPartySchemeId = StringUtils.isNotBlank(deleteDocument.getDocumentSenderParty().getEndpointID().getSchemeID()) 
					? deleteDocument.getDocumentSenderParty().getEndpointID().getSchemeID().trim() : "";
			String receiverPartyId = StringUtils.isNotBlank(deleteDocument.getDocumentReceiverParty().getEndpointID().getValue())
					? deleteDocument.getDocumentReceiverParty().getEndpointID().getValue().trim() : "";
			String receiverPartySchemeId = StringUtils.isNotBlank(deleteDocument.getDocumentReceiverParty().getEndpointID().getSchemeID())
					? deleteDocument.getDocumentReceiverParty().getEndpointID().getSchemeID().trim() : "";
			
			senderPartySchemeId = StringUtils.isEmpty(senderPartySchemeId) ? IdentifierIssuingAgency.GLN.name() : senderPartySchemeId;
			receiverPartySchemeId = StringUtils.isEmpty(receiverPartySchemeId) ? IdentifierIssuingAgency.GLN.name() : receiverPartySchemeId;
			
			Party sender = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(senderPartySchemeId), senderPartyId, message.getHeader().getIssuer().getBusinessDomain());
			Party receiver = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(receiverPartySchemeId), receiverPartyId, message.getHeader().getIssuer().getBusinessDomain());
			
			if (sender == null || receiver == null){
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
			}
			
			Message dbMessage = messageService.retrieveMessage(documentId, documentTypeCode, sender.getId(), receiver.getId());
			
			if (dbMessage == null) {
				TrustExMessage<String> responseMessage = new TrustExMessage<String>(DELETE_DOCUMENT_RESPONSE_OK);
				responseMessage.setHeader(message.getHeader());
				return responseMessage;				
			}
			
			//check if deletion is allowed for DeleteDocument request sender and original message ICA
			MetaDataItem searchCriteria = new MetaDataItem();
			searchCriteria.setSender(message.getHeader().getSender());
//			searchCriteria.setInterchangeAgreement(interchangeAgreementService.getInterchangeArgreement(message.getHeader().getInterchangeAgreementId()));
			searchCriteria.setProfile(dbMessage.getAgreement().getProfile());
			searchCriteria.setRawItemType(MetaDataItemType.DELETE_DOCUMENT_ALLOWED.name());
			List<MetaDataItem> metadata = metadataService.getMetaDataItemsByCriteria(searchCriteria);
			
			if (CollectionUtils.isEmpty(metadata)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);				
			}
			
			//only the receiver and the sender of the original document may request its deletion
			if (message.getHeader().getSenderPartyId().longValue() != receiver.getId().longValue() 
					&& message.getHeader().getSenderPartyId().longValue() != sender.getId().longValue()) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);				
			}
			
			deleteMessage(dbMessage);

            submitNotifications(message.getHeader().getIssuer().getBusinessDomain().getId(), dbMessage);
			
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(null);
			responseMessage.setHeader(message.getHeader());
			responseMessage.setPayload(DELETE_DOCUMENT_RESPONSE_OK);

			return responseMessage;
			
		} catch (JAXBException | MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.BUSINESS, 
					e.getMessage(), this.getClass().getName());
			logService.saveLog(logDTO);			
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void deleteMessage(Message message) throws MessageUpdateException {
		List<Message> toDelete = new ArrayList<Message>();
		List<Message> toDetatch = new ArrayList<Message>();
		// Removing the connection to the Parents
//		for (Message p : message.getParentMessages()) {
//			p.getChildMessages().remove(message);
//		}
//		message.getParentMessages().clear();

		// Removing the connection to the Children
		
		for (Message c : message.getChildMessages()) {
//			c.getParentMessages().clear();
			
			if(c.getParentMessages() != null && c.getParentMessages().size() > 1){
				toDetatch.add(c);
				c.getParentMessages().remove(message);
			}else{
				toDelete.add(c);
			}
		}
		
		for (Message det : toDetatch) {		
			message.getChildMessages().remove(det);	
		}
		
		messageService.updateMessage(message);

		// Deleting children
		for (Message del : toDelete) {
//			messageRoutingService.deleteByMessageId(del.getId());
			//recursively delete children			
			deleteMessage(del);		
		}
		//Deleting Message Routing references
		messageRoutingService.deleteByMessageId(message.getId());
		
//		message = messageService.updateMessage(message);
		if (messageService.isManaged(message)) {
			//message is managed in the current persistent context
			messageService.deleteMessage(message);
		}

		//ETRUSTEX-977 add log for deleted messages
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.BUSINESS, this.getClass().getName())
			.description("Deleting message")
			.messageId(message.getId())
			.build();
		logService.saveLog(logDTO);		
	}

    private void submitNotifications(Long businessDomainId, Message dbMessage) throws JAXBException {
        EventType eventType = eventNotificationService.getDeleteDocumentEventNotification(dbMessage);
        // Get parties configured to receive notifications
        List<Party> configuredParties = eventNotificationService.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.DOC, businessDomainId, null);

        for(Party p : configuredParties) {
            PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
            PartyIdentifier senderId = eventNotificationService.getSenderId(p);
            jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
        }
    }
}
