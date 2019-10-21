package eu.europa.ec.etrustex.services.admin;

import eu.europa.ec.etrustex.domain.admin.UserAccessRights;

/**
 * 
 * @author batrian
 *
 */
public interface IUserAccessRightsService {

	public UserAccessRights create(UserAccessRights userAccessRights);
	
	public void delete(UserAccessRights userAccessRights);
}
