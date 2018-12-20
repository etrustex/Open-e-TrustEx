package eu.europa.ec.etrustex.integration.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.IllegalStateException;
import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.CreateMessageDTO;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.types.DocumentStatusCode;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;

public class StoreMessageServiceActivator extends TrustExServiceActivator {
		
	private static final Logger logger = LoggerFactory.getLogger(StoreMessageServiceActivator.class);

	private void storeSynchMessage(Message<TrustExMessage<String>> message, Long receiverID, String receiverIdWithScheme, boolean isMulticastRequest){
		List<MessageBinary> binaries = new ArrayList<MessageBinary>();
		EntityAccessInfo info = new EntityAccessInfo();
		info.setCreationDate(message.getPayload().getHeader().getReceivedDate());
		info.setCreationId(message.getPayload().getHeader().getAuthenticatedUser());
		Transaction transaction = authorisationService.getTransactionById(message.getPayload().getHeader().getTransactionTypeId());
		
		
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.CRUD, 
				"Inside StoreMessageServiceActivator", 
				this.getClass().getName());	
		
		if ("SubmitAttachedDocumentRequest".equals(transaction.getRequestLocalName()) 
				&& (message.getPayload().getBinaries() == null || message.getPayload().getBinaries().isEmpty())) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.BINARY_ERROR.getDescription(), null, 
					ErrorResponseCode.BINARY_ERROR, null, null);
		}
		
		try {
			for (TrustExMessageBinary messageBinary : message.getPayload().getBinaries()) {
				MessageBinary bin = new MessageBinary();
				if (messageBinary.getBinaryContentIS() != null){
					Long binId = messageService.createMessageBinary(
						new CreateMessageBinaryDTO.Builder()
							.binaryType(MessageBinaryType.BINARY_ATTACHMENT.name())
							.mimeType(messageBinary.getMimeType())
							.creatorID(message.getPayload().getHeader().getAuthenticatedUser())
							.inputStream(messageBinary.getBinaryContentIS())
							.encryptBinary(false)
							.useFileStore(false)
							.issuer(message.getPayload().getHeader().getIssuer())
							.documentId(message.getPayload().getHeader().getMessageDocumentId())
							.senderIdWithScheme(message.getPayload().getHeader().getSenderIdWithScheme())
							.build());
					bin.setId(binId);
				}else{	
					bin.setBinary(messageBinary.getBinaryContent());
					bin.setMimeCode(messageBinary.getMimeType());
					bin.setBinaryType(MessageBinaryType.BINARY_ATTACHMENT.name());
				}
				binaries.add(bin);
			}
			
			MessageBinary rawMessage = new MessageBinary();
			rawMessage.setAccessInfo(info);
			rawMessage.setBinary(message.getPayload().getPayload().getBytes("UTF-8"));
			rawMessage.setMimeCode("text/xml");
			rawMessage.setBinaryType(MessageBinaryType.RAW_MESSAGE.name());
			
			MessageBinary rawHeader = new MessageBinary();
			rawHeader.setAccessInfo(info);
			rawHeader.setBinary(message.getPayload().getHeader().getRawHeader().getBytes("UTF-8"));
			rawHeader.setMimeCode("text/xml");
			rawHeader.setBinaryType(MessageBinaryType.RAW_HEADER.name());
			
			binaries.add(rawHeader);
			binaries.add(rawMessage);
			Long msgId= messageService.createMessage( 
				new CreateMessageDTO.Builder()
					.icaId(message.getPayload().getHeader().getInterchangeAgreementId())
					.documentId(message.getPayload().getHeader().getMessageDocumentId())
					.correlationId(message.getPayload().getHeader().getCorrelationId())
					.statusCode(DocumentStatusCode.SUBMITTED.name())
					.issuerId(message.getPayload().getHeader().getIssuerPartyId())
					.transactionTypeId(message.getPayload().getHeader().getTransactionTypeId())
					.authenticatedUser(message.getPayload().getHeader().getAuthenticatedUser())
					.receptionDate(message.getPayload().getHeader().getReceivedDate())
					.issueDate(message.getPayload().getHeader().getIssueDate())
					.receiverPartyId(receiverID)
					.senderPartyId(message.getPayload().getHeader().getSenderPartyId())
					//ETRUSTEX 1012
					.documentTypeCd(transaction.getTransactionTypeCode() !=null
						? transaction.getTransactionTypeCode() : transaction.getDocument().getDocumentTypeCode())
					.binaries(binaries)
					.build());
			
			logDTO.setMessageId(msgId);
			
			message.getPayload().getHeader().setMessageId(msgId);
			message.getPayload().getHeader().setReceiverIdWithScheme(receiverIdWithScheme);
			message.getPayload().getHeader().setTransactionNamespace(transaction.getNamespace());
			message.getPayload().getHeader().setTransactionRequestLocalName(transaction.getRequestLocalName());
			message.getPayload().getHeader().setLogCorrelationId(isMulticastRequest
					? logService.getLogCorrelationId() + msgId : logService.getLogCorrelationId());//ETRUSTEX-1264: to distinguish processing of multiple messages			
			logService.saveLog(logDTO);
			message.getPayload().getHeader().setLogCorrelationId(logService.getLogCorrelationId());

			
			
		} catch(MessageCreationException e)  {
			//logger.error( e.getMessage(),e);
			//no need to log "business exception" (derveol)
			String documentTypeCode = ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(e.getResponseCode()) 
					? transaction.getDocument().getDocumentTypeCode() : null;
			throw new MessageProcessingException(  "soapenv:"+ e.getResponseCode().getFaultCode(), e.getResponseCode().getDescription(), 
					null, e.getResponseCode(), documentTypeCode, e.getMessage());
		} catch (IOException e) {
			logger.error( e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} catch (PersistenceException e) {
			//ETRUSTEX-612 hide persistence exception details in the fault string (chiricr)
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} catch (IllegalStateException e) {
			//ETRUSTEX-610 hide JMS exception details in the fault string (chiricr)
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}

	}
	
	public Message<TrustExMessage<String>> storeSynchMessage(Message<TrustExMessage<String>> message){
		//TODO change here to iterate on the list
		TrustExMessageHeader header = message.getPayload().getHeader();
		boolean isMulticastRequest = CollectionUtils.isNotEmpty(header.getReceiverIdWithSchemeList()) 
				&& header.getReceiverIdWithSchemeList().size() > 1; 
		//ETRUSTEX-1264
		if (header.isMulticastSupported()){
			
			List<Long> receiverIdsStored = new ArrayList<Long>();
			
			for(String receiverIdwithScheme : header.getReceiverIdWithSchemeList()){
				
				Long receiverId = header.getReceiverIdByreceiverIdWithScheme().get(receiverIdwithScheme);
						
				if (receiverIdsStored.contains(receiverId))
					continue; //skip when having already stored a message for that receiver but via another sheme id.
				
				header.setReceiverPartyId(receiverId);
				header.setInterchangeAgreementId(header.getInterchangeAgreementIdByreceiverIdWithScheme().get(receiverIdwithScheme));
				
				storeSynchMessage(message, message.getPayload().getHeader().getReceiverPartyId(), receiverIdwithScheme, isMulticastRequest);
				receiverIdsStored.add(receiverId);
			}
		}else{		
			storeSynchMessage(message, message.getPayload().getHeader().getReceiverPartyId(), message.getPayload().getHeader().getReceiverIdWithScheme(), isMulticastRequest);
		}
		
		return message;
	}
	
	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	} 

}
