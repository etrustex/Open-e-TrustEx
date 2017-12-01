package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.MessagePriority;
import eu.europa.ec.cipa.etrustex.domain.PartyCredentials;
import eu.europa.ec.cipa.etrustex.services.dao.IMessagePriorityDAO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessagePriorityService implements IMessagePriorityService {
	
	@Autowired
	private IMessagePriorityDAO messagePriorityDAO;
	@Autowired
	private ICredentialsService credentialsService;
	

	@Override
	@Transactional(readOnly=true)
	public MessagePriority findByUserAndTransaction(String username, Long transactionId) {
        Credentials partyCredentials = new PartyCredentials();
        partyCredentials.setUser(username);
		Credentials credential = credentialsService.findByTypeAndUsername(partyCredentials).get(0);
		List<MessagePriority> messagePriorityList = messagePriorityDAO.findByUserAndTransaction(credential.getId(), transactionId);
		return CollectionUtils.isEmpty(messagePriorityList) ? null : messagePriorityList.get(0);
	}

}
