/**
 * 
 */
package eu.europa.ec.etrustex.dao.admin;

import java.util.List;

import eu.europa.ec.etrustex.dao.ITrustExDAO;
import eu.europa.ec.etrustex.domain.admin.UserRole;

/**
 * @author batrian
 *
 */
public interface IUserRoleDAO extends ITrustExDAO<UserRole, Long> {

	public UserRole get(Long id);
	public List<UserRole> getByCode(String code); 

}
