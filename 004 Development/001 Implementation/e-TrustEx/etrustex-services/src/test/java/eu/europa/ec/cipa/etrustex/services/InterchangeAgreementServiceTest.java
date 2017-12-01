package eu.europa.ec.cipa.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.dao.ICIALevelDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IInterchangeAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyRoleDao;
import eu.europa.ec.cipa.etrustex.services.dao.IProfileDAO;

public class InterchangeAgreementServiceTest {

	@InjectMocks
	private InterchangeAgreementService interchangeService;

	@Mock
	private IInterchangeAgreementDAO interchangeAgreementDAO;

	@Mock
	private IPartyRoleDao partyRoleDAO;

	@Mock
	private IPartyAgreementDAO partyAgrDAO;

	@Mock
	private IPartyDAO partyDAO;

	@Mock
	private ICIALevelDAO icaLevelDAO;

	@Mock
	private IProfileDAO profileDAO;

	private EntityAccessInfo acc;
	private Role role1, role2;
	private PartyRole pRole1, pRole2;
	private Set<PartyRole> roles;
	private Profile prof;
	private CIALevel level, level2;
	private PartyAgreement pAG1, pAG2;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// general
		acc = new EntityAccessInfo();
		acc.setCreationId("testing");
		acc.setModificationDate(new Date());
		acc.setModificationId("testing");
		acc.setCreationDate(new Date());

		level2 = new CIALevel();
		level2.setAccessInfo(acc);
		level2.setAvailabilityLevel(0);
		level2.setConfidentialityLevel(0);
		level2.setIntegrityLevel(0);
		
		ArrayList<CIALevel> levels= new ArrayList<CIALevel>();
		levels.add(level2);
		
		level = new CIALevel();
		level.setAccessInfo(acc);
		level.setAvailabilityLevel(0);
		level.setConfidentialityLevel(0);
		level.setIntegrityLevel(0);

		Party par1 = new Party();
		par1.setId(new Long(1));
		par1.setAccessInfo(acc);

		Party par2 = new Party();
		par2.setId(new Long(2));
		par2.setAccessInfo(acc);

		Party par3 = new Party();
		par3.setId(new Long(3));
		par3.setAccessInfo(acc);

		role1 = new Role();
		role1.setAccessInfo(acc);
		role1.setCode("R1");
		role1.setId(new Long(1));
		role1.setName("role1");

		role2 = new Role();
		role2.setAccessInfo(acc);
		role2.setCode("R2");
		role2.setId(new Long(2));
		role2.setName("role2");

		pRole1 = new PartyRole();
		pRole1.setAccessInfo(acc);
		pRole1.setRole(role1);
		pRole1.setId(new Long(1));
		pRole1.setParty(par1);

		pRole2 = new PartyRole();
		pRole2.setAccessInfo(acc);
		pRole2.setRole(role2);
		pRole2.setId(new Long(2));
		pRole2.setParty(par2);

		roles = new HashSet<PartyRole>();
		roles.add(pRole1);
		roles.add(pRole2);

		ArrayList<PartyRole> lRoles1 = new ArrayList<PartyRole>();
		lRoles1.add(pRole1);

		ArrayList<PartyRole> lRoles2 = new ArrayList<PartyRole>();
		lRoles2.add(pRole2);

		Transaction t = new Transaction();
		t.setId(new Long(1));
		t.setName("test");

		Set<Transaction> transactions = new HashSet<Transaction>();
		transactions.add(t);

		BusinessDomain domain = new BusinessDomain();
		domain.setAccessInfo(acc);
		domain.setId(new Long(1));
		domain.setName("test2");

		Set<BusinessDomain> domains = new HashSet<BusinessDomain>();
		domains.add(domain);

		pAG1 = new PartyAgreement();
		pAG1.setAuthorizingParty(par1);
		pAG1.setDelegateParty(par2);
		pAG1.setTransactions(transactions);

