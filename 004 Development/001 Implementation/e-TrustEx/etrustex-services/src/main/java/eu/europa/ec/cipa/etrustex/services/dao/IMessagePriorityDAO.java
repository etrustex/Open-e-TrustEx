package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.MessagePriority;

public interface IMessagePriorityDAO extends ITrustExDAO<MessagePriority, Long>{
	
	List<MessagePriority> findByUserAndTransaction(Long credentialId, Long transactionId);

}
