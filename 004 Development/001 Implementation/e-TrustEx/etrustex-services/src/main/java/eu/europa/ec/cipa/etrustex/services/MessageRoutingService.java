/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.cipa.etrustex.services.dao.IMessageRoutingDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chiricr
 *
 */
@Service
public class MessageRoutingService implements IMessageRoutingService {
	
	@Autowired
	private IMessageRoutingDAO messageRoutingDAO;
	
	@Override
	@Transactional
	public MessageRouting save(MessageRouting messageRouting) {
		return messageRoutingDAO.create(messageRouting);
	}
	
	@Override
	@Transactional
	public MessageRouting markAsProcessed(Long id) {
		MessageRouting messageRouting = messageRoutingDAO.read(id);
		messageRouting.setProcessed(Boolean.TRUE);
		messageRouting.getAccessInfo().setModificationDate(new Date());
		messageRouting.getAccessInfo().setModificationId("TRUSTEX");
		return messageRoutingDAO.update(messageRouting);
	}
	
	@Override
	@Transactional(readOnly = true)
	public MessageRouting findById(Long id) {
		return messageRoutingDAO.read(id);
	}

    @Override
    @Transactional(readOnly = true)
    public List<MessageRouting> findByMessageId(Long messageId) {
        return messageRoutingDAO.findByMessageId(messageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageRouting> findByEndpointId(Long endpointId) {
        return messageRoutingDAO.findByEndpointId(endpointId);
    }

    @Override
    public List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId) {
        return messageRoutingDAO.findByEndpointIdAndNotDispatched(endpointId);
    }

    @Override
    public List<MessageRouting> findByEndpointPartyId(Long partyId) {
        return messageRoutingDAO.findByEndpointPartyId(partyId);
    }

    @Override
    @Transactional
    public void deleteByMessageId (Long messageId) {
    	for (MessageRouting messageRouting : messageRoutingDAO.findByMessageId(messageId)) {
    		messageRoutingDAO.delete(messageRouting);
    	}
    }
}
