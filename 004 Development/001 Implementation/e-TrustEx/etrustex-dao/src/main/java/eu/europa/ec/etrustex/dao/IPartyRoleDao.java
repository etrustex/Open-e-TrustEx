/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;

import java.util.List;

/**
 * @author batrian
 *
 */
public interface IPartyRoleDao extends ITrustExDAO<PartyRole, Long> {

    List<PartyRole> getPartyRoles(Long partyId);

	List<PartyRole> getPartyRoles(Long partyId, Long roleId);

	List<Party>	getPartiesForRoleAndDomain(Long roleId, Long businessDomainId);
}
