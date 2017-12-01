package eu.europa.ec.cipa.etrustex.integration.task;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsSenderService;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.dao.IMessageRoutingDAO;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlaPolicyHelper {
	
	public static final String APP_RESP_NAME = "SubmitApplicationResponse";
	
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
	public void deleteMessage(Long messageId) throws MessageUpdateException, JAXBException {
        Message message = messageService.retrieveMessage(messageId);
		messageService.deleteMessage(message);
        submitNotifications(message);
	}
	
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteBundle(Long messageId) throws MessageUpdateException{
		Message message = messageService.retrieveMessage(messageId);
		List<Message> toDelete = new ArrayList<Message>();
	
		// Removing the connection to the Parents
		for (Message p : message.getParentMessages()) {
			p.getChildMessages().remove(message);
		}
		message.getParentMessages().clear();

		// Removing the connection to the Children
		for (Message c : message.getChildMessages()) {
			c.getParentMessages().remove(message);
			if (APP_RESP_NAME.equals(c.getTransaction().getName())) {
				toDelete.add(c);
			}
		}
		message.getChildMessages().clear();

		// Deleting Application Responses
		for (Message del : toDelete) {
			messageRoutingDao.deleteByMessageId(del.getId());
			messageService.deleteMessage(del);
			//ETRUSTEX-977 add log for deleted messages
			LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.APPLY_RETENTION_POLICY, this.getClass().getName())
				.description("Inside SlaPolicyHelper - deleting message")				
				.messageId(del.getId())
				.build();
			logService.saveLog(logDTO);
		}
		
		//Deleting Message Routing references
		messageRoutingDao.deleteByMessageId(message.getId());
		
		message = messageService.updateMessage(message);
		messageService.deleteMessage(message);
		
		//ETRUSTEX-977 add log for deleted messages
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.APPLY_RETENTION_POLICY, this.getClass().getName())
			.description("Inside SlaPolicyHelper - deleting message")
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