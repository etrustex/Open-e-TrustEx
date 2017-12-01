package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.routing.EventNotification;

import java.util.List;

/**
 * Created by guerrpa on 26/07/2016.
 */
public interface IEventNotificationDAO extends ITrustExDAO<EventNotification, Long>{
    List<EventNotification> findByParty(Party party);
    List<EventNotification> findByProfile(Profile profile);
}
