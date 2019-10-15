package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

public class PartyIndentifierDAOTest extends AbstractEtrustExTest {

	@Autowired private IPartyIdentifierDAO piDao;
	@Autowired private IPartyDAO 		   partyDao;
	 
	@Test public void testCreate(){				
		Party party1 = new Party();
		party1.setName("P1-Create");
		
		PartyIdentifier pi1 = new PartyIdentifier();
		pi1.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		pi1.setValue("PI-1");
		pi1.setParty(party1);
		piDao.create(pi1);

		Assert.assertNotNull(pi1.getId());
	}
	
	@Test public void testexists(){
		PartyIdentifier pi1 = new PartyIdentifier();
		pi1.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		pi1.setValue("PI-1");
		piDao.create(pi1);
		
		PartyIdentifier criteria = new PartyIdentifier();
		criteria.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		criteria.setValue("PI-1");
		
		Assert.assertTrue(piDao.exists(criteria));
		
		criteria.setSchemeId(IdentifierIssuingAgency.GLN);
		Assert.assertFalse(piDao.exists(criteria));
		
		criteria.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		criteria.setValue("PI-2");
		Assert.assertFalse(piDao.exists(criteria));
	} 
	
	@Test public void testGetBySchemeAndValue(){
		PartyIdentifier pi1 = new PartyIdentifier();
		pi1.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		pi1.setValue("PI-1");		
		piDao.create(pi1);
		
		PartyIdentifier criteria = new PartyIdentifier();
		criteria.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		criteria.setValue("PI-1");
		
		List<PartyIdentifier> piList = piDao.getBySchemeAndValue(criteria); 
		Assert.assertEquals(1, piList.size());
		
		
		criteria.setSchemeId(IdentifierIssuingAgency.GLN);
		piList = piDao.getBySchemeAndValue(criteria); 
		Assert.assertEquals(0, piList.size());
	} 
	
	@Test public void testUpdate(){
		PartyIdentifier pi1 = new PartyIdentifier();
		pi1.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		pi1.setValue("PI-1");		
		piDao.create(pi1);
		
		Assert.assertNotNull(pi1.getId());
		Assert.assertNull(pi1.getParty());
		
		Party party1 = new Party();
		party1.setName("P1");
		
		piDao.flushEm();
		
		pi1.setParty(party1);
		piDao.update(pi1);
		Assert.assertNotNull(pi1.getParty());
		Assert.assertNotNull(pi1.getParty().getId());
	} 
	
	@Test public void testDelete(){
		PartyIdentifier pi1 = new PartyIdentifier();
		pi1.setSchemeId(IdentifierIssuingAgency.AD_VAT);
		pi1.setValue("PI-1");		
		piDao.create(pi1);
		Long piId = pi1.getId();
		
		Assert.assertNotNull(piId);
		
		piDao.delete(piId);
		
		Assert.assertNull(piDao.read(piId));
	} 
}
