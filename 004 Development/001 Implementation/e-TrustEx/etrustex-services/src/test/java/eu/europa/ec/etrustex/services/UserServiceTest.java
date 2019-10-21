/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.admin.IUserAccessRightsDAO;
import eu.europa.ec.etrustex.dao.admin.IUserDAO;
import eu.europa.ec.etrustex.dao.admin.IUserRoleDAO;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.admin.impl.UserService;

/**
 * @author batrian
 *
 */

public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private IUserDAO userDAO;
	
	@Mock
	private IUserRoleDAO userRoleDAO;
	
	@Mock
	private IBusinessDomainDAO businessDomainDAO;
	
	@Mock
	private IPartyDAO partyDAO;
	
	@Mock
	private IUserAccessRightsDAO userAccessRightsDAOk;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		EntityAccessInfo acc = new EntityAccessInfo();
		acc.setCreationId("testing");
		acc.setModificationDate(new Date());
		acc.setModificationId("testing");
		acc.setCreationDate(new Date());

		User usr = new User();
		usr.setAccessInfo(acc);
		usr.setId(new Long(1));
		usr.setName("JunitTestUser");
		usr.setPassword("JunitPassword");

		// Mockito.when(userService.getUser(new Long(1))).thenReturn(usr);
		Mockito.when(userDAO.getByName(new String("JunitTestUser"))).thenReturn(usr);
		Mockito.when(userDAO.read(new Long(1))).thenReturn(usr);
	}

	@Test
	public void testGetUserById() {
		long start = System.currentTimeMillis();

		User user = userService.getUser(new Long(1));
		Assert.assertNotNull(user);
		Assert.assertEquals(new Long(1), user.getId());
		Assert.assertEquals("JunitTestUser", user.getName());

		user = userService.getUser(new Long(10));
		Assert.assertNull(user);

		System.out.println("UserServiceTest testGetUserById took " + (System.currentTimeMillis() - start) + "ms.");
	}

}
