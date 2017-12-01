/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author batrian
 *
 */
@Repository
public class PartyRoleDao extends TrustExDAO<PartyRole, Long> implements IPartyRoleDao {

    @Override
    public List<PartyRole> getPartyRoles(Long partyId) {
        return entityManager
                .createQuery("select pr from PartyRole pr where pr.party.id= :partyId", PartyRole.class)
                .setParameter("partyId", partyId).getResultList();
    }

	@Override
	public List<PartyRole> getPartyRoles(Long partyId, Long roleId) {

		return entityManager
				.createQuery("select pr from PartyRole pr where pr.party.id= :partyId and pr.role.id = :roleId",PartyRole.class)
				.setParameter("partyId", partyId)
				.setParameter("roleId", roleId).getResultList();
	}

	@Override
	public List<PartyListItemDTO> getPartiesForRoleAndDomain(Long roleId, Long businessDomainId) {
		return entityManager
				.createQuery(
						"select new eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO(pr.party.id, pr.party.name) from PartyRole pr " +
                                "where pr.role.id = :roleId " +
                                "and pr.party.businessDomain.id = :businessDomainId " +
                                "order by UPPER(pr.party.name)",
                        PartyListItemDTO.class)
				.setParameter("businessDomainId", businessDomainId)
				.setParameter("roleId", roleId).getResultList();
	}

}
