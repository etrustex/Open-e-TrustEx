package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.dao.exception.MissingImplementationException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Transaction;

import java.util.Collection;
import java.util.List;

public interface IPartyAgreementDAO extends ITrustExDAO<PartyAgreement, Long>{
	
	public PartyAgreement retrievePartyAgreement(Party authorizingParty,Party delegateParty);

	/**
	 * Checks if there is an agreement between the given parties.
	 * @param authorizingParty
	 * 		the delegating party
	 * @param delegateParty
	 * 		the delegated party
	 * @return
	 * 		true if there is an agreement between the two parties, false otherwise
	 */
	public Boolean existsPartyAgreement(Party authorizingParty, Party delegateParty);
	
	/**
	 * * Creates a party agreement between the specified authorizing and delegate parties. 
	 * @param authorizingParty
	 * 		the autorizing party
	 * @param delegateParty
	 * 		the delegate party
	 * @return
	 * 		the id of the new party agreement
	 */
	public Long create(Party authorizingParty, Party delegateParty);
	
	/**
	 * Retrieves the party agreement between the given parties identified by their ids if such agreement exists.
	 * @param authorizingPartyId
	 * @param delegatePartyId
	 * @return
	 * 		the party agreement, null if no party agreement found 
	 */
	public PartyAgreement get(Long authorizingPartyId, Long delegatePartyId)  throws MissingImplementationException ;
	
	/**
	 * Retrieves a list of delegated parties for the given party identified by its id.
	 * @param partyId
	 * 		the id of the party for which the delegate parties must be found
	 * @return
	 * 		a list with all delegate parties for the specified authorizing party
	 */
	public List<Party> getDelegateThirdPartiesFor(Long partyId) ;
	
	
	/**
	 * Checks if the party is part of an agreement.
	 * @param delegateParty the delegated party
	 * @return true if the party is part of an agreement, false otherwise
	 */
	public Boolean existsAgreementForParty(Party delegateParty);

    Collection<String> getDelegateThirdPartiesNamesFor(Long partyId);

    /**
	 * Returns the Authorised parties by a delegating party
	 * @param partyId Delegating Party
	 * @return
	 */
	public List<Party> getAuthorisedPartiesFor(Long partyId);

    List<PartyAgreement> findPartyAgreementsForParty(Party party);

    List<PartyAgreement> findPartyAgreementsForAuthorizingParty(Party party);

    List<PartyAgreement> findPartyAgreementsByCriteria(Party authorizingParty, Party delegateParty, Transaction transaction, Long businessDomainId);
    
    /**
     * checks if a party agreement exists for the given parties and the given transaction
     * @param authorizingParty
     * @param delegateParty
     * @param transactionId
     * @return true if a party agreement exists, false otherwise
     */
    public Boolean existsPartyAgreementForTransaction(Party authorizingParty, Party delegateParty, Long transactionId);
}
