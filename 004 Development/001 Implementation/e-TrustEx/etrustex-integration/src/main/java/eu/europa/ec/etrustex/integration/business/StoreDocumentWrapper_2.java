package eu.europa.ec.etrustex.integration.business;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.CreateMessageDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.types.DocumentStatusCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;

public class StoreDocumentWrapper_2 extends TrustExBusinessService implements ISynchBusinessService{
		
	private static final Logger logger = LoggerFactory.getLogger(StoreDocumentWrapper_2.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(
			TrustExMessage<String> message) throws BusinessException {
		logger.debug("StoreDocumentWrapper_2 start processMessage");
		List<MessageBinary> binaries = new ArrayList<MessageBinary>(); 
		EntityAccessInfo info = null; 
		
		for (TrustExMessageBinary trustexMessageBinary : message.getBinaries()) {
			MessageBinary bin = new MessageBinary();
			bin.setId(trustexMessageBinary.getBinaryId());
			
			info = createEntityAccessInfo(message);
			
			bin.setAccessInfo(info);
			bin.setBinaryIs(trustexMessageBinary.getBinaryContentIS());
			bin.setMimeCode(trustexMessageBinary.getMimeType()); 
			bin.setBinaryType(trustexMessageBinary.getBinaryType());
			binaries.add(bin);
		}
		
		MessageBinary rawMessage = new MessageBinary();
		info = createEntityAccessInfo(message);
		rawMessage.setAccessInfo(info);		
		try {
			rawMessage.setBinary(message.getPayload().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);			
		}
		rawMessage.setMimeCode("text/xml");
		rawMessage.setBinaryType(MessageBinaryType.RAW_MESSAGE.name());
		binaries.add(rawMessage);
		
		MessageBinary rawHeader = new MessageBinary();
		info = createEntityAccessInfo(message);
		rawHeader.setAccessInfo(info);		
		try {
			rawHeader.setBinary(message.getHeader().getRawHeader().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);			
		}
		rawHeader.setMimeCode("text/xml");
		rawHeader.setBinaryType(MessageBinaryType.RAW_HEADER.name());
		binaries.add(rawHeader);
		
		try{
			messageService.createMessage( 
				new CreateMessageDTO.Builder()
					.icaId(message.getHeader().getInterchangeAgreementId())
					.documentId(message.getHeader().getMessageDocumentId())
					.correlationId(message.getHeader().getCorrelationId())
					.statusCode(DocumentStatusCode.RECEIVED.name())
					.issuerId(message.getHeader().getIssuerPartyId())
					.transactionTypeId(message.getHeader().getTransactionTypeId())
					.authenticatedUser(message.getHeader().getAuthenticatedUser())
					.receptionDate(Calendar.getInstance().getTime())
					.issueDate(message.getHeader().getIssueDate())
					.receiverPartyId(message.getHeader().getReceiverPartyId())
					.senderPartyId(message.getHeader().getSenderPartyId())
					.documentTypeCd(message.getBinaries().iterator().next().getBinaryType())
					.binaries(binaries)
					.build());
			TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
			responseMessage.setHeader(message.getHeader());
			return responseMessage;
		} catch(MessageCreationException e)  {
			
			BusinessException businessException = new BusinessException(  "soapenv:"+ e.getResponseCode().getFaultCode(), e.getResponseCode().getDescription(), null, e.getResponseCode(), null, e.getMessage()); 
			message.getHeader().setBusinessException(businessException);
			throw businessException;
		}
		
	}

	private EntityAccessInfo createEntityAccessInfo(
			TrustExMessage<String> message) {
		EntityAccessInfo info;
		info = new EntityAccessInfo();
		info.setCreationDate(Calendar.getInstance().getTime());
		info.setCreationId(message.getHeader().getAuthenticatedUser());
		return info;
	}
	

	

}
