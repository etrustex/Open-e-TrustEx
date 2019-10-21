/**
 * 
 */
package eu.europa.ec.etrustex.services.admin.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.admin.IUserRoleDAO;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import eu.europa.ec.etrustex.services.admin.IUserRoleService;

/**
 * @author batrian
 *
 */
@Service
public class UserRoleService implements IUserRoleService {

	
	@Autowired
	private IUserRoleDAO userRoleDao;
	
	
	private static final Logger logger = LoggerFactory
	.getLogger(UserRoleService.class);
	
	
	/* (non-Javadoc)
	 * @see eu.europa.ec.etrustex.services.IUserRoleService#getAll()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<UserRole> getAll() {
		logger.info("UserRoleService - getAll()");
		return userRoleDao.getAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<UserRole> getByCode(String code) {
		logger.info("UserRoleService - getByCode({})", code);
		return userRoleDao.getByCode(code);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public UserRole getRole(Long id) {
		logger.info("UserRoleService - getRole({})", id);
		return userRoleDao.get(id);
	}

}
