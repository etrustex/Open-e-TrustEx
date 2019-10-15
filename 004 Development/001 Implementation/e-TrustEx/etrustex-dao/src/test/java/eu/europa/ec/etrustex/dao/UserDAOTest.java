package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.admin.IUserDAO;
import eu.europa.ec.etrustex.dao.admin.IUserRoleDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserRole;

public class UserDAOTest extends AbstractEtrustExTest {
	
	@Autowired private IUserDAO 			userDAO;
	@Autowired private IUserRoleDAO 		userRoleDAO;
	@Autowired private IBusinessDomainDAO	bdDao;
	@Autowired private IPartyDAO			partyDao;
	
	private User 	 user1 = null;
	private UserAccessRights uar = null;
	
	
	@Before public void init(){
		user1 = new User();
		user1.setName("XtestXuserX1");
		
		uar = new UserAccessRights();
		
		UserRole userRole1 = new UserRole();
		userRole1.setCode("CODE_1");
		userRole1.setDescription("CODE_1");
		userRoleDAO.create(userRole1);
		uar.setRole(userRole1);
		
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("UserDAOTestBD");
		bd1.setResponseSignatureRequired(true);
		bdDao.create(bd1);
		uar.setBusinessDomain(bd1);
		
		Party party = new Party();
		party.setName("P");
		partyDao.create(party);
		uar.setParty(party);
		
		uar.setUser(user1);
		user1.getAccessRights().add(uar);		
		user1 = userDAO.create(user1);		
	}
	
	@Test public void testgetUsers(){
		
		//Testing by UserName
		List<User> result = userDAO.getUsers("  XtestXuserX1  ", null, null, null);
		Assert.assertEquals(1, result.size());
		result = userDAO.getUsers("  STXus  ", null, null, null);
		Assert.assertEquals(1, result.size());
		result = userDAO.getUsers("  random  ", null, null, null);
		Assert.assertEquals(0, result.size());
		
		//User Role
		result = userDAO.getUsers(null, uar.getRole().getId(), null, null);
		Assert.assertEquals(1, result.size());		
		result = userDAO.getUsers(null, -5555L, null, null);
		Assert.assertEquals(0, result.size());
		
		//Party Id
		result = userDAO.getUsers(null, null, null, uar.getParty().getId());
		Assert.assertEquals(1, result.size());
		result = userDAO.getUsers(null, null, null, -5555L);
		Assert.assertEquals(0, result.size());
		
		//Business Domain
		result = userDAO.getUsers(null, null, uar.getBusinessDomain().getId(), null);
		Assert.assertEquals(1, result.size());
		result = userDAO.getUsers(null, null, -5555L, null);
		Assert.assertEquals(0, result.size());
		
		result = userDAO.getUsers("  XtestXuserX1  ", uar.getRole().getId(), uar.getBusinessDomain().getId(), uar.getParty().getId());
		Assert.assertEquals(1, result.size());
	}
}
