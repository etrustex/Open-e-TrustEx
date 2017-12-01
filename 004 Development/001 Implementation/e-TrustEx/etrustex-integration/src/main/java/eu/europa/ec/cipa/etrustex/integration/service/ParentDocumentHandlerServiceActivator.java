package eu.europa.ec.cipa.etrustex.integration.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.IMessageProcessorActivator;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.BusinessErrorHandlingStrategy;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import eu.europa.ec.cipa.etrustex.types.RedeliveryReasonType;

public class ParentDocumentHandlerServiceActivator extends TrustExServiceActivator implements IMessageProcessorActivator {

	@Autowired
	private JmsTemplate toWaitingRoomTemplate;
	private Long timeFrameMS = 120000L;

	private static final Logger logger = LoggerFactory.getLogger(ParentDocumentHandlerServiceActivator.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message) {
		String parentDocumentID = message.getPayload().getHeader().getMessageParentDocumentId();
		Map<MetaDataItemType, MetaDataItem> metadata = message.getPayload().getHeader().getMetadata();
		MetaDataItem strategyItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_NA_STRATEGY);
		MetaDataItem timeFrameItem = metadata.get(MetaDataItemType.PARENT_ID_NOT_FOUND_TIMEFRAME);
		if (timeFrameItem != null) {
			timeFrameMS = Long.valueOf(timeFrameItem.getValue());
		}
		String parentDocumentTypeCode = message.getPayload().getHeader().getMessageParentDocumentTypeCode();

		BusinessErrorHandlingStrategy strategy = BusinessErrorHandlingStrategy.IGNORE;
		
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.VALIDATION, 
				"Inside ParentDocumentHandlerServiceActivator ", 
				this.getClass().getName());	
		
		if (strategyItem != null) {
			try {
				strategy = BusinessErrorHandlingStrategy.valueOf(strategyItem.getValue());
			} catch (Exception e) {
				strategy = BusinessErrorHandlingStrategy.IGNORE;
			}
		}
		if (parentDocumentID == null) {
			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
			return builder.build();
		} else {
			Set<String> states = new HashSet<String>(Arrays.asList("ERROR", "SUBMITTED"));
			eu.europa.ec.cipa.etrustex.domain.Message parent = messageService.retrieveMessage(parentDocumentID, parentDocumentTypeCode, 
					message.getPayload().getHeader().getSenderPartyId(), message.getPayload().getHeader().getReceiverPartyId(), true, states);

			if (parent == null) {
				switch (strategy) {
				case ERROR:
					try {
						logDTO.setDescription(logDTO.getDescription() + " Parent document ID does not exist.");
						logService.saveLog(logDTO);
						eu.europa.ec.cipa.etrustex.domain.Message msgDB = messageService.retrieveMessage(message.getPayload().getHeader().getMessageId());
						String appResponseId = message.getPayload().getHeader().getMessageDocumentId() + "_" + msgDB.getMessageDocumentTypeCode() + "_ERR";
						createApplicationResponse(message.getPayload(), appResponseId, ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getCode(),
								ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getDescription(), message.getPayload().getHeader().getReplyTo());
						return null;
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
						logService.saveLog(logDTO);
						return null;
					}
				case WAIT:
					Long today = Calendar.getInstance().getTimeInMillis();
					Long timeFrame = message.getPayload().getHeader().getReceivedDate().getTime() + timeFrameMS;
					// if it's the first redelivery we send the message to the
					// waiting room queue
					if (message.getPayload().getHeader().getRedeliveryReason() == null) {
						message.getPayload().getHeader().setRedeliveryReason(RedeliveryReasonType.PARENT_ID_NOT_FOUND);
						toWaitingRoomTemplate.convertAndSend(message.getPayload());
						return null;
					}
					if (today.compareTo(timeFrame) <= 0) {
						throw new RuntimeException("Sending back message to waitning room");
					} else {
						try {
							logDTO.setDescription(logDTO.getDescription() + " Parent document ID does not exist.");
							logService.saveLog(logDTO);
							eu.europa.ec.cipa.etrustex.domain.Message msgDB = messageService.retrieveMessage(message.getPayload().getHeader().getMessageId());
							String appResponseId = message.getPayload().getHeader().getMessageDocumentId() + "_" + msgDB.getMessageDocumentTypeCode() + "_ERR";
							createApplicationResponse(message.getPayload(), appResponseId, ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getCode(),
									ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST.getDescription(), message.getPayload().getHeader().getReplyTo());
							return null;
						} catch (Exception e) {
							logger.error("Parent Document not found", e);
							logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
							logService.saveLog(logDTO);
							return null;
						}
					}
				default:// TODO log the parent id missing event to the db
					MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
					logger.error("Parent Document not found");
					logDTO.setDescription(logDTO.getDescription() + "Parent Document not found");
					logService.saveLog(logDTO);
					return builder.build();
				}
			} else {// so parent exists
				try {
					eu.europa.ec.cipa.etrustex.domain.Message msg = messageService.retrieveMessage(message.getPayload().getHeader().getMessageId());
					msg.addParentMessage(parent);
					messageService.updateMessage(msg);
					// keep parent id in the message header
					if (parent.getId() != null) {
						message.getPayload().getHeader().setParentMessageId(parent.getId());
					}
				} catch (MessageUpdateException e) {
					logger.error(e.getMessage(), e);
					logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
					logService.saveLog(logDTO);
					return null;
				}
				MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
				return builder.build();
			}
		}
	}

	@Override
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message) {
		// TODO Auto-generated method stub
		return null;
	}

	public JmsTemplate getToWaitingRoomTemplate() {
		return toWaitingRoomTemplate;
	}

	public void setToWaitingRoomTemplate(JmsTemplate toWaitingRoomTemplate) {
		this.toWaitingRoomTemplate = toWaitingRoomTemplate;
	}

}
