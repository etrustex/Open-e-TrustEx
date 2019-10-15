package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;

public class RoleDaoTest extends AbstractEtrustExTest {

	public static String ROLE_CODE_1 = "CODE1";
	public static String ROLE_CODE_2 = "CODE2";
	public static String ROLE_NAME_1 = "NAME1";
	public static String ROLE_NAME_2 = "NAME2";
	
	private Role role1;
	private Role role2;
	
	@Autowired private IRoleDAO 	   roleDao;
	@Autowired private ITransactionDAO transactionDao;
	@Autowired private IPartyRoleDao   partyRoleDao;
	
	@Before public void prepareRoles(){
		role1 = new Role();
		role1.setCode(ROLE_CODE_1);
		role1.setName(ROLE_NAME_1);
		role1.setTechnicalFlag(Boolean.TRUE);
		
		role2 = new Role();
		role2.setCode(ROLE_CODE_2);
		role2.setName(ROLE_NAME_2);
		role2.setTechnicalFlag(Boolean.FALSE);
		
		roleDao.create(role1);
		roleDao.create(role2);
		flush();
	}
	
	@Test public void testCrud(){
		role1.setName("UPDATED_ROLE");
		roleDao.update(role1);
		flush();
		Assert.assertEquals("Role not Updated", "UPDATED_ROLE", roleDao.read(role1.getId()).getName());
		roleDao.delete(role1.getId());
		Assert.assertNull("Role Not Deleted", roleDao.read(role1.getId()));
	}
	
	
	@Test(expected=DataIntegrityViolationException.class) public void testUniqueCode(){
		Assert.assertEquals(true, roleDao.isUniqueCode(role1));
		Role role = new Role();
		role.setCode(ROLE_CODE_1);
		role.setName("DUMB_NAME");
		role.setTechnicalFlag(Boolean.TRUE);		
		roleDao.create(role);
		roleDao.isUniqueCode(role1);
	}
	
	@Test(expected=DataIntegrityViolationException.class) public void testUniqueRoleName(){
		Assert.assertEquals(true, roleDao.isUniqueName(role1));
		Role role = new Role();
		role.setCode("DUMB_CODE");
		role.setName(ROLE_NAME_1);
		role.setTechnicalFlag(Boolean.TRUE);		
		roleDao.create(role);
		roleDao.isUniqueName(role1);
	}
	
	@Test public void testInUse(){
		Assert.assertEquals(false, roleDao.isInUse(role1));
		Assert.assertEquals(false, roleDao.isInUse(role2));
		
		Transaction tx = new Transaction();
		tx.setSenderRole(role1);
		tx.setName("TxName");
		tx.setNamespace("namespace");
		transactionDao.create(tx);
		flush();
		Assert.assertEquals(true, roleDao.isInUse(role1));
		
		PartyRole pr = new PartyRole();
		pr.setRole(role2);
		partyRoleDao.create(pr);
		flush();
		Assert.assertEquals(true, roleDao.isInUse(role2));
	}
	
	@Test public void testUnique(){
		Assert.assertEquals(false, roleDao.isUnique(role1));
		
		Role role = new Role();
		role.setCode("dummy");
		role.setName("dummy");
		
		Assert.assertEquals(true, roleDao.isUnique(role));
	}
	
	@Test public void testFindByCode(){
		Assert.assertEquals(role1.getId(), roleDao.findByCode(role1.getCode()).getId());
		
		Assert.assertEquals(null, roleDao.findByCode("dummy"));
	}
	
	@Test public void testGetAllNonTechnical(){
		List<Role> roles = roleDao.getAllNonTechnical();
		for (Role role : roles) {
			if(role.getTechnicalFlag()){
				Assert.fail("A Role with technical flag returned");
			}
		}
	}
	
	@Test public void testGetAll(){
		List<Role> roles = roleDao.getAll();
		Assert.assertTrue(roles.size() >= 2);
	}
	
	//TODO
//	@Test public void testGetRolesForProfile(){
//		
//		PartyRole pr = new PartyRole();
//		pr.setRole(role2);
//		pr.set
//		partyRoleDao.create(pr);
//		
//		List<Role> roles = roleDao.getRolesForProfile(profileId, technical);
//		
//	}
}
