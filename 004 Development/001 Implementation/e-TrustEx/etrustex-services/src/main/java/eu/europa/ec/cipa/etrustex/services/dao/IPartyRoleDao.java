/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;

import java.util.List;

/**
 * @author batrian
 *
 */
public interface IPartyRoleDao extends ITrustExDAO<PartyRole, Long> {

    List<PartyRole> getPartyRoles(Long partyId);

	public List<PartyRole> getPartyRoles(Long partyId, Long roleId);

	public List<PartyListItemDTO>	getPartiesForRoleAndDomain(Long roleId, Long businessDomainId);
}
