package eu.europa.ec.etrustex.dao.admin;

import eu.europa.ec.etrustex.dao.ITrustExDAO;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;

import java.util.List;

/**
 * 
 * @author batrian
 *
 */
public interface IUserAccessRightsDAO extends ITrustExDAO<UserAccessRights, Long>{

	public void delete(Long id);
    List<UserAccessRights> findByPartyId(Long partyId);
	
}