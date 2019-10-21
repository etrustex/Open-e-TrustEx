package eu.europa.ec.etrustex.admin.old.web.services;

import java.util.List;

import javax.xml.bind.JAXBException;

import eu.europa.ec.etrustex.admin.old.web.dto.PartyIdentifierDTO;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.domain.Party;

/**
 * @author guerrpa
 */
public interface IPartyNotificationService {
    Party createAndSendNotification(Party party, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception;

    Party updateAndSendNotification(Party party, List<Long> partyIdentifiersToDelete, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception;

    boolean deleteAndSendNotification(Long id, Long businessDomainId) throws RecordInUseException, JAXBException;
}
