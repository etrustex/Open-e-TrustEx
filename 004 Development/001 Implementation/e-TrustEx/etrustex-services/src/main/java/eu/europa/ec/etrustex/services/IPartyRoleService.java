package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.PartyRole;

public interface IPartyRoleService {
	
	public List<PartyRole> getPartyRoles(Long partyId, Long roleId);
	public PartyRole createPartyRole(PartyRole pr);
	public List<PartyListItemDTO> getPartiesByRoleAndDomain(Long roleId, Long businessDomainId);

}
