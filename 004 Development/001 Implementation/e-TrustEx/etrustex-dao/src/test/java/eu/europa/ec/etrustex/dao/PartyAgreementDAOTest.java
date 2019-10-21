package eu.europa.ec.etrustex.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import eu.europa.ec.etrustex.dao.exception.MissingImplementationException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

public class PartyAgreementDAOTest extends AbstractEtrustExTest {

	@Autowired private IPartyAgreementDAO paDao;
	@Autowired private IPartyDAO 		  partyDao;
	@Autowired private ITransactionDAO 	  transactionDao;
	@Autowired private IBusinessDomainDAO bdDao;
	
	private static final String AUTHORIZING_PARTY_1 = "AUTHORIZING_PARTY_1";
	private static final String DELEGATING_PARTY_1  = "DELEGATING_PARTY_1";
	
	private Party 			party1;
	private Party 			party2;
	private PartyAgreement 	pa1; 
	
	@Before 
	public void setupTests(){
		party1 = new Party();
		party1.setName(AUTHORIZING_PARTY_1);
		partyDao.create(party1);
		
		party2 = new Party();
		party2.setName(DELEGATING_PARTY_1);
		partyDao.create(party2);
		
		pa1 = new PartyAgreement();
		pa1.setAuthorizingParty(party1);
		pa1.setDelegateParty(party2);
		pa1 = paDao.create(pa1);
	}
	
	@Test public void testCreatePA(){
		Party partyOne = new Party();
		partyOne.setName("One");
		partyDao.create(partyOne);
		
		Party partyTwo = new Party();
		partyTwo.setName("two");
		partyDao.create(partyTwo);
		
		PartyAgreement p = new PartyAgreement();
		
		try{
			paDao.create(p);
			Assert.fail("Should not continue");
		}catch(DataIntegrityViolationException e){
			
		}catch(Exception e){
			Assert.fail("Should be DataIntegrityViolationException");
		}
		
		Long paId = paDao.create(partyOne, partyTwo);

		Assert.assertNotNull(paId);
	}
	
	@Test public void testExistsAgreementForParty(){
		boolean authPartyBool = paDao.existsAgreementForParty(party1);
		
		Assert.assertFalse("no authorizing party exist for AUTHORIZING_PARTY_1", authPartyBool);
		
		boolean delPartyBool = paDao.existsAgreementForParty(party2);
		
		Assert.assertTrue("DELEGATING_PARTY_1 has an agreement with AUTHORIZING_PARTY_1", delPartyBool);
		
		Party party = new Party();
		party.setName("New Party");
		partyDao.create(party);
		
		boolean newPartyBool = paDao.existsAgreementForParty(party);
		
		Assert.assertFalse("no PA should be present", newPartyBool);
	}
	
	@Test(expected=EmptyResultDataAccessException.class) public void testRetrievePartyAgreement(){
		PartyAgreement pa = paDao.retrievePartyAgreement(party1, party2);
		
		Assert.assertNotNull(pa);
		
		Assert.assertEquals(pa1.getId(), pa.getId());
		
		pa = paDao.retrievePartyAgreement(party2, party1);
	}
	
	@Test public void testFindPartyAgreementsForParty(){
		List<PartyAgreement> listOfPAs = paDao.findPartyAgreementsForParty(party1);
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
		
		listOfPAs = paDao.findPartyAgreementsForParty(party2);
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
		
		Party party = new Party();
		party.setName("New Party");
		partyDao.create(party);
		
		listOfPAs = paDao.findPartyAgreementsForParty(party);
		
		Assert.assertTrue(listOfPAs.size() == 0);
	}
	
	@Test public void testFindPartyAgreementsByCriteria(){
		BusinessDomain bd = new BusinessDomain();
		bd.setName("BD");
		bd.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd);
		
		party1.setBusinessDomain(bd);
		partyDao.update(party1);
		
		party2.setBusinessDomain(bd);
		partyDao.update(party2);
		
		Party party = new Party();
		party.setName("New Party");
		
		partyDao.create(party);
		
		final Transaction tx = new Transaction();
		tx.setName("myTX");
		transactionDao.create(tx);
		
		final Transaction tx2 = new Transaction();
		tx2.setName("myTX2");
		transactionDao.create(tx2);
		
