package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;

public class PartyAgreementServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IPartyService partyService;
	@Autowired private IPartyAgreementService partyAgreementService;
	
	@Autowired private IPartyAgreementDAO partyAgreementDao; //TODO is it needed?
	
	private Party party1;
	private Party party2;
	private PartyAgreement partyAgreement;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("PartyAgreementService");
		System.out.println("--------------------------------------------------");
	}
	
	@Before
	public void initParam(){
		List<Party> parties = partyService.getAllThirdPartiesForBusinessDomain(1L);
		List<PartyAgreement> partyAgreements = partyAgreementDao.getAll();
		assertNotNull(parties);
		assertNotNull(parties);
		assertTrue(!parties.isEmpty());
		assertTrue(!partyAgreements.isEmpty());
		party1 = parties.get(0);
		party2 = parties.get(1);
		partyAgreement = partyAgreements.get(0);
	}

	@Test
	public void testFindPartyAgreementById() {
		System.out.println("------------findPartyAgreementById------------");
		PartyAgreement pAgreement = partyAgreementService.findPartyAgreementById(partyAgreement.getId());
		assertNotNull(pAgreement);
	}
	
	@Test
	public void testCRUD(){
		System.out.println("------------Create-Update-Delete------------");
		PartyAgreement partyAgreement = new PartyAgreement();
		Party authorizingParty = partyService.getParty(11667071L);
		assertNotNull(authorizingParty);
		partyAgreement.setAuthorizingParty(authorizingParty);
		partyAgreement.setDelegateParty(partyService.getParty(11777031L));
		partyAgreement = partyAgreementService.createPartyAgreement(partyAgreement);
		assertNotNull(partyAgreement.getId());
		System.out.println("------------Create------------");
		System.out.println(partyAgreement.getDelegateParty().getAccessInfo().getModificationDate());
		partyAgreement.getDelegateParty().getAccessInfo().setModificationDate(new Date());
		partyAgreement = partyAgreementService.updatePartyAgreement(partyAgreement);
		System.out.println("------------Update------------");
		System.out.println(partyAgreement.getDelegateParty().getAccessInfo().getModificationDate());
		
		partyAgreementService.deletePartyAgreement(partyAgreement);
		System.out.println("------------Delete------------");
	}
	
	@Test
	public void testFindPartyAgreementsForAuthorizingParty(){
		System.out.println("------------findPartyAgreementsForAuthorizingParty------------");
		List<PartyAgreement> list = partyAgreementService.findPartyAgreementsForAuthorizingParty(party1);
		assertNotNull(list);		
	}
	
	@Test
	public void testGetDelegateThirdPartiesNamesFor(){
		System.out.println("------------getDelegateThirdPartiesNamesFor------------");
		Collection<String> list = partyAgreementService.getDelegateThirdPartiesNamesFor(partyAgreement.getId());
		assertNotNull(list);
		
	}

}
