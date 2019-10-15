package eu.europa.ec.etrustex.services.admin.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.admin.IUserAccessRightsDAO;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.services.admin.IUserAccessRightsService;

@Service
public class UserAccessRightsService implements IUserAccessRightsService {

	@Autowired
	private IUserAccessRightsDAO userAccessRightsDAO;
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserAccessRightsService.class.getName());

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public UserAccessRights create(UserAccessRights userAccessRights) {
		logger.info("Creating user access rights ({})", userAccessRights);
		return userAccessRightsDAO.create(userAccessRights);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(UserAccessRights userAccessRights) {
		logger.info("Deleting user access rights ({})", userAccessRights);
		userAccessRightsDAO.delete(userAccessRights);
		
	}
}
