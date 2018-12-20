package eu.europa.ec.etrustex.services;

import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;

public interface IInterchangeAgreementService {

	public List<InterchangeAgreement> getInterchangeAgreements(Party requester, Set<Party> senders, Set<Party> receivers, List<String> documentTypeIds);
	
	public List<InterchangeAgreement> getInterchangeAgreements(Party requester, Party party1, Party party2, List<String> documentTypeIds);
	
	public List<InterchangeAgreement> getInterchangeAgreements(PartyRole a, PartyRole b, Profile p, CIALevel l);

	public InterchangeAgreement getInterchangeArgreement(Long icaID);
	
	/**
	 * @author batrian
	 * @param ica
	 * @return
	 */
	public InterchangeAgreement createInterchangeAgreement(InterchangeAgreement ica);
	public InterchangeAgreement createInterchangeAgreement(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation);
	public InterchangeAgreement updateInterchangeAgreement(InterchangeAgreement ica);
	public InterchangeAgreement updateInterchangeAgreement(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation);
	public boolean deleteInterchangeAgreement(InterchangeAgreement ica);
    List<InterchangeAgreement> findInterchangeAgreementsByCriteria(ICASearchDTO dto);
	public List<InterchangeAgreement> getInterchangeAgreementsByProfile(Long profileId);
	/*public List<Long> listUnavailableParties(Long profileId, Long roleId, Long firsPartyId);*/
	public List<Long> getPartiesInInterchangeAgreementFor(Long profileId, Long firstPartyRoleId, Long firsPartyId, Long secondPartyRoleId);
	public boolean deleteInterchangeAgreementById(Long icaId);
	
	/**
	 * Checks if the given interchange agreement is unique by verifying the following information:
	 *  - the profile 
	 *  - the first party and its role
	 *  - the second party and its role
	 * 		true if there is no other interchange agreement with the same values for the fields in the business rule
	 * 		false if exists an interchange agreement having same values for the fields in the business rule
	 */
	public boolean isUnique(Long profileId, Long firstPartyId, Long firstPartyRoleId, Long secondPartyId, Long secondPartyRoleId);
	public Boolean containsParty(Party party);
	public Boolean existICAwithSameRoleForParties(Role role);
	
	/**
	 * Checks if the interchange agreement identified by the given id is used in other tables.
	 * @param id
	 * 		the id of the interchange agreement
	 * @return
	 * 		false, if no reference is found
	 * 		true, if there are other tables using this interchange agreement
	 */
	public Boolean isUsed(Long id);
    List<InterchangeAgreement> findByParty(Party party);
    List<PartyListItemDTO> getPartiesInExistingAgreement(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId);
	public List<PartyListItemDTO> getSecondParties(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId, Long businessDomainId);
}
