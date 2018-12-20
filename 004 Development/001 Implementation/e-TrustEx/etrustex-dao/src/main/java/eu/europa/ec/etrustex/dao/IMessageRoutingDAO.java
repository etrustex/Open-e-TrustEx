/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.domain.routing.MessageRouting;

/**
 * @author chiricr
 *
 */
public interface IMessageRoutingDAO extends ITrustExDAO<MessageRouting, Long> {
	public void deleteByMessageId(Long messageId);
    public List<MessageRouting> findByMessageIds(Set<Long> messageIds);
	List<MessageRouting> findByMessageId(Long messageId);
    List<MessageRouting> findByEndpointId(Long endpointId);
    List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId);
    List<MessageRouting> findByEndpointPartyId(Long partyId);
}
