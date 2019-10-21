package eu.europa.ec.etrustex.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import eu.europa.ec.etrustex.dao.impl.PartyDAO;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.EventNotification;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

public class PartyDAOTest extends AbstractEtrustExTest {
	
	public static final String BD_NAME = "TEST_BUSINESS_DOMAIN";
	public static final String BD_NAME_2 = "TEST_BUSINESS_DOMAIN_2";
	public static final String CREDENTIALS_NAME = "CREDENTIALS_NAME";
	public static final String PARTY_ETRUSTEX = PartyDAO.EVENT_NOTIF_PUBLISHER_ETRUSTEX;
	public static final String PARTY_NAME = "TEST_PARTY_NAME";
	public static final String PARTY_NAME_2 = "TEST_PARTY_NAME_2";
	public static final String PARTY_NAME_3 = "TEST_PARTY_NAME_3";
	public static final String PARTY_NAME_PI_3 = "TEST_PARTY_NAME_PI_3";
	
	@Autowired
	private IPartyDAO partyDao;
	
	@Autowired
	private IBusinessDomainDAO businessDomainDAO;
	
	@Autowired
	private ICredentialsDAO credentialsDao;
	
	@Autowired
	private IEventNotificationDAO eventNotificationDAO;
	
	@Autowired 
	IProfileDAO profileDao;
	
	@Test
	public void testCreate(){
		Party party = new Party();
		party.setName(PARTY_NAME);
		
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Credentials cred = new PartyCredentials();
		cred.setUser(CREDENTIALS_NAME);
		cred.setSignatureRequired(Boolean.FALSE);
		cred.setSalt(Boolean.FALSE);
		cred.setPassword("password");
		cred.setPasswordEncrypted(Boolean.FALSE);
		//credentialsDao.create(cred);
		
		partyDao.create(party);
		party.setBusinessDomain(businessDomain);
		party.setCredentials(cred);
		flush();
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testCreateWithoutName(){
		Party party = new Party();		
		partyDao.create(party);
		flush();
	}
	
	@Test(expected=PersistenceException.class)
	public void testCreateNonUniqueName(){
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		partyDao.create(party1);
		
		Party party2 = new Party();		
		party2.setName(PARTY_NAME);
		partyDao.create(party2);
		flush();
	}
	
	@Test
	public void testCRUD(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		
		Credentials cred = new PartyCredentials();
		cred.setUser(CREDENTIALS_NAME);
		cred.setSignatureRequired(Boolean.FALSE);
		cred.setSalt(Boolean.FALSE);
		cred.setPassword("password");
		cred.setPasswordEncrypted(Boolean.FALSE);
		
		Certificate cert = new Certificate();
		cert.setIsRevoked(Boolean.FALSE);
		cert.setIsActive(Boolean.FALSE);
		cert.setEncodedData("DATA");
		cert.setSerialNumber("Serial");
		cert.setIssuer("Issuer");
		cert.setHolder("holder");
		cert.setUsage("Usage");
		cert.setType("Type");
		
		Party originalParty = new Party();		
		originalParty.setName(PARTY_NAME);
		partyDao.create(originalParty);
		Assert.assertNotNull("Object has not been created", originalParty.getId());
		
		Party party2 = partyDao.read(originalParty.getId());
		Assert.assertEquals("It is not the same party",originalParty.getName(), party2.getName());
		
		party2.setName(PARTY_NAME_2);
		party2.setBusinessDomain(businessDomain);
		party2.setCredentials(cred);
		party2.getCertificates().add(cert);
		partyDao.update(party2);
		
		party2 = partyDao.read(originalParty.getId());
		Assert.assertNotNull("Object is null", party2.getId());
		Assert.assertEquals("The name has not been updated",PARTY_NAME_2, party2.getName());
		
		partyDao.delete(originalParty.getId());
		party2 = partyDao.read(originalParty.getId());
		Assert.assertNull("Object is not deleted by Id", party2);
		
		Party newParty = new Party();		
		newParty.setName(PARTY_NAME);
		newParty = partyDao.create(newParty);
		partyDao.delete(newParty);
		Assert.assertNull("Object is not deleted by Reference", partyDao.read(newParty.getId()));
	}
	
	@Test
	public void testGetEventPublisherParty(){
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		partyDao.create(party1);
		
		Assert.assertNull(partyDao.getEventPublisherParty(party1));
	}
	
	@Test
	public void testGetAllPartiesForBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		partyDao.create(party1);
		
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setBusinessDomain(businessDomain);
		partyDao.create(party2);
		
		Assert.assertEquals("Didn't return the expected number of parties",2,partyDao.getAllPartiesForBusinessDomain(businessDomain.getId()).size());
	}
	
