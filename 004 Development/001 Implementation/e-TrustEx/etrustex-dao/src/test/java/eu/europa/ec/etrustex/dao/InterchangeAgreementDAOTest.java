package eu.europa.ec.etrustex.dao;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

public class InterchangeAgreementDAOTest extends AbstractEtrustExTest {

	@Autowired private IInterchangeAgreementDAO icaDao;
	@Autowired private IPartyRoleDao 			prDao;
	@Autowired private IPartyDAO 				partyDao;
	@Autowired private IRoleDAO 				roleDao;
	@Autowired private IProfileDAO 				profileDao;
	@Autowired private ITransactionDAO			transactionDao;
	@Autowired private IMessageDAO				messageDao;
	@Autowired private IEndpointDAO				endpointDao;
	@Autowired private IMetadataDAO				mdDao;
	@Autowired private IMessageResponseCodeDAO	mrcDao;
	@Autowired private IBusinessDomainDAO		bdDao;
	@Autowired private IPartyIdentifierDAO		pidDao;
	@Autowired private ICIALevelDAO				ciaLevelDao;
	
	private CIALevel ciaLevel6 = null;
	private Profile  profile = null;
	
	@Before public void setupTests(){
		ciaLevel6 = new CIALevel();
		ciaLevel6.setConfidentialityLevel(6);
		ciaLevel6.setIntegrityLevel(6);
		ciaLevel6.setAvailabilityLevel(6);
		
		profile = new Profile();
		profile.setName("P");	
	}
	
	@Test public void testCreate(){
		PartyRole pr2 = prDao.create(buildPartyRole("P2", "R2"));
		PartyRole pr1 = prDao.create(buildPartyRole("P1", "R1"));
		
		InterchangeAgreement ica = icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		Assert.assertNotNull(ica.getId());
		Assert.assertEquals(2, ica.getPartyRoles().size());
		PartyRole[] prs = ica.getPartyRoles().toArray(new PartyRole[2]);
		Assert.assertTrue(prs[0].getId() != null && prs[1].getId() != null);
	}
	
	@Test public void testUpdate(){
		PartyRole pr1 = prDao.create(buildPartyRole("P1", "R1"));
		
		InterchangeAgreement ica = icaDao.create(buildICA(ciaLevel6, profile, pr1, null));
		
		Assert.assertNotNull(ica.getId());
		
		Profile p2 = new Profile();
		p2.setName("2");
		
		CIALevel ciaLevel2 = new CIALevel();
		ciaLevel2.setConfidentialityLevel(7);
		ciaLevel2.setIntegrityLevel(7);
		ciaLevel2.setAvailabilityLevel(7);
		
		PartyRole pr2 = buildPartyRole("P2", "R2");
		
		ica.getPartyRoles().clear();
		ica.addPartyRole(pr2);
		ica.setCiaLevel(ciaLevel2);
		ica.setProfile(p2);
		ica = icaDao.update(ica);
		
		Assert.assertEquals(1, ica.getPartyRoles().size());
		PartyRole[] prs = ica.getPartyRoles().toArray(new PartyRole[2]);
		Assert.assertTrue(prs[0].getId().equals(pr2.getId()));
	}
	
	@Test public void testGetInterchangeAgreements(){
		PartyRole pr1 = prDao.create(buildPartyRole("P1", "R1"));
		PartyRole pr2 = prDao.create(buildPartyRole("P2", "R2"));
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		Set<Party> set = new HashSet<Party>(Arrays.asList(pr1.getParty()));
		Set<Party> set2 = new HashSet<Party>(Arrays.asList(pr2.getParty()));
		
		Assert.assertEquals(1, icaDao.getInterchangeAgreements(null, set, null).size());
		Assert.assertEquals(1, icaDao.getInterchangeAgreements(null, null, set).size());
		Assert.assertEquals(1, icaDao.getInterchangeAgreements(null, set2, set).size());
	}

