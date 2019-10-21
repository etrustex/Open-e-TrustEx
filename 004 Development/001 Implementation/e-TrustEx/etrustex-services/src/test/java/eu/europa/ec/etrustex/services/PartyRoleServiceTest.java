package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyRoleDao;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.PartyRole;

public class PartyRoleServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IPartyRoleService partyRoleService;
	@Mock      private IPartyRoleDao partyRoleDAO;     
	@Mock      private IPartyAgreementDAO partyAgreementDAO; 
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("PartyRoleService");
		System.out.println("--------------------------------------------------");
	}
	
	@Test
	public void testGetPartiesByRoleAndDomain() {
		System.out.println("------------getPartiesByRoleAndDomain------------");
		List<PartyListItemDTO> list = partyRoleService.getPartiesByRoleAndDomain(null, 1L);
		assertNotNull(list);
		//		assertTrue(list.size() > 0);
	}
	
	@Test
	public void testGetPartyRoles(){
		System.out.println("------------getPartyRoles------------");
		List<PartyRole> list = partyRoleService.getPartyRoles(11667048L, null);
		assertNotNull(list);
	}
	
//	@Test
//	public void testCreate(){
//		System.out.println("------------createPartyRole------------");
//		PartyRole partyRole = new PartyRole();
//		Party party = partyService.getParty(Long.valueOf(prop.getProperty("party.id")));
//		partyRole.setParty(party);
//		partyRole = partyRoleService.createPartyRole(partyRole);
//		assertNotNull(partyRole.getId());
//		System.out.println("party role :"+partyRole.getId());
//	}

}
