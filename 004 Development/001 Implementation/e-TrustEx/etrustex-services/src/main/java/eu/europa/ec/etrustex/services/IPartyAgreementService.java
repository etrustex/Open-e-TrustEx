/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.Collection;
import java.util.List;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Transaction;


/**
 * @author batrian
 *
 */
public interface IPartyAgreementService {

	PartyAgreement createPartyAgreement(PartyAgreement agr);

    PartyAgreement updatePartyAgreement(PartyAgreement agr);
	
	boolean existsPartyAgreement(Party authorizingParty, Party delegateParty);

    PartyAgreement findPartyAgreementById(Long id);

	PartyAgreement retrievePartyAgreement(Party authorizingParty, Party delegateParty);
	
	Boolean existsAgreementForParty(Party delegateParty);
	
	List<Party> getThirdPartiesForDelegatingParty(Long partyId);

    void deletePartyAgreement(PartyAgreement partyAgreement);

    List<PartyAgreement> findPartyAgreementsForAuthorizingParty(Party party);

    List<PartyAgreement> findPartyAgreementsByCriteria(Party authorizingParty, Party delegateParty, Transaction transaction, Long businessDomainId);

    Collection<String> getDelegateThirdPartiesNamesFor(Long partyId);
}
