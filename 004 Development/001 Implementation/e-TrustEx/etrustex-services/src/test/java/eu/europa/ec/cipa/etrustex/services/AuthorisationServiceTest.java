package eu.europa.ec.cipa.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.jexl.junit.Asserter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.dao.IBusinessDomainDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ICertificateDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ICredentialsDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IInterchangeAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyIdentifierDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IServiceEndpointDao;
import eu.europa.ec.cipa.etrustex.services.dao.ITransactionDAO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.services.util.EncryptionService;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;

public class AuthorisationServiceTest {
	@InjectMocks
	private AuthorisationService authorisationService;

	@InjectMocks
	private PartyService partyService;

	@InjectMocks
	private EncryptionService encryptionService;

	@Mock
	private IBusinessDomainDAO businessDao;

	@Mock
	private IPartyDAO partyDao;

	@Mock
	private ITransactionDAO transactionDao;

	@Mock
	private IInterchangeAgreementDAO interchangeAgreementDao;

	@Mock
	private IPartyAgreementDAO partyAgreementDao;

	@Mock
	private ICredentialsDAO credentialsDao;

	@Mock
	private IMetadataService metadataService;

	@Mock
	private IServiceEndpointDao serviceEndpointDao;

	@Mock
	private ICertificateDAO certificateDao;

	@Mock
	private IPartyIdentifierDAO partyIdentifierDao;

	private InterchangeAgreement ica;
	
	private BusinessDomain businessDomain;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		EntityAccessInfo acc = new EntityAccessInfo();
		acc.setCreationId("testing");
		acc.setModificationDate(new Date());
		acc.setModificationId("testing");
		acc.setCreationDate(new Date());

		businessDomain = new BusinessDomain();
		businessDomain.setName("TEST");
		businessDomain.setAccessInfo(acc);
		businessDomain.setResponseSignatureRequired(false);
		HashSet<BusinessDomain> hBuss = new HashSet<BusinessDomain>();
		hBuss.add(businessDomain);

		// Party 1 creation
		Party p = new Party();
		PartyIdentifier pId = new PartyIdentifier();

		pId.setSchemeId(IdentifierIssuingAgency.GLN);
		pId.setValue("000886688001");
		pId.setParty(p);
		pId.setAccessInfo(acc);
		pId.setId(new Long(1));

		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setSchemeId(IdentifierIssuingAgency.VA_VAT);
		pId2.setValue("TEST");
		pId2.setId(new Long(2));

		HashSet<PartyIdentifier> h = new HashSet<PartyIdentifier>();
		h.add(pId);
		h.add(pId2);

		p.setIdentifiers(h);
		p.setAccessInfo(acc);
		p.setBusinessDomain(businessDomain);
		p.setName("000886688001");
		p.setId(new Long(1));

		// Party 2 creation
		Party p2 = new Party();
		PartyIdentifier pId3 = new PartyIdentifier();

		pId3.setSchemeId(IdentifierIssuingAgency.GLN);
		pId3.setValue("000886688002");
		pId3.setParty(p);
		pId3.setAccessInfo(acc);
		pId3.setId(new Long(3));

		PartyIdentifier pId4 = new PartyIdentifier();
		pId4.setSchemeId(IdentifierIssuingAgency.VA_VAT);
		pId4.setValue("TEST2");
		pId4.setId(new Long(4));

		HashSet<PartyIdentifier> h2 = new HashSet<PartyIdentifier>();
		h.add(pId3);
		h.add(pId4);

		p2.setIdentifiers(h2);
		p2.setAccessInfo(acc);
		p2.setBusinessDomain(businessDomain);
		p2.setName("000886688002");
		p2.setId(new Long(2));

		Role role1 = new Role();
		role1.setAccessInfo(acc);
		role1.setCode("R1");
		role1.setId(new Long(1));
		role1.setName("role1");

		Role role2 = new Role();
		role2.setAccessInfo(acc);
		role2.setCode("R2");
		role2.setId(new Long(2));
		role2.setName("role2");

		PartyRole pRole1 = new PartyRole();
		pRole1.setAccessInfo(acc);
		pRole1.setRole(role1);
		pRole1.setId(new Long(1));
		pRole1.setParty(p);

		PartyRole pRole2 = new PartyRole();
		pRole2.setAccessInfo(acc);
		pRole2.setRole(role2);
		pRole2.setId(new Long(2));
		pRole2.setParty(p2);

		HashSet<PartyRole> roles = new HashSet<PartyRole>();
		roles.add(pRole1);
		roles.add(pRole2);

		ArrayList<PartyRole> lRoles1 = new ArrayList<PartyRole>();
		lRoles1.add(pRole1);

		ArrayList<PartyRole> lRoles2 = new ArrayList<PartyRole>();
		lRoles2.add(pRole2);

		Transaction t = new Transaction();
		t.setId(new Long(8));
		t.setName("test");
		t.setSenderRole(role1);
		t.setReceiverRole(role2);

		Set<Transaction> transactions = new HashSet<Transaction>();
		transactions.add(t);

		Profile prof = new Profile();
		prof.setBusinessDomains(hBuss);
		prof.setId(new Long(8));
		prof.setTransactions(transactions);

		ica = new InterchangeAgreement();
		ica.setProfile(prof);
		ica.setId(new Long(1));
		ica.setPartyRoles(roles);

		ArrayList<InterchangeAgreement> icas = new ArrayList<InterchangeAgreement>();
		icas.add(ica);

		Mockito.when(partyDao.getParty(IdentifierIssuingAgency.GLN, "000886688001", businessDomain)).thenReturn(p);
		Mockito.when(partyDao.getParty(IdentifierIssuingAgency.GLN, "000886688002", businessDomain)).thenReturn(p2);
		Mockito.when(interchangeAgreementDao.getInterchangeAgreements(p, p2, new Long(8))).thenReturn(icas);
	}

	@Test(expected = UndefinedIdentifierException.class)
	public void testGetPartyNull() throws Exception {
		String s = null;
		Party np = authorisationService.getParty(s, businessDomain);
	}

	@Test(expected = UndefinedIdentifierException.class)
	public void testGetPartyEmpty() throws Exception {
		String s = "";
		Party np = authorisationService.getParty(s, businessDomain);
	}

	@Test
	public void testGetParty() throws Exception {
		Party p = authorisationService.getParty("GLN:000886688001", businessDomain);
		assertNotNull(p);
		assertEquals("000886688001", p.getName());
		assertEquals("TEST", p.getBusinessDomain().getName());
		assertEquals(null, authorisationService.getParty("GLN:000886688003", businessDomain));
	}

	@Test
	public void testCheckAuthorisation() throws Exception {
		Party p1 = authorisationService.getParty("GLN:000886688001", businessDomain);
		Party p2 = authorisationService.getParty("GLN:000886688002", businessDomain);
		InterchangeAgreement ica1 = authorisationService.checkAuthorisation(p1, p1, p2, new Long(8));
		compareInterChangAgrEquals(ica1, ica);
	}

	private void compareInterChangAgrEquals(InterchangeAgreement agr1, InterchangeAgreement agr2) {
		assertEquals(agr1.getId(), agr2.getId());
		assertEquals(agr1.getCiaLevel(), agr2.getCiaLevel());
		assertEquals(agr1.getAccessInfo(), agr2.getAccessInfo());
		assertEquals(agr1.getProfile(), agr2.getProfile());
		assertEquals(agr1.getPartyRoles(), agr2.getPartyRoles());
	}
}
