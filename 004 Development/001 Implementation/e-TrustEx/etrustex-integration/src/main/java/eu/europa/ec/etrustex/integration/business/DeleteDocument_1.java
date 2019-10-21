package eu.europa.ec.etrustex.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import eu.europa.ec.etrustex.domain.Profile;
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
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMessageRoutingService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.etrustex.types.MetaDataItemType;

/**
 * @author chiricr
 *
 */
public class DeleteDocument_1 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(DeleteDocument_1.class);
	private static final String DELETE_DOCUMENT_RESPONSE_OK = "<ec:Acknowledgment xmlns:ec='ec:schema:xsd:Ack-2' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Acknowledgment>";

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
			if (StringUtils.isBlank(documentId)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docId"));
			}
			String documentTypeCode = deleteDocument.getDocumentReference().getDocumentTypeCode() != null
					? deleteDocument.getDocumentReference().getDocumentTypeCode().getValue() : null;

			if (StringUtils.isBlank(documentTypeCode)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docTypeCode"));
			}
			documentTypeCode = documentTypeCode.trim();
			String senderPartyId = deleteDocument.getDocumentSenderParty().getEndpointID() != null &&
					StringUtils.isNotBlank(deleteDocument.getDocumentSenderParty().getEndpointID().getValue())
					? deleteDocument.getDocumentSenderParty().getEndpointID().getValue().trim()
					: "";
			if (StringUtils.isBlank(senderPartyId)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.hardrule.sender.missing"));
			}
			String senderPartySchemeId = StringUtils.isNotBlank(deleteDocument.getDocumentSenderParty().getEndpointID().getSchemeID())
					? deleteDocument.getDocumentSenderParty().getEndpointID().getSchemeID().trim()
					: IdentifierIssuingAgency.GLN.name();
			String receiverPartyId = deleteDocument.getDocumentReceiverParty().getEndpointID() != null
					&& StringUtils.isNotBlank(deleteDocument.getDocumentReceiverParty().getEndpointID().getValue())
					? deleteDocument.getDocumentReceiverParty().getEndpointID().getValue().trim()
					: "";
			if (StringUtils.isBlank(receiverPartyId)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.hardrule.receiver.missing"));
			}
			String receiverPartySchemeId = StringUtils.isNotBlank(deleteDocument.getDocumentReceiverParty().getEndpointID().getSchemeID())
					? deleteDocument.getDocumentReceiverParty().getEndpointID().getSchemeID().trim()
					: IdentifierIssuingAgency.GLN.name();


			Party sender = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(senderPartySchemeId), senderPartyId, message.getHeader().getIssuer().getBusinessDomain());
			if (sender == null) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.senderParty.unknown"));
			}

			Party receiver = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(receiverPartySchemeId), receiverPartyId, message.getHeader().getIssuer().getBusinessDomain());

			if (receiver == null) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.unknown"));
			}

			Message dbMessage = messageService.retrieveMessage(documentId, documentTypeCode, sender.getId(), receiver.getId());

			if (dbMessage == null) {
				if(deleteMetadataExists(message.getHeader().getSender(),null)){
					TrustExMessage<String> responseMessage = new TrustExMessage<String>(null);
					responseMessage.setHeader(message.getHeader());
					responseMessage.setPayload(DELETE_DOCUMENT_RESPONSE_OK);
					return responseMessage;
				}
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.deletion.noDoc"));
			}

			if(!deleteMetadataExists(message.getHeader().getSender(), dbMessage.getAgreement().getProfile())){
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.deletion.metadata"));
			}

			//only the receiver and the sender of the original document may request its deletion
			if (message.getHeader().getSenderPartyId().longValue() != receiver.getId().longValue()
					&& message.getHeader().getSenderPartyId().longValue() != sender.getId().longValue()) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.deletion.owner"));
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

	private boolean deleteMetadataExists(Party party, Profile profile){
		boolean mdExists = false;

		MetaDataItem searchCriteria = new MetaDataItem();
		searchCriteria.setSender(party);
		if(profile != null) {
			searchCriteria.setProfile(profile);
		}
		searchCriteria.setRawItemType(MetaDataItemType.DELETE_DOCUMENT_ALLOWED.name());
		List<MetaDataItem> metadata = metadataService.getMetaDataItemsByCriteria(searchCriteria);

		if(!CollectionUtils.isEmpty(metadata) && "true".equalsIgnoreCase(metadata.get(0).getValue())){
			mdExists = true;
		}

		return  mdExists;
	}

	private void deleteMessage(Message message) throws MessageUpdateException {

		messageService.deleteMessageTree(message);
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
