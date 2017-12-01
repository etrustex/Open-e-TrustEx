package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.MessagePriority;

public interface IMessagePriorityService {
	
	MessagePriority findByUserAndTransaction(String username, Long transactionId);

}
