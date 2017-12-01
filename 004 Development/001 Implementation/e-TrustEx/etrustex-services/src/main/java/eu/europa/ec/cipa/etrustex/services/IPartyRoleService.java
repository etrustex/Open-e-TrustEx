package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;

import java.util.List;

public interface IPartyRoleService {
	
	public List<PartyRole> getPartyRoles(Long partyId, Long roleId);
	public PartyRole createPartyRole(PartyRole pr);
	public List<PartyListItemDTO> getPartiesByRoleAndDomain(Long roleId, Long businessDomainId);

}
