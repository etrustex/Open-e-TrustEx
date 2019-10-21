package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.MessagePriority;

public interface IMessagePriorityDAO extends ITrustExDAO<MessagePriority, Long>{
	
	List<MessagePriority> findBySenderOrTransaction(Long senderId, Long transactionId);

}
