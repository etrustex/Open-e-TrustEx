package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.IMessagePriorityDAO;
import eu.europa.ec.etrustex.domain.MessagePriority;
import eu.europa.ec.etrustex.services.IMessagePriorityService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessagePriorityService implements IMessagePriorityService {
	
	@Autowired
	private IMessagePriorityDAO messagePriorityDAO;
	
	@Override
	@Transactional(readOnly=true)
	public MessagePriority findBySenderOrTransaction(Long senderId, Long transactionId){
		List<MessagePriority> messagePriorityList = messagePriorityDAO.findBySenderOrTransaction(senderId, transactionId);
		
		if(CollectionUtils.isEmpty(messagePriorityList)){
			return null;
		}
		
		MessagePriority mpReturn = null;
		
		//Check if there is an exact match 
		for (MessagePriority messagePriority : messagePriorityList) {
            if (messagePriority.getSender() != null
                    && senderId.equals(messagePriority.getSender().getId())
                    && messagePriority.getTransaction() != null
                    && transactionId.equals(messagePriority.getTransaction().getId())) {
				return messagePriority;
			}				
		}
							
		MessagePriority mpSender = null;
		MessagePriority mpTx = null;
		for (MessagePriority messagePriority : messagePriorityList) {
			if(messagePriority.getSender() != null && senderId.equals(messagePriority.getSender().getId())){
				mpSender = messagePriority;
			}else if(messagePriority.getTransaction() != null && transactionId.equals(messagePriority.getTransaction().getId())){
				mpTx = messagePriority;
			}
		}
		
		if(mpSender != null && mpTx != null){
			mpReturn = (mpSender.getPriority() > mpTx.getPriority()) ? mpSender : mpTx;
		}else {
			mpReturn = (mpTx == null) ? mpSender: mpTx;
		}

		return mpReturn;		
	}
}
