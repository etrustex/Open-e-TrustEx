package eu.europa.ec.etrustex.services.admin.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.admin.IUserAccessRightsDAO;
import eu.europa.ec.etrustex.dao.admin.IUserDAO;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import eu.europa.ec.etrustex.services.admin.IUserService;

@Service("userService")
public class UserService implements IUserService {

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IPartyDAO partyDAO;
	
	@Autowired
	private IUserAccessRightsDAO userAccessRightsDAO;

	private static final Logger logger = LoggerFactory
			.getLogger(UserService.class.getName());

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public User getUser(Long id) {
		
		logger.info("UserService - getUser(id {}): ", id);
		
		User user = userDAO.read(id);
		return user;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<UserAccessRights> getUserAccessRights(String username) {
		
		logger.info("UserService - getUser(username {}): ", username);
		
		return getUserAccessRights(userDAO.getByName(username));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<UserAccessRights> getUserAccessRights(Long userId) {
		
		logger.info("UserService - getUser(userId {}): ", userId);
		
		return getUserAccessRights(userDAO.read(userId));
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	private List<UserAccessRights> getUserAccessRights(User user){
		
		Set<UserAccessRights> accessRights = user.getAccessRights();
		
		logger.info("found {} access rights", accessRights.size());
		
		String r = "";
		
		for (UserAccessRights rights : accessRights) {
			r += " " + (rights.getBusinessDomain() == null ? "": rights.getBusinessDomain().getName());
		}
		logger.info("User rights for user[{}]: {} ", user, r);
		
		List<UserAccessRights> result = new ArrayList<UserAccessRights>();
		
		if (CollectionUtils.isNotEmpty(accessRights)) {
			result.addAll(accessRights);
		}
		
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public UserRole getUserRole(String username, Long domainId) {

		logger.info("UserService - getUserRole(username {}, domainId {}): ", username, domainId);
		
		User user = userDAO.getByName(username); 
		Set<UserAccessRights> userAccessRights = user.getAccessRights();
		
		if (userAccessRights != null && userAccessRights.size() > 0) {
			for (UserAccessRights accesRight : userAccessRights) {
				if (accesRight.getBusinessDomain() != null 
						&& accesRight.getBusinessDomain().getId().equals(domainId)){
					return accesRight.getRole();
				}
			}
			
			if (userAccessRights.size() == 1){
				return userAccessRights.iterator().next().getRole();
			}
		}
		
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<PartyListItemDTO> getUserParties(String username, Long domainId) {
		
		logger.info("UserService - getUserParties(username {}, domainId {}): ", username, domainId);
		
		User user = userDAO.getByName(username); 
		Set<UserAccessRights> userAccessRights = user.getAccessRights();
		List<PartyListItemDTO> userParties = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(userAccessRights)) {
			for (UserAccessRights accesRight : userAccessRights) {
				if (userAccessRights.size() == 1 
						&& accesRight.getBusinessDomain() == null 
						&& StringUtils.equalsIgnoreCase(accesRight.getRole().getCode(), "adm")){//General admin for all domains
					return partyDAO.getAllPartiesForBusinessDomain(domainId);
				} 
				if (accesRight.getBusinessDomain().getId().equals(domainId)){
					if (StringUtils.equalsIgnoreCase(accesRight.getRole().getCode(), "adm")
							|| StringUtils.equalsIgnoreCase(accesRight.getRole().getCode(), "cbo")){
						return partyDAO.getAllPartiesForBusinessDomain(domainId);
					} else {
                        Party arParty = accesRight.getParty();
						userParties.add(new PartyListItemDTO(arParty.getId(), arParty.getName()));
					}
				}
			}
		}
		
		Collections.sort(userParties, new Comparator<PartyListItemDTO>() {
	        @Override
			public int compare(PartyListItemDTO o1, PartyListItemDTO o2) {
	            return o1.getName().trim().compareToIgnoreCase(o2.getName().trim());
	        }
	    });
		

		return userParties;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<BusinessDomain> getUserBusinessDomains(String username) {
		
		logger.debug("UserService - getUserBusinessDomains({})...", username);

		if (username == null){
			logger.error("UserService - getUserBusinessDomains() - Username is NULL.");
			return null;
		}
		
		return getUserBusinessDomains(userDAO.getByName(username));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<BusinessDomain> getUserBusinessDomains(Long userId) {
		
		logger.debug("UserService - getUserBusinessDomains({})...", userId);

		if (userId == null){
			logger.error("UserService - getUserBusinessDomains() - User Id is NULL.");
			return null;
		}
		
		return getUserBusinessDomains(userDAO.read(userId));
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<BusinessDomain> getUserBusinessDomains(User user){
		List<UserAccessRights> accessRights = getUserAccessRights(user);
		if (CollectionUtils.isEmpty(accessRights)) {
			logger.error("UserService - getUserBusinessDomains({}): user not authorized for a domain.", user);
			return null;
		}

		List<BusinessDomain> businessDomains = new ArrayList<BusinessDomain>();
		for (UserAccessRights rights : accessRights) {
			if (rights.getBusinessDomain() != null){
				businessDomains.add(rights.getBusinessDomain());
			}
		}

		logger.info("UserService - getUserBusinessDomains({}): found {} domains.", user, CollectionUtils.isEmpty(businessDomains) ? 0
						: businessDomains.size());
		return businessDomains;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public User getUser(String username) {
		logger.debug("UserService - getUser({})...", username);

		User user = userDAO.getByName(username);
		if (user == null) {
			logger.error("UserService - getUser({}): User not registered/authorized.", username, user);
		} else {
			logger.info("Registered user: {}", user);
		}

		return user;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public User getUser(String username, String password) {
		logger.debug("Getting user (with password): {}...", username);

		User user = userDAO.getByName(username, password);
		if (user == null) {
			logger.error("User {} not registered/authorized ({}).", username, user);
		} else {
			logger.info("Registered user: {}", user);
		}

		return user;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public User create(User user) {
		Set<UserAccessRights> uars = user.getAccessRights();
		user.setAccessRights(null);
		user = userDAO.create(user);

		//link the created user to the user access Rights and create them
		for(UserAccessRights uar : uars) {
			uar.setUser(user);
			userAccessRightsDAO.create(uar);
		}

		//return the complete object
		return user;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public User update(User modifiedUser, List<Long> userAccessRightsToDelete) {
	    String newPassword = modifiedUser.getPassword();
		User user = getUser(modifiedUser.getId());

        if (StringUtils.isNotBlank(newPassword)) {
            user.setPassword(newPassword);
        }

        // Delete UserAccessRights records if they have been removed
        for(Iterator<UserAccessRights> it =  user.getAccessRights().iterator(); it.hasNext();) {
            UserAccessRights userAccessRights = it.next();
            if (userAccessRightsToDelete.contains(userAccessRights.getId())) {
                it.remove();
            }
        }

        user.getAccessRights().clear();

        for (Iterator<UserAccessRights> it =  modifiedUser.getAccessRights().iterator(); it.hasNext();) {
            UserAccessRights userAccessRights = it.next();

            userAccessRights.setUser(user);

            // Create UserAccessRights records if they have been added
            if(userAccessRights.getId() == null) {
                userAccessRightsDAO.create(userAccessRights);
            }

            // This will update UserAccessRights records whether they have been changed or not but makes thins simplier
            user.getAccessRights().add(userAccessRights);
        }

		return userDAO.update(user);
	}

    @Override
    public User changePwd(User modifiedUser) {
        String newPassword = modifiedUser.getPassword();
        User user = getUser(modifiedUser.getId());

        if (StringUtils.isNotBlank(newPassword)) {
            user.setPassword(newPassword);
        }

        return userDAO.update(user);
    }

    @Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<User> getUsers(String username, Long userRoleId,
			Long businessDomainId, Long partyId) {
		
		return userDAO.getUsers(username, userRoleId, businessDomainId, partyId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<User> getUsers(Long userRoleId,
			Long businessDomainId, Long partyId) {
		
		return userDAO.getUsers(null, userRoleId, businessDomainId, partyId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(User user) {
		
		if (user == null){
			throw new NullPointerException("NULL User sent for deletion");
		}
		
		User usr = userDAO.read(user.getId());
		
		if (usr == null){
			throw new RecordNotFoundException("User", user.getId());
		}
		
		userDAO.delete(usr);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Boolean exists(String username) {
		User user = userDAO.getByName(username);
		return user == null ? false : true;
	}
}
