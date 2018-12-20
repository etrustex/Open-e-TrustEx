/**
 * 
 */
package eu.europa.ec.etrustex.dao.admin.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.admin.IUserRoleDAO;
import eu.europa.ec.etrustex.dao.impl.TrustExDAO;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import eu.europa.ec.etrustex.domain.admin.UserRole_;

/**
 * @author batrian
 *
 */
@Repository
public class UserRoleDAO extends TrustExDAO<UserRole, Long> implements IUserRoleDAO {
	
	public List<UserRole> getByCode(String code) {
		return getListByAttribute(UserRole_.code, code);
	}

	@Override
	public UserRole get(Long id) {
		List<UserRole> userRole = getListByAttribute(UserRole_.id, id);
		if (CollectionUtils.isNotEmpty(userRole)){
			return userRole.get(0);
		}
		return null;
	}
}
