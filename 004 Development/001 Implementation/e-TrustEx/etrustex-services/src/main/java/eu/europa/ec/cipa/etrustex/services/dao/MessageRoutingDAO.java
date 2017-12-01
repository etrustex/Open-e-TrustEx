/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .createQuery("from MessageRouting mr where mr.endpoint.party.id = :partyId", MessageRouting.class)
                .setParameter("partyId", partyId).getResultList();
    }
}