	@Test public void testFindInterchangeAgreementsByCriteria(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		profileDao.create(profile);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		PartyIdentifier pId1 = new PartyIdentifier();
		pId1.setParty(party1);
		pId1.setSchemeId(IdentifierIssuingAgency.GLN);
		pId1.setValue("P1");
		pidDao.create(pId1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setParty(party2);
		pId2.setSchemeId(IdentifierIssuingAgency.GLN);
		pId2.setValue("P2");
		pidDao.create(pId2);
		
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
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		ICASearchDTO dto = new ICASearchDTO();
		dto.setBusinessDomainId(bd.getId());
		dto.setIdentifierValue(pId1.getValue());
		dto.setPartyName(party1.getName());
		dto.setProfileId(profile.getId());
		dto.setRoleId(role1.getId());
		
		List<InterchangeAgreement> icaList = icaDao.findInterchangeAgreementsByCriteria(dto);
		
		Assert.assertEquals(1, icaList.size());
		
		dto = new ICASearchDTO();
		dto.setBusinessDomainId(bd.getId());
		dto.setIdentifierValue(pId2.getValue());
		dto.setPartyName(party2.getName());
		dto.setProfileId(profile.getId());
		dto.setRoleId(role2.getId());
		
		icaList = icaDao.findInterchangeAgreementsByCriteria(dto);
		
		Assert.assertEquals(1, icaList.size());
		
		dto = new ICASearchDTO();
		dto.setBusinessDomainId(bd.getId());
		dto.setIdentifierValue("Not there");
		dto.setPartyName(party2.getName());
		dto.setProfileId(profile.getId());
		dto.setRoleId(role2.getId());
		
		icaList = icaDao.findInterchangeAgreementsByCriteria(dto);
		
		Assert.assertEquals(0, icaList.size());
	} 
	
	@Test public void testExistICAwithSameRoleForParties(){
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		Role role3 = new Role();
		role3.setName("ANY_R3");
		role3.setCode("ANY_ROLE_R3");
		roleDao.create(role3);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		PartyRole pr3 = new PartyRole();
		pr3.setParty(party2);
		pr3.setRole(role3);
		prDao.create(pr3);
		
		icaDao.create(buildICA(ciaLevel6, null, pr1, pr2));
		icaDao.create(buildICA(ciaLevel6, null, pr1, pr3));
		icaDao.create(buildICA(ciaLevel6, null, pr2, pr3));
		
		Boolean icaBool = icaDao.existICAwithSameRoleForParties(role1);
		Assert.assertFalse(icaBool);
		
		icaBool = icaDao.existICAwithSameRoleForParties(role2);
		Assert.assertFalse(icaBool);
		
		icaBool = icaDao.existICAwithSameRoleForParties(role3);
		Assert.assertFalse(icaBool);
		
		icaBool = icaDao.existICAwithSameRoleForParties(null);
		Assert.assertFalse(icaBool);
		
		//TODO
	} 
	
	@Test public void testGetInterchangeAgreementsbySenderReceiverAndTransaction(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		Transaction t = new Transaction();
		t.setName("tx1");
		t = transactionDao.create(t);
		
		profileDao.create(profile);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		PartyIdentifier pId1 = new PartyIdentifier();
		pId1.setParty(party1);
		pId1.setSchemeId(IdentifierIssuingAgency.GLN);
		pId1.setValue("P1");
		pidDao.create(pId1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setParty(party2);
		pId2.setSchemeId(IdentifierIssuingAgency.GLN);
		pId2.setValue("P2");
		pidDao.create(pId2);
		
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
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		InterchangeAgreement ica = buildICA(ciaLevel6, profile, pr1, pr2);
		icaDao.create(ica);
		
		List<InterchangeAgreement> icaList = icaDao.getInterchangeAgreements(party1, party2, t.getId(), profile.getId());
		
		Assert.assertEquals(0, icaList.size());
		
		profile.getTransactions().add(t);
		profileDao.update(profile);
		
		
		icaList = icaDao.getInterchangeAgreements(party1, party2, t.getId(), profile.getId());
		
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.getInterchangeAgreements(party1, null, t.getId(), profile.getId());
		
		Assert.assertEquals(1, icaList.size());
		
		//Set the validity start date in the future
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 20);
		ica.setValidityStartDate(cal.getTime());
		icaDao.update(ica);
		
		icaList = icaDao.getInterchangeAgreements(party1, null, t.getId(), profile.getId());
		Assert.assertEquals(0, icaList.size());
		
		//Set the validity start date in the past
		cal.add(Calendar.YEAR, -30);
		ica.setValidityStartDate(cal.getTime());
		icaDao.update(ica);
		
		icaList = icaDao.getInterchangeAgreements(party1, null, t.getId(), profile.getId());
		Assert.assertEquals(1, icaList.size());
	} 
	
	@Test public void testIsUniqueBy(){
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		Profile p1 = new Profile();
		p1.setName("P1");
		profileDao.create(p1);
		
		Boolean boolResult = icaDao.isUniqueBy(p1.getId(), party1.getId(), role1.getId(), party2.getId(), role2.getId());
		
		Assert.assertTrue(boolResult);
		
		icaDao.create(buildICA(ciaLevel6, p1, pr1, pr2));
		
		boolResult = icaDao.isUniqueBy(p1.getId(), party1.getId(), role1.getId(), party2.getId(), role2.getId());
		
		Assert.assertFalse(boolResult);
		
		boolResult = icaDao.isUniqueBy(p1.getId(), party2.getId(), role2.getId(), party1.getId(), role1.getId());
		
		Assert.assertFalse(boolResult);
	} 
	
	@Test public void testGetInterchangeAgreementsByPartyrolesProfilesAndCIA(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		Transaction t = new Transaction();
		t.setName("tx1");
		t = transactionDao.create(t);
		
		CIALevel ciaLevel1 = new CIALevel();
		ciaLevel1.setConfidentialityLevel(1);
		ciaLevel1.setIntegrityLevel(1);
		ciaLevel1.setAvailabilityLevel(1);
		
		ciaLevelDao.create(ciaLevel1);
		ciaLevelDao.create(ciaLevel6);
		
		Profile profile2 = new Profile();
		profile2.setName("P2");	
		
		profileDao.create(profile);
		profileDao.create(profile2);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		PartyIdentifier pId1 = new PartyIdentifier();
		pId1.setParty(party1);
		pId1.setSchemeId(IdentifierIssuingAgency.GLN);
		pId1.setValue("P1");
		pidDao.create(pId1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setParty(party2);
		pId2.setSchemeId(IdentifierIssuingAgency.GLN);
		pId2.setValue("P2");
		pidDao.create(pId2);
		
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
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		List<InterchangeAgreement> icaList = icaDao.getInterchangeAgreements(pr1, pr2, profile, ciaLevel6);
		
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.getInterchangeAgreements(pr1, pr2, profile2, ciaLevel6);
		
		Assert.assertEquals(0, icaList.size());
		
		icaList = icaDao.getInterchangeAgreements(pr1, pr2, profile, ciaLevel1);
		
		Assert.assertEquals(0, icaList.size());
	} 
	
	@Test public void testContainsParty(){
		Party party0 = new Party();
		party0.setName("P0");
		partyDao.create(party0);
		
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		Profile p1 = new Profile();
		p1.setName("P1");
		profileDao.create(p1);
		
		icaDao.create(buildICA(ciaLevel6, p1, pr1, pr2));
		
		Boolean boolResult = icaDao.containsParty(party1);
		
		Assert.assertTrue(boolResult);
		
		boolResult = icaDao.containsParty(party2);
		
		Assert.assertTrue(boolResult);
		
		boolResult = icaDao.containsParty(party0);
		
		Assert.assertFalse(boolResult);
	} 
	
	@Test public void testIsUsed(){
		PartyRole pr1 = prDao.create(buildPartyRole("P1", "R1"));
		PartyRole pr2 = prDao.create(buildPartyRole("P2", "R2"));
		
		InterchangeAgreement ica = icaDao.create(buildICA(ciaLevel6, null, pr1, pr2));
		
		Boolean usedBool = icaDao.isUsed(ica.getId());
		
		Assert.assertFalse(usedBool);
		
		Party p = new Party();
		p.setName("MyParty");
		partyDao.create(p);
		
		Transaction t = new Transaction();
		t.setName("tx1");
		transactionDao.create(t);
		
		Message m = new Message();
		m.setDocumentId("Doc1");
		m.setMessageDocumentTypeCode("DOCUMENT_TYPE_CODE");
		m.setTransaction(t);
		m.setSender(p);
		m.setIssuer(p);
		m.setAgreement(ica);
		messageDao.create(m);
		
		usedBool = icaDao.isUsed(ica.getId());
		Assert.assertTrue("Used through a message", usedBool);

		ica = icaDao.create(buildICA(ciaLevel6, null, pr1, pr2));
		
		JMSEndpoint ep = new JMSEndpoint();
		ep.setDestinationJndiName("D");
		ep.setIsSupportingReplyTo(false);
		ep.setIsActive(true);
		ep.setInterchangeAgreement(ica);
		ep = (JMSEndpoint)endpointDao.create(ep);
		
		usedBool = icaDao.isUsed(ica.getId());
		Assert.assertTrue("Used through a message", usedBool);

		ica = icaDao.create(buildICA(ciaLevel6, null, pr1, pr2));
		
		MetaDataItem md = new MetaDataItem();
		md.setRawItemType("type");
		md.setValue("Value");
		md.setInterchangeAgreement(ica);
		mdDao.create(md);
		
		usedBool = icaDao.isUsed(ica.getId());
		Assert.assertTrue("Used through a metadata", usedBool);

		ica = icaDao.create(buildICA(ciaLevel6, null, pr1, pr2));
		
		MessageResponseCode mrc = new MessageResponseCode();
		mrc.setInterchangeAgreement(ica);
		mrc.setResponseCodeKey("key");
		mrc.setResponseCodeValue("value");
		mrcDao.create(mrc);
		
		usedBool = icaDao.isUsed(ica.getId());
		Assert.assertTrue("Used through a metadata", usedBool);
	}
	
	@Test public void testGetInterchangeAgreementsByParty1RolesAndProfile(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		profileDao.create(profile);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		PartyIdentifier pId1 = new PartyIdentifier();
		pId1.setParty(party1);
		pId1.setSchemeId(IdentifierIssuingAgency.GLN);
		pId1.setValue("P1");
		pidDao.create(pId1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setParty(party2);
		pId2.setSchemeId(IdentifierIssuingAgency.GLN);
		pId2.setValue("P2");
		pidDao.create(pId2);
		
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
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		List<InterchangeAgreement> icaList = icaDao.getInterchangeAgreements(party1.getId(),role1.getId(), profile.getId(), role2.getId());
		
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.getInterchangeAgreements(party2.getId(),role2.getId(), profile.getId(), role1.getId());
		
		Assert.assertEquals(1, icaList.size());
		
		Profile profile2 = new Profile();
		profile2.setName("P2");
		profileDao.create(profile2);
		
		icaList = icaDao.getInterchangeAgreements(party2.getId(),role2.getId(), profile2.getId(), role1.getId());
		
		Assert.assertEquals(0, icaList.size());

	} 
	
	@Test public void testFindByParty(){
		Party party0 = new Party();
		party0.setName("P0");
		partyDao.create(party0);
		
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		List<InterchangeAgreement> icaList = icaDao.findByParty(party0);
		Assert.assertTrue(icaList.isEmpty());
		
		icaList = icaDao.findByParty(party1);
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.findByParty(party2);
		Assert.assertEquals(1, icaList.size());
	} 
	
	@Test public void testGetPartiesInExistingAgreement(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		profileDao.create(profile);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd);
		party1 = partyDao.create(party1);
		
		PartyIdentifier pId1 = new PartyIdentifier();
		pId1.setParty(party1);
		pId1.setSchemeId(IdentifierIssuingAgency.GLN);
		pId1.setValue("P1");
		pidDao.create(pId1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd);
		partyDao.create(party2);
		
		PartyIdentifier pId2 = new PartyIdentifier();
		pId2.setParty(party2);
		pId2.setSchemeId(IdentifierIssuingAgency.GLN);
		pId2.setValue("P2");
		pidDao.create(pId2);
		
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
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		
		
		List<Party> partiesList = icaDao.getPartiesInExistingAgreement(party1.getId(), role1.getId(), profile.getId(), role2.getId());
		
		Assert.assertEquals(1, partiesList.size());
		
		partiesList = icaDao.getPartiesInExistingAgreement(party2.getId(), role2.getId(), profile.getId(), role1.getId());
		
		Assert.assertEquals(1, partiesList.size());
	} 
	
	@Test public void testGetInterchangeAgreementsByProfile(){
		Party party0 = new Party();
		party0.setName("P0");
		partyDao.create(party0);
		
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Role role1 = new Role();
		role1.setName("ANY_R1");
		role1.setCode("ANY_ROLE_R1");
		roleDao.create(role1);
		
		Role role2 = new Role();
		role2.setName("ANY_R2");
		role2.setCode("ANY_ROLE_R2");
		roleDao.create(role2);
		
		PartyRole pr1 = new PartyRole();
		pr1.setParty(party1);
		pr1.setRole(role1);
		prDao.create(pr1);
		
		PartyRole pr2 = new PartyRole();
		pr2.setParty(party2);
		pr2.setRole(role2);
		prDao.create(pr2);
		
		profile = profileDao.create(profile);
		
		Profile profile2 = new Profile();
		profile2.setName("P2");
		profileDao.create(profile2);
		
		Profile profile3 = new Profile();
		profile3.setName("P3");
		profileDao.create(profile3);
		
		icaDao.create(buildICA(ciaLevel6, profile, pr1, pr2));
		icaDao.create(buildICA(ciaLevel6, profile2, pr1, pr2));
		
		List<InterchangeAgreement> icaList = icaDao.getInterchangeAgreementsByProfile(profile.getId());
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.getInterchangeAgreementsByProfile(profile2.getId());
		Assert.assertEquals(1, icaList.size());
		
		icaList = icaDao.getInterchangeAgreementsByProfile(profile3.getId());
		Assert.assertEquals(0, icaList.size());
	} 
	
	private PartyRole buildPartyRole(String partyName, String roleName){
		Party party = new Party();
		party.setName(partyName);
		
		Role role = new Role();
		role.setCode(roleName);
		role.setName(roleName);
		
		PartyRole pr = new PartyRole();
		pr.setRole(role);
		pr.setParty(party);
		
		return pr;
	}
	
	private InterchangeAgreement buildICA(CIALevel cia, Profile profile, PartyRole partyR1, PartyRole partyR2){
		InterchangeAgreement ica = new InterchangeAgreement();
		if(partyR1 != null){
			ica.addPartyRole(partyR1);
		}
		if(partyR2 != null){
			ica.addPartyRole(partyR2);
		}
		if(cia != null){
			ica.setCiaLevel(cia);
		}
		if(profile != null){
			ica.setProfile(profile);
		}else{
			Profile profile1 = new Profile();
			profile1.setName("P"+UUID.randomUUID().toString());
			ica.setProfile(profile1);
		}
		
		return ica;
	}
}
