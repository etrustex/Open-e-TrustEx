package eu.europa.ec.etrustex.services;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author batrian
 *
 */
public interface IPartyService {

	
	/**
	 * Retrieves the party corresponding to a given id.
	 * @param id
	 * 		the id of the party to retrieve
	 * @return
	 * 		the party corresponding to the specified id  
	 */
	public Party getParty(Long id);
	
	/**
	 * Creates a new party in the database.
	 * @param party 
	 * 		the party to create
	 * 
	 * @return 
	 * 		the created party
	 * @throws NoSuchAlgorithmException 
	 */
	public Party createParty(Party party) throws NoSuchAlgorithmException;
	
	/**
	 * Updates a party in the database.
	 * @param party
	 * 		the party to update
	 * @param partyIdentifiersToDelete TODO
	 * @return
	 * 		the updated party
	 * @throws NoSuchAlgorithmException 
	 */
	public Party updateParty(Party party, List<Long> partyIdentifiersToDelete) throws NoSuchAlgorithmException;
	
	/**
	 * Deletes a party identified by its id from database.
	 * @param id
	 * 		the id of the party to delete
	 * 
	 * @throws Exception
	 */
	public boolean deleteById(Long id);
	
	/**
	 * Returns a list with all parties.
	 * @param businessDomainId TODO
	 * @return
	 */
	public List<PartyListItemDTO> getAllPartiesForBusinessDomain(Long businessDomainId);
	
	/**
	 * Returns a list with all parties that can act as third party (service provider).
	 * @param businessDomainId TODO
	 * @return
	 */
	public List<Party> getAllThirdPartiesForBusinessDomain(Long businessDomainId);
	
	/**
	 * Returns a list with all parties that act as third party (service provider) for the specified party.
	 * @return
	 */
	public List<Party> getAllThirdPartiesFor(Long partyId);
	

	
	/**
	 * Retrieve a single party based on one schemeId and id value
	 * @param schemeId (if no value is provided, GLN is used by default)
	 * @param idValue
	 * @param businessDomain the businessDomain
	 * @return the corresponding party or null if no party is found.
	 */
	public Party getPartyByID(IdentifierIssuingAgency schemeId, String idValue, BusinessDomain businessDomain);
	
	/**
	 * Checks if a party with the given name exists in the database.
	 * @param partyName
	 * @param businessDomainId TODO
	 * @return
	 * 		true if a party with the specified name exists, false otherwise
	 */
	public Boolean isUniquePartyNamePerDomain(String partyName, Long businessDomainId);
	
    List<Party> findPartiesByCriteria(String name, String username, String identifierValue, List<Long> businessDomainIds, Boolean isThirdParty);
	
	/**
	 * returns the unique party defined by the given name and business domain
	 * @param partyName
	 * @param businessDomainId
	 * @return
	 */
	public Party getPartyForBusinessDomain(String partyName, Long businessDomainId);
}
