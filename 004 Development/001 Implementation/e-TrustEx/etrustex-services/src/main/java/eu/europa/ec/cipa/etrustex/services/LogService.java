/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.log.Log;
import eu.europa.ec.cipa.etrustex.services.dao.ILogDAO;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.FrequencyType;

/**
 * @author chiricr
 *
 */
@Service
public class LogService extends ThreadLocal<String> implements ILogService {
	
	@Autowired
	private ILogDAO logDAO;
	
	@Override 
	protected String initialValue() {
		return UUID.randomUUID().toString();
	}
	
	@Override
	public String getLogCorrelationId() {
		return getLogCorrelationId(false);
	}	
	
	@Override
	public void reinitializeCorrelationId() {
		remove();
	}

	
	private String getLogCorrelationId(boolean reinitialize) {
		//ETRUSTEX-693 force reinitialization of thread local variable
		if (reinitialize) {
			remove();
		}
		return get();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public LogDTO saveLog(LogDTO logDTO) {
		if (StringUtils.isEmpty(logDTO.getCorrelationId())) {
			logDTO.setCorrelationId(getLogCorrelationId(logDTO.getReinitializeCorrelationId()));
		}
		Log	log = new Log.LogBuilder(logDTO.getLogType().name(), logDTO.getCorrelationId())
			.businessCorrelationId(logDTO.getBusinessCorrelationId())
			.businessDomain(logDTO.getBusinessDomain())
			.description(logDTO.getDescription())
			.documentId(logDTO.getDocumentId())
			.documentTypeCode(logDTO.getDocumentTypeCode())
			.issuerParty(logDTO.getIssuerParty())
			.largeValue(logDTO.getLargeValue())
			.messageId(logDTO.getMessageId())
			.messageBinaryId(logDTO.getMessageBinaryId())
			.messageSize(logDTO.getMessageSize())
			.operation(logDTO.getOperation().name())
			.receiverParty(logDTO.getReceiverParty())
			.senderParty(logDTO.getSenderParty())
			.transaction(logDTO.getTransaction())
			.urlContext(logDTO.getUrlContext())
			.value(logDTO.getValue())
			.module(logDTO.getModule())
			.username(logDTO.getUsername())
			.userRole(logDTO.getUserRole())
			.entity(logDTO.getEntity())
			.authIpAddress(logDTO.getAuthIpAddress())
			.entityId(logDTO.getEntityId())
			.logClass(logDTO.getLogClass())
			.build();
		log = logDAO.create(log);
		logDTO.setId(log.getId());
		return logDTO;
	}
	
	@Override
	public long getVolumeCountForParty(long partyId, FrequencyType frequencyType) {
		return logDAO.getVolumeCountForParty(partyId, frequencyType);
	}
	
	@Override
	public Log getLog(Long id) {
		return logDAO.getLog(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Log> findLogsByCriteria(LogDTO logDTO, Date from, Date to, int firstResult, int maxResults) {
		return logDAO.findLogsByCriteria(logDTO, from, to, firstResult, maxResults);
	}

	@Override
	public long count(LogDTO logDTO, Date from, Date to) {
		return logDAO.count(logDTO, from, to);
	}
}
