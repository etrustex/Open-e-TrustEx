/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.routing.MessageRouting;

/**
 * @author chiricr
 *
 */
public interface IMessageRoutingService {
	
	/**
	 * saves an instance of MessageRouting
	 * @param messageRouting
	 * @return
	 */
	public MessageRouting save(MessageRouting messageRouting);
	
	public MessageRouting markAsProcessed(Long id);
	
	public MessageRouting findById(Long id);
	
    public List<MessageRouting> findByMessageId(Long messageId);

    List<MessageRouting> findByEndpointId(Long endpointId);

    List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId);

    List<MessageRouting> findByEndpointPartyId(Long partyId);

    public void deleteByMessageId (Long messageId);
}
