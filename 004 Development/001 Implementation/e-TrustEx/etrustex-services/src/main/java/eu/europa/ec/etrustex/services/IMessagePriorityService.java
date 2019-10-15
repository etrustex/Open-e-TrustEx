package eu.europa.ec.etrustex.services;

import eu.europa.ec.etrustex.domain.MessagePriority;

public interface IMessagePriorityService {
	
	MessagePriority findBySenderOrTransaction(Long senderId, Long transactionId);

}
