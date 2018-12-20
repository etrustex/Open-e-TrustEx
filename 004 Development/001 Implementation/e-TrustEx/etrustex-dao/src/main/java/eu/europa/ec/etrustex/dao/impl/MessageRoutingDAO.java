/**
 * 
 */
package eu.europa.ec.etrustex.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IMessageRoutingDAO;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;

/**
 * @author chiricr
 *
 */
@Repository
public class MessageRoutingDAO extends TrustExDAO<MessageRouting, Long> implements
		IMessageRoutingDAO {
	
	public void deleteByMessageId(Long messageId){
		entityManager
				.createQuery("DELETE FROM MessageRouting mr WHERE mr.message.id = :mId")
				.setParameter("mId", messageId)
				.executeUpdate();
	}
	
	@Override
	public List<MessageRouting> findByMessageIds(Set<Long> messageIds){
		return entityManager
                .createQuery("from MessageRouting mr where mr.id IN :messageIds", MessageRouting.class)
                .setHint(QueryHints.HINT_READONLY, Boolean.TRUE)
                .setParameter("messageIds", messageIds).getResultList();
	}
	
    @Override
    public List<MessageRouting> findByMessageId(Long messageId) {
        return entityManager
                .createQuery("from MessageRouting mr where mr.message.id = :messageId order by processed desc, accessInfo.modificationDate desc", MessageRouting.class)
                .setParameter("messageId", messageId).getResultList();
    }

    @Override
    public List<MessageRouting> findByEndpointId(Long endpointId) {
        return entityManager
                .createQuery("from MessageRouting mr where mr.endpoint.id = :endpointId", MessageRouting.class)
                .setParameter("endpointId", endpointId).getResultList();
    }

    @Override
    public List<MessageRouting> findByEndpointIdAndNotDispatched(Long endpointId) {
        return entityManager
                .createQuery("from MessageRouting mr where mr.endpoint.id = :endpointId and mr.processed = 0", MessageRouting.class)
                .setParameter("endpointId", endpointId).getResultList();
    }

    @Override
    public List<MessageRouting> findByEndpointPartyId(Long partyId) {
        return entityManager
                .createQuery("select mr from MessageRouting mr join mr.endpoint edp where edp.party.id = :partyId", MessageRouting.class)
                .setParameter("partyId", partyId).getResultList();
    }
}