		pAG2 = new PartyAgreement();
		pAG2.setAuthorizingParty(par1);
		pAG2.setDelegateParty(par3);
		pAG2.setTransactions(transactions);

		prof = new Profile();
		prof.setAccessInfo(acc);
		prof.setTransactions(transactions);
		prof.setName("test");
		prof.setBusinessDomains(domains);
		prof.setCiaLevel(level);

		InterchangeAgreement agr1 = new InterchangeAgreement();
		agr1.setId(new Long(1));
		agr1.setAccessInfo(acc);
		agr1.setPartyRoles(roles);
		agr1.setProfile(prof);
		agr1.setCiaLevel(level);

		Mockito.when(partyAgrDAO.existsPartyAgreement(par1, par2)).thenReturn(true);
		Mockito.when(partyAgrDAO.existsPartyAgreement(par1, par3)).thenReturn(true);
		Mockito.when(partyRoleDAO.getPartyRoles(new Long(1), new Long(1))).thenReturn(lRoles1);
		Mockito.when(partyRoleDAO.getPartyRoles(new Long(2), new Long(2))).thenReturn(lRoles2);

		Mockito.when(interchangeAgreementDAO.update(Mockito.any(InterchangeAgreement.class))).thenReturn(agr1);
		Mockito.when(interchangeAgreementDAO.create(Mockito.any(InterchangeAgreement.class))).thenReturn(agr1);
		Mockito.when(interchangeAgreementDAO.read(Mockito.anyLong())).thenReturn(agr1);
		Mockito.when(icaLevelDAO.retrieveCIALevel(0, 0, 0)).thenReturn(levels);
	}

	@Test
	public void testCreateInterchangeAgreementInterchangeAgreementPartyAgreementPartyAgreement() {
		InterchangeAgreement agr2 = createBasicInterchangeAgr();
		InterchangeAgreement agr3 = interchangeService.createInterchangeAgreement(agr2, pAG1, pAG2);
		compareInterChangAgrEquals(agr3, agr2);
		agr2 = updateInterchangeAgr(agr2);
		agr3 = interchangeService.createInterchangeAgreement(agr2, pAG1, pAG2);
		assertNotSame(agr2.getProfile().getTransactions().size(), agr3.getProfile().getTransactions().size());
		Mockito.verify(interchangeAgreementDAO, VerificationModeFactory.times(2)).create(Mockito.any(InterchangeAgreement.class));
		Mockito.verify(partyAgrDAO, VerificationModeFactory.times(4)).existsPartyAgreement(Mockito.any(Party.class), Mockito.any(Party.class));
		Mockito.verify(partyRoleDAO, VerificationModeFactory.times(4)).getPartyRoles(Mockito.any(Long.class), Mockito.any(Long.class));
	}
	
	@Test
	public void testCreateInterchangeAgreement()
	{
		InterchangeAgreement agr2 = createBasicInterchangeAgr();
		InterchangeAgreement agr4 = interchangeService.createInterchangeAgreement(agr2);
		compareInterChangAgrEquals(agr4, agr2);
	}

	@Test
	public void testUpdateInterchangeAgreementInterchangeAgreementPartyAgreementPartyAgreement() {
		InterchangeAgreement agr2 = createBasicInterchangeAgr();
		InterchangeAgreement agr3 = interchangeService.updateInterchangeAgreement(agr2, pAG1, pAG2);
		compareInterChangAgrEquals(agr3, agr2);
		
		agr2 = updateInterchangeAgr(agr2);
		agr3 = interchangeService.updateInterchangeAgreement(agr2, pAG1, pAG2);
		assertNotSame(agr2.getProfile().getTransactions().size(), agr3.getProfile().getTransactions().size());
		
		
		agr2.setCiaLevel(null);
		agr3 = interchangeService.updateInterchangeAgreement(agr2, pAG1, pAG2);
		compareInterChangAgrEquals(agr3, agr2);
		
		agr2.setCiaLevel(null);
		agr2.getProfile().setCiaLevel(null);
		agr3 = interchangeService.updateInterchangeAgreement(agr2, pAG1, pAG2);
		compareInterChangAgrEquals(agr3, agr2);
		
		Mockito.when(icaLevelDAO.retrieveCIALevel(0, 0, 0)).thenReturn(null);
		agr2.setCiaLevel(null);
		agr2.getProfile().setCiaLevel(null);
		agr3 = interchangeService.updateInterchangeAgreement(agr2, pAG1, pAG2);
		compareInterChangAgrEquals(agr3, agr2);
		
		Mockito.verify(interchangeAgreementDAO, VerificationModeFactory.times(5)).update(Mockito.any(InterchangeAgreement.class));
		Mockito.verify(partyAgrDAO, VerificationModeFactory.times(10)).existsPartyAgreement(Mockito.any(Party.class), Mockito.any(Party.class));
		Mockito.verify(partyRoleDAO, VerificationModeFactory.times(10)).getPartyRoles(Mockito.any(Long.class), Mockito.any(Long.class));
	}
	
	@Test
	public void testUpdateInterchangeAgreement() {
		InterchangeAgreement agr2 = createBasicInterchangeAgr();
		InterchangeAgreement agr3 = interchangeService.updateInterchangeAgreement(agr2);
		compareInterChangAgrEquals(agr3, agr2);
		Mockito.verify(interchangeAgreementDAO, VerificationModeFactory.times(1)).update(Mockito.any(InterchangeAgreement.class));
	}
	
	@Test
	public void testGetInterchangeAgreement(){
		InterchangeAgreement agr = createBasicInterchangeAgr();
		InterchangeAgreement agr2 = interchangeService.getInterchangeArgreement(new Long(1));
		compareInterChangAgrEquals(agr, agr2);
		Mockito.verify(interchangeAgreementDAO, VerificationModeFactory.times(1)).read(Mockito.anyLong());
	}

	@Ignore
	@Test
	public void testIsUnique() {
		fail("Not yet implemented");
	}
	
	@After
	public void resetMockito() {
		Mockito.reset(interchangeAgreementDAO);
		Mockito.reset(partyAgrDAO);
		Mockito.reset(partyRoleDAO);
	}

	public InterchangeAgreement createBasicInterchangeAgr() {
		InterchangeAgreement agr = new InterchangeAgreement();
		agr.setId(new Long(1));
		agr.setAccessInfo(acc);
		agr.setPartyRoles(roles);
		agr.setProfile(prof);
		agr.setCiaLevel(level);
		return agr;
	}

	private void compareInterChangAgrEquals(InterchangeAgreement agr1, InterchangeAgreement agr2) {
		assertEquals(agr1.getId(), agr2.getId());
		assertEquals(agr1.getCiaLevel(), agr2.getCiaLevel());
		assertEquals(agr1.getAccessInfo(), agr2.getAccessInfo());
		assertEquals(agr1.getProfile(), agr2.getProfile());
		assertEquals(agr1.getPartyRoles(), agr2.getPartyRoles());
	}

	private InterchangeAgreement updateInterchangeAgr(InterchangeAgreement agr) {
		Transaction t = new Transaction();
		t.setId(new Long(1));
		t.setName("test");

		Transaction t2 = new Transaction();
		t2.setId(new Long(2));
		t2.setName("test2");

		Set<Transaction> transactions = new HashSet<Transaction>();
		transactions.add(t);
		transactions.add(t2);

		BusinessDomain domain = new BusinessDomain();
		domain.setAccessInfo(acc);
		domain.setId(new Long(1));
		domain.setName("test2");

		Set<BusinessDomain> domains = new HashSet<BusinessDomain>();
		domains.add(domain);

		Profile prof2 = new Profile();
		prof2.setAccessInfo(acc);
		prof2.setTransactions(transactions);
		prof2.setName("test");
		prof2.setBusinessDomains(domains);
		prof2.setCiaLevel(level);
		agr.setProfile(prof2);

		return agr;
	}
}
