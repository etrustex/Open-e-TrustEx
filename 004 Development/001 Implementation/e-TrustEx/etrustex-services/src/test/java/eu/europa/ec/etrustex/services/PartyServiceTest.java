package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.ICertificateDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.util.EncryptionService;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

public class PartyServiceTest extends AbstractEtrustExTest {

	@Autowired private IPartyService partyService;
	@Mock 	   private IPartyDAO partyDAO;
	@Mock 	   private IBusinessDomainDAO businessDomainDAO;
	@Mock	   private ICertificateDAO certificateDAO;
	@Mock      private IPartyAgreementDAO partyAgreementDAO;
	@Mock	   private EncryptionService encryptionService;
	
	@Before public void init2(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(partyService, "partyDAO", partyDAO);
		ReflectionTestUtils.setField(partyService, "businessDomainDAO", businessDomainDAO);
		ReflectionTestUtils.setField(partyService, "certificateDAO", certificateDAO);
		ReflectionTestUtils.setField(partyService, "partyAgreementDAO", partyAgreementDAO);
		ReflectionTestUtils.setField(partyService, "encryptionService", encryptionService);
	}

	@Test public void testCreate() {
		Party party = new Party();
		party.setName("Test");
		
		Long bdId = 1L;
		BusinessDomain bd = new BusinessDomain();
		bd.setId(bdId);
		party.setBusinessDomain(bd);
		
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationId("user");
		party.setAccessInfo(eai);
		
		Certificate cert = new Certificate();
		party.getCertificates().add(cert);
		
		Credentials cred = new PartyCredentials();
		cred.setUser("User ");
		cred.setPassword("password");
		cred.setPasswordEncrypted(true);
		party.setCredentials(cred);
		
		PartyIdentifier pi = new PartyIdentifier();
		pi.setValue("MyPI");
		party.getIdentifiers().add(pi);
		
		when(encryptionService.encryptPasswordByBCrypt(any(String.class))).thenReturn("Encrypted");
		when(businessDomainDAO.read(bdId)).thenReturn(bd);
		
		try {
			partyService.createParty(party);
		} catch (NoSuchAlgorithmException e) {}

		verify(partyDAO, times(1)).create(any(Party.class));
	}
	
	@Test public void testUpdate(){
		Long partyId = 5L;
		Party party = new Party();
		party.setId(partyId);
		party.setName("Test");
		
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationId("user");
		party.setAccessInfo(eai);
		
		Certificate cert1 = new Certificate();
		cert1.setId(1L);
		cert1.setSerialNumber("AAA");
		cert1.setType("AAA");
		cert1.setUsage("AAA");
		cert1.setIssuer("AAA");
		cert1.setHolder("AAA");
		cert1.setEncodedData("AAA");
		cert1.setAttributes("AAA");
		cert1.setVersion("AAA");
		cert1.setSignatureAlgorithm("AAA");
		cert1.setSignatureValue("AAA");

		party.getCertificates().add(cert1);
		Certificate cert2 = new Certificate();
		party.getCertificates().add(cert2);
		
		Credentials cred = new PartyCredentials();
		cred.setId(1L);
		cred.setUser("User ");
		cred.setPassword("password");
		cred.setPasswordEncrypted(true);
		cred.setAccessInfo(eai);
		party.setCredentials(cred);
		
		PartyIdentifier pid1 = new PartyIdentifier();
		pid1.setId(1L);
		pid1.setParty(party);
		party.getIdentifiers().add(pid1);
		
		PartyIdentifier pid2 = new PartyIdentifier();
		pid2.setId(2L);
		pid2.setParty(party);
		party.getIdentifiers().add(pid2);
		
		//Existing Party
		Party exParty = new Party();
		exParty.setId(2L);
		exParty.setAccessInfo(eai);
		exParty.setAccessInfo(eai);
		exParty.setCredentials(cred);
		
		Certificate cert3 = new Certificate();
		cert3.setId(3L);
		exParty.getCertificates().add(cert3);
		
		PartyIdentifier pid3 = new PartyIdentifier();
		pid3.setId(3L);
		pid3.setParty(exParty);
		exParty.getIdentifiers().add(pid3);
		
		when(encryptionService.encryptPasswordByBCrypt(any(String.class))).thenReturn("EncryptedPwd");
		when(certificateDAO.update(any(Certificate.class))).thenReturn(cert2);
		when(partyDAO.read(partyId)).thenReturn(null);
		
		try {
			partyService.updateParty(party, null);
			fail("Should not proceed");
		} catch (NoSuchAlgorithmException e) {}
		catch(RecordNotFoundException e1){}
		
		when(partyDAO.read(partyId)).thenReturn(exParty);
		
		try {
			partyService.updateParty(party, null);
		} catch (NoSuchAlgorithmException e) {fail("Should not fail");}
		verify(partyDAO,times(1)).update(any(Party.class));
	}
	
