package eu.europa.ec.cipa.admin.web.services;

import eu.europa.ec.cipa.admin.web.dto.PartyIdentifierDTO;
import eu.europa.ec.cipa.etrustex.domain.Party;

import java.util.List;

/**
 * @author guerrpa
 *
 */
public interface IPartyNotificationService {
	Party createAndSendNotification(Party party, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception;
	Party updateAndSendNotification(Party party, List<Long> partyIdentifiersToDelete, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception;
	boolean deleteAndSendNotification(Long id, Long businessDomainId) throws Exception;
}
