/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao.admin;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.admin.UserRole;
import eu.europa.ec.cipa.etrustex.services.dao.ITrustExDAO;

/**
 * @author batrian
 *
 */
public interface IUserRoleDAO extends ITrustExDAO<UserRole, Long> {

	public UserRole get(Long id);
	public List<UserRole> getByCode(String code); 

}
