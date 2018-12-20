package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Role;

public class RoleServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IRoleService roleService;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("RoleService");
		System.out.println("--------------------------------------------------");
	}

	@Test
	public void testCRUD() {
		System.out.println("------CRUD-and-check-----");
		Role role = new Role();
		role.setName("Test");
		role.setBidirectionalFlag(Boolean.FALSE);
		role.setCode("TTT");
		role.setTechnicalFlag(Boolean.FALSE);
		role = roleService.createRole(role);
		assertNotNull(role.getId());
		System.out.println("Role has been created : "+role);
		
		role.setTechnicalFlag(Boolean.TRUE);
		role = roleService.updateRole(role);
		assertNotEquals(role.getTechnicalFlag(), Boolean.FALSE);
		System.out.println("Role has been updated : "+role);
		
		Role r = roleService.findByCode(role.getCode());
		assertNotNull(r);
		
		r = null;
		r = roleService.getRole(role.getId());
		assertNotNull(r);
	}
	
	@Test
	public void getRolesForBusinessDomain(){
		System.out.println("------getRolesForBusinessDomain------");
		Set<Role> roles = roleService.getRolesForBusinessDomain(1L);
		assertNotNull(roles);
		assertTrue(roles.size() > 0);	
	}

}
