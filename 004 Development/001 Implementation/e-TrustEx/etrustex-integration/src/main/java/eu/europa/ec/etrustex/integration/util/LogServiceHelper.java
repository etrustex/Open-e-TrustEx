/**
 * 
 */
package eu.europa.ec.etrustex.integration.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.ITransactionService;
import eu.europa.ec.etrustex.types.LogModuleEnum;

/**
 * @author chiricr
 * Helper class used for creating complex LogDTO objects
 *
 */
@Component("logServiceHelper")
public class LogServiceHelper {
		
	@Autowired
	private IBusinessDomainService businessDomainService;
	
	@Autowired
	private IPartyService partyService;
	
	@Autowired
	private ITransactionService transactionService;
	
	/**
	 * creates a LogDTO based on the given parameters
	 * @param message the Spring Integration message
	 * @param logType the log type (e.g. INFO, WARN, ERROR)
	 * @param logOperation the operation to be logged
	 * @param description the log description
	 * @param logClass the logging class
	 * @return a LogDTO object
	 */
	@Transactional(readOnly=true)
	public LogDTO createLog(TrustExMessage<String> message, LogDTO.LOG_TYPE logType, LogDTO.LOG_OPERATION logOperation, String description, String logClass) {
		Transaction transaction = transactionService.getTransaction(message.getHeader().getTransactionTypeId());
		
		String logMessageDocumentId = message.getHeader().getMessageDocumentId();
		//trim message document id to 255 characters
		logMessageDocumentId = StringUtils.isNotEmpty(logMessageDocumentId) && logMessageDocumentId.length() > 255 
			? logMessageDocumentId.substring(0, 255)
			: logMessageDocumentId;
		
		return new LogDTO.LogDTOBuilder(logType, logOperation, logClass)
			.businessCorrelationId(message.getHeader().getCorrelationId())
			.businessDomain(retrieveBusinessDomain(message))
			.correlationId(message.getHeader().getLogCorrelationId())
			.description(description)
			.documentId(message.getHeader().getMessageDocumentId() != null && message.getHeader().getMessageDocumentId().length() > 250 
					? message.getHeader().getMessageDocumentId().substring(0,  250) 
					: message.getHeader().getMessageDocumentId())
			.issuerParty(message.getHeader().getIssuerPartyId() != null ? partyService.getParty(message.getHeader().getIssuerPartyId()) : null)
			.messageId(message.getHeader().getMessageId())
			.module(LogModuleEnum.ETRUSTEX)
			//ETRUSTEX-12264: multicast can lead to several receivers but they will be logged in authorisation activator to not introduce too many changes in the log activator. 
			.receiverParty(message.getHeader().getReceiverPartyId() != null ? partyService.getParty(message.getHeader().getReceiverPartyId()) : null)
			.senderParty(message.getHeader().getSenderPartyId() != null ? partyService.getParty(message.getHeader().getSenderPartyId()) : null)
			.transaction(transaction)
			.documentTypeCode(transaction != null ? transaction.getDocument().getDocumentTypeCode() : null)
			.build();		
	}
	
	private BusinessDomain retrieveBusinessDomain(TrustExMessage<?> trustexMessage) {
		if (trustexMessage.getHeader().getIssuer() != null) {
			return trustexMessage.getHeader().getIssuer().getBusinessDomain();
		} else {
			return businessDomainService.retrieveBusinessDomain(trustexMessage.getHeader().getAuthenticatedUser());
		}
	}
	

}
