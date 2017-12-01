/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;

import java.util.List;

/**
 * @author chiricr
 *
 */
public interface IMessageRoutingDAO extends ITrustExDAO<MessageRouting, Long> {
	public void deleteByMessageId(Long messageId);
    List<MessageRouting> findByMessageId(Long messageId);
    List<MessageRouting> findByEndpointId(Long endpointId);
    List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId);
    List<MessageRouting> findByEndpointPartyId(Long partyId);
}
