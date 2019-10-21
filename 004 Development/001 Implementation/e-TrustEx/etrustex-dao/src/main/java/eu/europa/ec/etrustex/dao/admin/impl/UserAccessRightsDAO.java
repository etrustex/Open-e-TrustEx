/**
 * 
 */
package eu.europa.ec.etrustex.dao.admin.impl;

import eu.europa.ec.etrustex.dao.admin.IUserAccessRightsDAO;
import eu.europa.ec.etrustex.dao.impl.TrustExDAO;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author batrian
 *
 */
@Repository
public class UserAccessRightsDAO extends TrustExDAO<UserAccessRights, Long> implements IUserAccessRightsDAO {


	public void delete(Long id) {
		UserAccessRights uar = read(id);
		super.delete(uar);
	}

    @Override
    public List<UserAccessRights> findByPartyId(Long partyId) {
        return entityManager.createQuery("from UserAccessRights uar where uar.party.id = :partyId", UserAccessRights.class)
                .setParameter("partyId", partyId).getResultList();
    }

    @Override
	public UserAccessRights update(UserAccessRights userAccessRights){
		if(userAccessRights.getUser() != null 
				&& !entityManager.contains(userAccessRights.getUser())) {
			userAccessRights.setUser(entityManager.merge(userAccessRights.getUser()));
		}
		
		return super.update(userAccessRights);
	}
}