		PartyAgreement pa = new PartyAgreement();
		pa.setAuthorizingParty(party1);
		pa.setDelegateParty(party);
		pa.setTransactions(new HashSet<Transaction>() {{add(tx);}});
		
		paDao.create(pa);
		
		List<PartyAgreement> listOfPAs = paDao.findPartyAgreementsByCriteria(party1, party2, null, null);
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
		
		listOfPAs = paDao.findPartyAgreementsByCriteria(party1, null, null, null);
		
		Assert.assertTrue(listOfPAs.size() == 2);
		
		listOfPAs = paDao.findPartyAgreementsByCriteria(null, party2, null, null);
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
		
		listOfPAs = paDao.findPartyAgreementsByCriteria(party1, null, tx, null);
		
		Assert.assertTrue(listOfPAs.size() == 2);

		listOfPAs = paDao.findPartyAgreementsByCriteria(party1, null, tx2, null);
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
		
		listOfPAs = paDao.findPartyAgreementsByCriteria(null, null, null, bd.getId());
		
		Assert.assertTrue(listOfPAs.size() == 1);
		
		Assert.assertEquals(pa1.getId(), listOfPAs.get(0).getId());
	}

	
	@Test public void testExistsPartyAgreement(){
		Assert.assertTrue(paDao.existsPartyAgreement(party1, party2));
		Assert.assertFalse(paDao.existsPartyAgreement(party2, party1));
		Assert.assertFalse(paDao.existsPartyAgreement(party1, new Party()));
	}
	
	@Test public void testGetPartyAgreement(){
		try {
			paDao.get(party1.getId(), party2.getId());
			Assert.fail("Exception Not Caught");
		} catch (MissingImplementationException e) {
			
		} catch (Exception e) {
			Assert.fail("Wrong Exception Caught");
		}
	}
	
	@Test public void testGetAuthorizedPartiesFor(){
		Assert.assertTrue("There should be one delegate Party", paDao.getAuthorisedPartiesFor(party1.getId()).size() == 1);
		
		Assert.assertTrue("Delegate Party is the one initialized", paDao.getAuthorisedPartiesFor(party1.getId()).get(0).getName().equals(DELEGATING_PARTY_1));
		
		Assert.assertTrue("There should be no delegate Party", paDao.getAuthorisedPartiesFor(party2.getId()).size() == 0);
		
		Assert.assertTrue("There should be no delegate Party", paDao.getAuthorisedPartiesFor(null).size() == 0);
	}
	
	@Test public void testGetDelegateThirdPartiesNamesFor(){
		Collection<String> thirdParty = paDao.getDelegateThirdPartiesNamesFor(party1.getId());
		
		Assert.assertTrue("The collection should not be empty", thirdParty.size() == 1);
		
		boolean b = thirdParty.contains(DELEGATING_PARTY_1);
		
		Assert.assertTrue("The collection contains the delegating party",b);
		
		Collection<String> thirdPartyEmpty = paDao.getDelegateThirdPartiesNamesFor(party2.getId());
		
		Assert.assertTrue("The collection should not be empty", thirdPartyEmpty.size() == 0);
	}
	
	@Test public void testGetDelegateThirdPartiesFor(){
		List<Party> thirdParty = paDao.getDelegateThirdPartiesFor(party1.getId());
		
		Assert.assertTrue("The list should not be empty", thirdParty.size() == 1);
		
		Assert.assertTrue("The list contains the delegating party",thirdParty.get(0).getName().equals(DELEGATING_PARTY_1));
		
		List<Party> thirdPartyEmpty = paDao.getDelegateThirdPartiesFor(party2.getId());
		
		Assert.assertTrue("The list should not be empty", thirdPartyEmpty.size() == 0);
	}
	
	@Test public void testFindPartyAgreementsForAuthorizingParty(){
		List<PartyAgreement> pas = paDao.findPartyAgreementsForAuthorizingParty(party1);
		
		Assert.assertTrue("Should find 1 PA", pas.size() == 1);
		
		Assert.assertEquals(pa1.getId(), pas.get(0).getId());
		
		pas = paDao.findPartyAgreementsForAuthorizingParty(party2);
		
		Assert.assertTrue("Should not find any PA", pas.size() == 0);
		
		try{
			pas = paDao.findPartyAgreementsForAuthorizingParty(null);
			Assert.fail("Should not proceed");
		}catch(NullPointerException e){
			
		}
	}
	
}
