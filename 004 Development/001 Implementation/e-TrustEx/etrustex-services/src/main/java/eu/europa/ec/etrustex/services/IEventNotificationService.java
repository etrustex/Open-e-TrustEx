package eu.europa.ec.etrustex.services;

import java.util.List;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.types.EventNotificationType;

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
