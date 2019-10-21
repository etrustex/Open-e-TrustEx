package eu.europa.ec.etrustex.integration.task;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.dao.IMessageRoutingDAO;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.types.EventNotificationType;

@Service
public class PolicyHelper {
	
	public static final String APP_RESP_TX_NAME = "SubmitApplicationResponse";
	public static final String WRAPPER_TX_NAME = "StoreDocumentWrapper";
	
	protected static final Logger logger = LoggerFactory.getLogger(PolicyHelper.class);
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private IMessageRoutingDAO messageRoutingDao;
	
	@Autowired
	private ILogService logService;

    @Autowired
    private IEventNotificationService eventNotificationService;

    @Autowired
    @Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;


    @Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteWrapperMessage(Long messageId) throws MessageUpdateException, JAXBException {
        Message message = messageService.retrieveMessage(messageId);
		messageService.deleteMessage(message);
        submitNotifications(message);
	}
	
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteMessage(Long messageId) throws MessageUpdateException{
		Message message = messageService.retrieveMessage(messageId);
		List<Message> appResponsesToDelete = new ArrayList<Message>();
		List<Message> wrappersToDelete = new ArrayList<Message>();
	
		// Removing the connection to the Parents
		for (Message parent : message.getParentMessages()) {
			parent.getChildMessages().remove(message);
		}
		message.getParentMessages().clear();

		// Removing the connection to the Children
		for (Message child : message.getChildMessages()) {
			child.getParentMessages().remove(message);
			if (APP_RESP_TX_NAME.equals(child.getTransaction().getName())) {
				appResponsesToDelete.add(child);
			}else if (WRAPPER_TX_NAME.equals(child.getTransaction().getName()) && (child.getParentMessages().size() == 0)){				
				wrappersToDelete.add(child);
			}
		}
		message.getChildMessages().clear();

		// Deleting Application Responses
		for (Message del : appResponsesToDelete) {
			messageRoutingDao.deleteByMessageId(del.getId());
			messageService.deleteMessage(del);
			//ETRUSTEX-977 add log for deleted messages
			LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.APPLY_RETENTION_POLICY, this.getClass().getName())
				.description("Inside PolicyHelper - deleting message")				
				.messageId(del.getId())
				.build();
			logService.saveLog(logDTO);
		}
		
		//Deleting Wrappers
		for (Message wrpMsg : wrappersToDelete) {
			try {
				deleteWrapperMessage(wrpMsg.getId());
			} catch (MessageUpdateException | JAXBException e) {
				logger.error("Could not delete message with doc Id: "+wrpMsg.getDocumentId(), e);
			}
		}
		
		//Deleting Message Routing references
		messageRoutingDao.deleteByMessageId(message.getId());
		
		message = messageService.updateMessage(message);
		messageService.deleteMessage(message);
		
		//ETRUSTEX-977 add log for deleted messages
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.APPLY_RETENTION_POLICY, this.getClass().getName())
			.description("Inside PolicyHelper - deleting message")
			.messageId(messageId)
			.build();
		logService.saveLog(logDTO);
	}
	
	
	
	public IMessageService getMessageService() {
		return messageService;
	}
	
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}



	public IMessageRoutingDAO getMessageRoutingDao() {
		return messageRoutingDao;
	}



	public void setMessageRoutingDao(IMessageRoutingDAO messageRoutingDao) {
		this.messageRoutingDao = messageRoutingDao;
	}

    private void submitNotifications(Message message) throws JAXBException {
        EventType eventType = eventNotificationService.getDeleteDocumentEventNotification(message);

        // Get parties configured to receive notifications
        List<Party> configuredParties = eventNotificationService.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.DOC,
                message.getSender().getBusinessDomain().getId(), null);

        for(Party p : configuredParties) {
            PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
            PartyIdentifier senderId = eventNotificationService.getSenderId(p);
            jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
        }
    }
}