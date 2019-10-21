/**
 * 
 */
package eu.europa.ec.etrustex.dao.impl;

import eu.europa.ec.etrustex.dao.IPartyRoleDao;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
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
	public List<Party> getPartiesForRoleAndDomain(Long roleId, Long businessDomainId) {
		return entityManager
				.createQuery(
						"select pr.party from PartyRole pr " +
								"join pr.party pty " +
                                "where pr.role.id = :roleId " +
                                "and pty.businessDomain.id = :businessDomainId " +
                                "order by UPPER(pty.name)",
                        Party.class)
				.setParameter("businessDomainId", businessDomainId)
				.setParameter("roleId", roleId).getResultList();
	}

}
