package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

public class PartyRoleDAOTest extends AbstractEtrustExTest {

	@Autowired private IPartyRoleDao 		prDao;
	@Autowired private IPartyDAO 			partyDao;
	@Autowired private IRoleDAO 			roleDao;
	@Autowired private IBusinessDomainDAO  	bdDao;
	
	@Test public void testGetPartyRolesByPartyId(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		role1 = roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr1Bis = new PartyRole();
		pr1Bis.setParty(party1);
		pr1Bis.setRole(role2);
		prDao.create(pr1Bis);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		List<PartyRole> prList = prDao.getPartyRoles(party1.getId());
		Assert.assertEquals(2, prList.size());
		Assert.assertEquals(party1.getId(), prList.get(0).getParty().getId());
		Assert.assertEquals(party1.getId(), prList.get(1).getParty().getId());
		
		prList = prDao.getPartyRoles(party2.getId());
		Assert.assertEquals(1, prList.size());
		Assert.assertEquals(party2.getId(), prList.get(0).getParty().getId());
	} 
	
	@Test public void testGetPartiesForRoleAndDomain(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("bd-test2");
		bd2.setResponseSignatureRequired(false);
		bdDao.create(bd2);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd2);
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		role1 = roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr1Bis = new PartyRole();
		pr1Bis.setParty(party1);
		pr1Bis.setRole(role2);
		prDao.create(pr1Bis);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		List<Party> partyList = prDao.getPartiesForRoleAndDomain(role1.getId(), bd.getId());
		Assert.assertEquals(1, partyList.size());
		
		partyList = prDao.getPartiesForRoleAndDomain(role2.getId(), bd.getId());
		Assert.assertEquals(1, partyList.size());
	} 
	
	@Test public void testGetPartyRolesByPartyAndRole(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		role1 = roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr1Bis = new PartyRole();
		pr1Bis.setParty(party1);
		pr1Bis.setRole(role2);
		prDao.create(pr1Bis);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		List<PartyRole> prList = prDao.getPartyRoles(party1.getId(), role1.getId());
		Assert.assertEquals(1, prList.size());
		Assert.assertEquals(party1.getId(), prList.get(0).getParty().getId());
		Assert.assertEquals(role1.getId(), prList.get(0).getRole().getId());
		
		prList = prDao.getPartyRoles(party1.getId(), role2.getId());
		Assert.assertEquals(1, prList.size());
		Assert.assertEquals(party1.getId(), prList.get(0).getParty().getId());
		Assert.assertEquals(role2.getId(), prList.get(0).getRole().getId());
		
		
		prList = prDao.getPartyRoles(party2.getId(), role2.getId());
		Assert.assertEquals(1, prList.size());
		Assert.assertEquals(party2.getId(), prList.get(0).getParty().getId());
		Assert.assertEquals(role2.getId(), prList.get(0).getRole().getId());
		
		prList = prDao.getPartyRoles(party2.getId(), role1.getId());
		Assert.assertEquals(0, prList.size());
	} 

}
