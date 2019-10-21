package eu.europa.ec.etrustex.dao.impl;

import eu.europa.ec.etrustex.dao.IEventNotificationDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.routing.EventNotification;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by guerrpa on 26/07/2016.
 */
@Repository
public class EventNotificationDAO extends TrustExDAO<EventNotification, Long> implements IEventNotificationDAO {

    @Override
    public List<EventNotification> findByParty(Party party) {
        return entityManager.createQuery("from EventNotification en where en.party.id =:partyId", EventNotification.class)
                .setParameter("partyId", party.getId())
                .getResultList();
    }

    @Override
    public List<EventNotification> findByProfile(Profile profile) {
        return entityManager.createQuery("from EventNotification en where en.profile.id =:profileId", EventNotification.class)
                .setParameter("profileId", profile.getId())
                .getResultList();
    }
}
