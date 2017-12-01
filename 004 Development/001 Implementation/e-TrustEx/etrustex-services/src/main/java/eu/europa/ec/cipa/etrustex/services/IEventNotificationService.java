package eu.europa.ec.cipa.etrustex.services;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;

import java.util.List;

/**
 * Created by guerrpa on 18/09/2015.
 */
public interface IEventNotificationService {
    EventType getCreatePartyEventNotification(Party party);
    EventType getUpdatePartyEventNotification(Party fromParty, Party toParty);
    EventType getDeletePartyEventNotification(Party party);
    EventType getCreateInterchangeAgreementyEventNotification(InterchangeAgreement ica);
    EventType getUpdateInterchangeAgreementyEventNotification(InterchangeAgreement fromIca, InterchangeAgreement toIca);
    EventType getDeleteInterchangeAgreementyEventNotification(InterchangeAgreement ica);
    EventType getDeleteDocumentEventNotification(Message message);
    PartyIdentifier getSenderId(Party receiverParty);
    List<Party> getPartiesConfiguredToReceiveEventNotification(EventNotificationType eventType, Long businessDomainId, Long profileId);
}
