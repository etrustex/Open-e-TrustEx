/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.Transaction;

import java.util.List;


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
}
