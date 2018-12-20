package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IIdentifierIssuingAgency;

import java.util.List;

public interface IPartyDAO extends ITrustExDAO<Party, Long> {
	
	public Party getParty(IIdentifierIssuingAgency shemeId, String partyId, BusinessDomain bd);
	
	public Party getMessageIssuer(String authenticatedUser);
	
	/**
	 * @author batrian
	 * Retrieves a list with all parties corresponding to a specified business domain.
	 * @param businessDomainId
	 * 		the business domain for which the parties needs to be retrieved
	 * @return
	 * 		list with all parties pertaining to the specified business domain
	 */
	public List<PartyListItemDTO> getAllPartiesForBusinessDomain(Long businessDomainId);
	
	/**
	 * @author batrian
	 * Retrieves the party corresponding to the specified name.
	 * @param name
	 * 		the name of the party to retrieve
	 * @param businessDomainId 
	 * 		the business domain for which the party needs to be retrieved
	 * @return
	 * 		the party corresponding to the given name
	 */
	public Party getPartyForBusinessDomain(String name, Long businessDomainId);
	
	/**
	 * @author batrian
	 * Retrieves a list with all parties that can act as third parties (service providers) in the context of the .
	 * @param businessDomainId 
	 * 		the business domain for which the parties needs to be retrieved
	 * @return
	 * 		list with all parties having a third party role
	 */
	public List<Party> getThirdPartiesForBusinessDomain(Long businessDomainId);

    /**
	 * @author batrian
	 * Deletes a party identified by its id.
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * @author batrian
	 * Creates a party and returns it.
	 */
	public Party create(Party party);
	
	public List<Party> getPartiesForBusinessDomain(String name, String username, String identifierValue, List<Long> businessDomainIds);

    List<Party> findPartiesByCriteria(String name, String username, String identifierValue, List<Long> businessDomainIds, Boolean isThirdParty);

    public Boolean isUniquePartyPerBusinessDomain(String name, Long businessDomainId);
	
	public List<Party> getPartiesConfiguredToReceiveEventNotification(EventNotificationType eventType, Long businessDomainId, Long profileId);

	public Party getEventPublisherParty(Party confSubscriberParty);

    Party getEtrustexParty(Long businessDomainId);

    void flushEm();
}