	@Test
	public void testGetPartyForBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		partyDao.create(party1);
		
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setBusinessDomain(businessDomain);
		partyDao.create(party2);
		
		Assert.assertNotNull("Didn't return the expected number of parties",partyDao.getPartyForBusinessDomain(PARTY_NAME_2, businessDomain.getId()).getId());
		
		Assert.assertNull("Didn't return the expected number of parties",partyDao.getPartyForBusinessDomain(PARTY_NAME_2+"__F", businessDomain.getId()));
	}
	
	@Test
	public void testGetThirdPartyForBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setThirdPartyFlag(Boolean.TRUE);
		party1.setBusinessDomain(businessDomain);
		partyDao.create(party1);
		
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setThirdPartyFlag(Boolean.FALSE);
		party2.setBusinessDomain(businessDomain);
		partyDao.create(party2);
		
		List<Party> thirdP = partyDao.getThirdPartiesForBusinessDomain(businessDomain.getId());
		
		Assert.assertEquals("Should find one party", 1, thirdP.size());
		Assert.assertEquals("Should find one party", PARTY_NAME, thirdP.get(0).getName());
	}
		
	@Test
	public void testGetPartyWrongIIA(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);
		party1.setThirdPartyFlag(Boolean.TRUE);					
		PartyIdentifier pi = new PartyIdentifier();
		pi.setSchemeId(IdentifierIssuingAgency.GLN);
		pi.setValue(PARTY_NAME_PI_3);
		pi.setParty(party1);
		party1.getIdentifiers().add(pi);
		partyDao.create(party1);
		
		Assert.assertNotNull("Should find one party", partyDao.getParty(IdentifierIssuingAgency.GLN, PARTY_NAME_PI_3, businessDomain));
		
		Assert.assertNull("Should not find any party", partyDao.getParty(IdentifierIssuingAgency.GLN, PARTY_NAME, null));
		
		Assert.assertNull("Should not find any party",partyDao.getParty(IdentifierIssuingAgency.AD_VAT, PARTY_NAME, businessDomain));
	}	
	
	@Test
	public void testGetPartyWrongParty(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);
		party1.setThirdPartyFlag(Boolean.TRUE);					
		PartyIdentifier pi = new PartyIdentifier();
		pi.setSchemeId(IdentifierIssuingAgency.GLN);
		pi.setValue(PARTY_NAME_PI_3);
		pi.setParty(party1);
		party1.getIdentifiers().add(pi);
		partyDao.create(party1);
		
		Assert.assertNotNull("Should find one party", partyDao.getParty(IdentifierIssuingAgency.GLN, PARTY_NAME_PI_3, businessDomain));
		
		Assert.assertNull("Should not find any party", partyDao.getParty(IdentifierIssuingAgency.GLN, PARTY_NAME_2, businessDomain));
	}
	
	
	
	
	@Test
	public void testFlushEm(){
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		partyDao.create(party1);
		
		partyDao.flushEm();
	}
	
	@Test
	public void testgetAll(){
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		partyDao.create(party1);		
		Assert.assertTrue("Should retrieve at least one result", partyDao.getAll().size() >= 1);
	}
	
	@Test
	public void testGetEtrustexParty(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_ETRUSTEX);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);		
		partyDao.create(party1);
		
		Party p = partyDao.getEtrustexParty(businessDomain.getId());
		
		Assert.assertNotNull("Should find Etrustex Party",p);
		Assert.assertEquals("Should find Etrustex Party",PARTY_ETRUSTEX,p.getName());
	}
	
	@Test
	public void testIsUniquePartyPerBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Assert.assertTrue("The is not there yet", partyDao.isUniquePartyPerBusinessDomain(PARTY_NAME, businessDomain.getId()));
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);		
		partyDao.create(party1);
		
		Assert.assertFalse("Party already exist", partyDao.isUniquePartyPerBusinessDomain(PARTY_NAME, businessDomain.getId()));
	}
	
	
	
	@Test
	public void testFindPartiesByCriteriaBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);		
		party1.setThirdPartyFlag(Boolean.FALSE);
		partyDao.create(party1);
		
		BusinessDomain businessDomain2 = new BusinessDomain();
		businessDomain2.setName(BD_NAME_2);
		businessDomain2.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain2 = businessDomainDAO.create(businessDomain2);
		
		flush();
		
		List<Party> resultList = partyDao.findPartiesByCriteria(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain2.getId()))), null, null);
		Assert.assertEquals("Should not find any party", 0, resultList.size());
		
		Assert.assertEquals("Should find 1 Party", 1, partyDao.getAllPartiesForBusinessDomain(businessDomain.getId()).size());
		
		List<Party> resultList2 = partyDao.findPartiesByCriteria(PARTY_NAME, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))), null, null);
		Assert.assertEquals("Should find 1 Party", 1, resultList2.size());
	}
	
	@Test
	public void testFindPartiesByCriteria(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		
		BusinessDomain businessDomain2 = new BusinessDomain();
		businessDomain2.setName(BD_NAME_2);
		businessDomain2.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain2 = businessDomainDAO.create(businessDomain2);
		
		Credentials cred = new PartyCredentials();
		cred.setUser(CREDENTIALS_NAME);
		cred.setSignatureRequired(Boolean.FALSE);
		cred.setSalt(Boolean.FALSE);
		cred.setPassword("password");
		cred.setPasswordEncrypted(Boolean.FALSE);
		credentialsDao.create(cred);
		
		//PARTY 1
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);
		party1.setThirdPartyFlag(Boolean.FALSE);
		party1.setCredentials(cred);
		partyDao.create(party1);
		
		//PARTY 2
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party2);
		party2.setThirdPartyFlag(Boolean.TRUE);					
		partyDao.create(party2);
		
		//PARTY 3
		Party party3 = new Party();		
		party3.setName(PARTY_NAME_3);
		party3.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party3);
		party3.setThirdPartyFlag(Boolean.TRUE);					
		PartyIdentifier pi = new PartyIdentifier();
		pi.setSchemeId(IdentifierIssuingAgency.GLN);
		pi.setValue(PARTY_NAME_PI_3);
		pi.setParty(party3);
		party3.getIdentifiers().add(pi);
		partyDao.create(party3);
	
		flush();
		
		List<Party> resultList = partyDao.findPartiesByCriteria(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain2.getId()))), null, null);
		Assert.assertEquals("Should not find any party", 0, resultList.size());
		
		resultList = partyDao.findPartiesByCriteria(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))), null, null);
		Assert.assertEquals("Should find 3 Parties", 3, resultList.size());
		
		resultList = partyDao.findPartiesByCriteria(PARTY_NAME_3, null, null, null, null, null);
		Assert.assertEquals("Did not find result by party name", 1, resultList.size());
		
		resultList = partyDao.findPartiesByCriteria(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))), null, null);
		Assert.assertEquals("Should find 3 Parties", 3, resultList.size());
			
		resultList = partyDao.findPartiesByCriteria(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))), Boolean.TRUE, null);
		Assert.assertEquals("Should find 2 parties with third party flag", 2, resultList.size());
		
		resultList = partyDao.findPartiesByCriteria(null, CREDENTIALS_NAME, null, null, null, null);
		Assert.assertEquals("Should only find one party with credentials", 1, resultList.size());
		
		resultList = partyDao.findPartiesByCriteria(null, null, PARTY_NAME_PI_3, null, null, null);
		Assert.assertEquals("Should only find one party with that Party Identifier", 1, resultList.size());
	}
	
	@Test
	public void testGetMessageIssuer(){
		Credentials cred = new PartyCredentials();
		cred.setUser(CREDENTIALS_NAME);
		cred.setSignatureRequired(Boolean.FALSE);
		cred.setSalt(Boolean.FALSE);
		cred.setPassword("password");
		cred.setPasswordEncrypted(Boolean.FALSE);
		credentialsDao.create(cred);
		
		Party party = new Party();		
		party.setName(PARTY_NAME);
		party.setThirdPartyFlag(Boolean.FALSE);
		party.setCredentials(cred);
		partyDao.create(party);
		
		Assert.assertEquals("Should find the party that corresponds to the issuer",PARTY_NAME, partyDao.getMessageIssuer(CREDENTIALS_NAME).getName());

		Assert.assertNull(partyDao.getMessageIssuer(CREDENTIALS_NAME+"_2"));
	}
	
	@Test
	public void testGetPartiesConfiguredToReceiveEventNotification(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);
		party1.setThirdPartyFlag(Boolean.FALSE);
		partyDao.create(party1);
		
		Profile p = new Profile();
		p.setName("MyProfile");
		profileDao.create(p);
		
		Profile p2 = new Profile();
		p2.setName("MyProfile2");
		profileDao.create(p2);
		
		EventNotification ev = new EventNotification();
		ev.setProfile(p);
		
		ev.setBusinessDomain(businessDomain);
		ev.setEventType(EventNotificationType.DOC);
		ev.setParty(party1);
		eventNotificationDAO.create(ev);
		
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setThirdPartyFlag(Boolean.FALSE);
		partyDao.create(party2);
		
		EventNotification ev2 = new EventNotification();
		ev2.setProfile(p2);
		ev2.setBusinessDomain(businessDomain);
		ev2.setEventType(EventNotificationType.DOC);
		ev2.setParty(party2);
		eventNotificationDAO.create(ev2);
		
		Assert.assertEquals("Should find One Event for the new BD", PARTY_NAME, partyDao.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.DOC, businessDomain.getId(), p.getId()).get(0).getName());
		Assert.assertEquals("Should find One Event for the new BD", PARTY_NAME_2, partyDao.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.DOC,  businessDomain.getId(), p2.getId()).get(0).getName());
		Assert.assertEquals("Should Not find anything",0, partyDao.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG,  businessDomain.getId(), p2.getId()).size());
	}
	
	@Test
	public void testGetPartiesForBusinessDomain(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		
		
		BusinessDomain businessDomain2 = new BusinessDomain();
		businessDomain2.setName(BD_NAME_2);
		businessDomain2.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain2 = businessDomainDAO.create(businessDomain2);
		
		Credentials cred = new PartyCredentials();
		cred.setUser(CREDENTIALS_NAME);
		cred.setSignatureRequired(Boolean.FALSE);
		cred.setSalt(Boolean.FALSE);
		cred.setPassword("password");
		cred.setPasswordEncrypted(Boolean.FALSE);
		credentialsDao.create(cred);
		
		//PARTY 1
		Party party1 = new Party();		
		party1.setName(PARTY_NAME);
		party1.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party1);
		party1.setThirdPartyFlag(Boolean.FALSE);
		party1.setCredentials(cred);
		partyDao.create(party1);
		
		//PARTY 2
		Party party2 = new Party();		
		party2.setName(PARTY_NAME_2);
		party2.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party2);
		party2.setThirdPartyFlag(Boolean.TRUE);					
		partyDao.create(party2);
		
		//PARTY 3
		Party party3 = new Party();		
		party3.setName(PARTY_NAME_3);
		party3.setBusinessDomain(businessDomain);
		businessDomain.getParty().add(party3);
		party3.setThirdPartyFlag(Boolean.TRUE);					
		PartyIdentifier pi = new PartyIdentifier();
		pi.setSchemeId(IdentifierIssuingAgency.GLN);
		pi.setValue(PARTY_NAME_PI_3);
		pi.setParty(party3);
		party3.getIdentifiers().add(pi);
		partyDao.create(party3);
	
		flush();
		
		List<Party> resultList = partyDao.getPartiesForBusinessDomain(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain2.getId()))));
		Assert.assertEquals("Should not find any party", 0, resultList.size());
		
		resultList = partyDao.getPartiesForBusinessDomain(PARTY_NAME_3, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))));
		Assert.assertEquals("Should find 1 Party", 1, resultList.size());	
		
		resultList = partyDao.getPartiesForBusinessDomain(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))));
		Assert.assertEquals("Should find 3 Parties", 3, resultList.size());		
		
		resultList = partyDao.getPartiesForBusinessDomain(null, null, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))));
		Assert.assertEquals("Should find 3 Parties", 3, resultList.size());		
		
		resultList = partyDao.getPartiesForBusinessDomain(null, CREDENTIALS_NAME, null, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))));
		Assert.assertEquals("Should only find one party with credentials", 1, resultList.size());
		
		resultList = partyDao.getPartiesForBusinessDomain(null, null, PARTY_NAME_PI_3, new ArrayList<Long>((Arrays.asList(businessDomain.getId()))));
		Assert.assertEquals("Should only find one party with that Party Identifier", 1, resultList.size());
	}
	

}