	@Test public void testGetParty(){
		assertNull(partyService.getParty(null));
		partyService.getParty(1L);
		verify(partyDAO, times(1)).read(1L);
	}
	
	@Test public void testGetPartyByID(){
		IdentifierIssuingAgency schemeId = IdentifierIssuingAgency.VA_VAT;
		String idValue = "MY_ISSUER";
		BusinessDomain businessDomain = new BusinessDomain();
		
		assertNull(partyService.getPartyByID(schemeId, null, businessDomain));
		
		partyService.getPartyByID(null, idValue, businessDomain);
		verify(partyDAO, times(1)).getParty(IdentifierIssuingAgency.GLN, idValue, businessDomain);
		partyService.getPartyByID(schemeId, idValue, businessDomain);
		verify(partyDAO, times(1)).getParty(schemeId, idValue, businessDomain);
	}
	
	@Test public void testFindPartiesByCriteria(){
		String name = "name";
		String username = "user";
		String identifierValue = "ID"; 
		List<Long> businessDomainIds = null;
		Boolean isThirdParty = true;
		when(partyDAO.findPartiesByCriteria(name, username, identifierValue, businessDomainIds, isThirdParty)).thenReturn(Arrays.asList(new Party()));
		partyService.findPartiesByCriteria(name, username, identifierValue, businessDomainIds, isThirdParty);
		verify(partyDAO, times(1)).findPartiesByCriteria(name, username, identifierValue, businessDomainIds, isThirdParty);
	}
	
	
	@Test public void testGetAllPartiesForBusinessDomain(){
		Long bdID = 1L;
		partyService.getAllPartiesForBusinessDomain(bdID);
		verify(partyDAO, times(1)).getAllPartiesForBusinessDomain(bdID);
	}
	
	@Test public void testGetPartyForBusinessDomain(){
		String partyName = "PN  ";
		Long bdID = 1L;
		partyService.getPartyForBusinessDomain(partyName, bdID);
		verify(partyDAO, times(1)).getPartyForBusinessDomain(partyName, bdID);
	}
	
	@Test public void testGetAllThirdPartiesFor(){
		Long partyId = 1L;
		partyService.getAllThirdPartiesFor(partyId);
		verify(partyAgreementDAO, times(1)).getDelegateThirdPartiesFor(partyId);
	}
	
	@Test public void testDeleteById(){
		Long partyId = 1L;
		Party party = new Party();
		when(partyDAO.read(partyId)).thenReturn(party);
		
		partyService.deleteById(partyId);
		verify(partyDAO, times(1)).delete(party);
	}
	
	@Test public void testIsUniquePartyNamePerDomain(){
		String partyName = "PN  ";
		Long bdID = 1L;
		
		partyService.isUniquePartyNamePerDomain(partyName, bdID);
		verify(partyDAO, times(1)).isUniquePartyPerBusinessDomain(partyName.trim(), bdID);
		
		partyService.isUniquePartyNamePerDomain(null, bdID);
		verify(partyDAO, times(1)).isUniquePartyPerBusinessDomain(null, bdID);
	}

}
