/**
 * 
 */
package eu.europa.ec.etrustex.services.admin;

import java.util.List;

import eu.europa.ec.etrustex.domain.admin.UserRole;


/**
 * @author batrian
 *
 */
public interface IUserRoleService {

	public UserRole getRole(Long id);
	public List<UserRole> getByCode(String code);
	public List<UserRole> getAll();
	
}
