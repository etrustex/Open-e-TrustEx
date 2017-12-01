package eu.europa.ec.cipa.etrustex.services.admin;

import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;

/**
 * 
 * @author batrian
 *
 */
public interface IUserAccessRightsService {

	public UserAccessRights create(UserAccessRights userAccessRights);
	
	public void delete(UserAccessRights userAccessRights);
}
