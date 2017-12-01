package eu.europa.ec.cipa.etrustex.services.dao.admin;

import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.services.dao.ITrustExDAO;

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