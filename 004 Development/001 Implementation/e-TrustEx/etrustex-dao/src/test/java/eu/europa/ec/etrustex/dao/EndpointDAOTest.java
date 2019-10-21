package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.ProxyCredentials;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;

public class EndpointDAOTest extends AbstractEtrustExTest {
	
	@Autowired private IEndpointDAO 			endpointDao;
	@Autowired private IPartyDAO 				partyDao;
	@Autowired private ITransactionDAO			transactionDao;
	@Autowired private ICredentialsDAO  		credentialsDao;
	@Autowired private IBusinessDomainDAO		bdDao;
	@Autowired private IProfileDAO 				profileDao;
	@Autowired private IInterchangeAgreementDAO icaDao;
	
	
	@Test public void testFindByCredentialsTypeAndUsername(){
		List<? extends Endpoint> epList;
		
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("bd-test2");
		bd2.setResponseSignatureRequired(false);
		bdDao.create(bd2);
		
		Credentials c1 = new PartyCredentials();
		c1.setUser("Cred-01");
		c1.setPassword("XXX");
		c1.setSignatureRequired(false);
		c1.setPasswordEncrypted(false);
		credentialsDao.create(c1);
		
		Credentials c2 = new PartyCredentials();
		c2.setUser("Cred-02");
		c2.setPassword("XXX");
		c2.setSignatureRequired(false);
		c2.setPasswordEncrypted(false);
		credentialsDao.create(c2);
		
		ProxyCredentials pc = new ProxyCredentials();
		pc.setUser("PCred-01");
		pc.setPassword("XXX");
		pc.setSignatureRequired(false);
		pc.setPasswordEncrypted(false);
		credentialsDao.create(pc);
		
		ProxyCredentials pc2 = new ProxyCredentials();
		pc2.setUser("PCred-02");
		pc2.setPassword("XXX");
		pc2.setSignatureRequired(false);
		pc2.setPasswordEncrypted(false);
		credentialsDao.create(pc2);
		
		endpointDao.create(buildWSEndpoint(bd, null, null, c1, null, null, null));
		endpointDao.create(buildWSEndpoint(bd2, null, null, null, pc, null, null));
		
		Credentials criteria = new PartyCredentials();
		criteria.setUser("Cred-0");
		
		epList = endpointDao.findByCredentialsTypeAndUsernameEquals(criteria, bd.getId(), WSEndpoint.class);
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.findByCredentialsTypeAndUsernameLike(criteria, bd.getId(), WSEndpoint.class);
		Assert.assertEquals(1, epList.size());
		Assert.assertEquals(c1.getId(), epList.get(0).getCredentials().getId());
		
		criteria = new ProxyCredentials();
		criteria.setUser("Cred-0");
		
		epList = endpointDao.findByCredentialsTypeAndUsernameEquals(criteria, bd2.getId(), WSEndpoint.class);
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.findByCredentialsTypeAndUsernameLike(criteria, bd2.getId(), WSEndpoint.class);
		Assert.assertEquals(1, epList.size());
		Assert.assertEquals(pc.getId(), epList.get(0).getProxyCredential().getId());
	}

	@Test public void testFindByCredentialsTypeAndUsernameBug(){
		Credentials criteria = new ProxyCredentials();
		criteria.setUser("PCred");
		
		ProxyCredentials pc = new ProxyCredentials();
		pc.setUser("PCred-01");
		pc.setPassword("XXX");
		pc.setSignatureRequired(false);
		pc.setPasswordEncrypted(false);
		credentialsDao.create(pc);
		
		ProxyCredentials pc2 = new ProxyCredentials();
		pc2.setUser("PCred-02");
		pc2.setPassword("XXX");
		pc2.setSignatureRequired(false);
		pc2.setPasswordEncrypted(false);
		credentialsDao.create(pc2);
		
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		Endpoint edp  = buildWSEndpoint(bd, null, null, null, pc, null, null);
		edp.setProxyHost("alihost");
		edp.setProxyPort(1111);
		endpointDao.create(edp);
		Endpoint edp2 = buildWSEndpoint(bd, null, null, null, pc2, null, null);
		edp2.setProxyHost("alihost");
		edp2.setProxyPort(1111);
		endpointDao.create(edp2);
		
		
		List<? extends Endpoint> list = endpointDao.findByCredentialsTypeAndUsernameLike(criteria, bd.getId(), WSEndpoint.class);
		System.err.println(list.size());
	}
	
	@Test public void testFindByCredentialsTypeAndUsernameEquals(){} 
	
	@Test public void testFindEndpointsByCriteria(){
		List<? extends Endpoint> epList;
		
		Credentials c2 = new PartyCredentials();
		c2.setUser("Cred-02");
		c2.setPassword("XXX");
		c2.setSignatureRequired(false);
		c2.setPasswordEncrypted(false);
		credentialsDao.create(c2);
		
		ProxyCredentials pc = new ProxyCredentials();
		pc.setUser("PCred-01");
		pc.setPassword("XXX");
		pc.setSignatureRequired(false);
		pc.setPasswordEncrypted(false);
		credentialsDao.create(pc);
		
		Party p = buildParty("TEST-P1");
		partyDao.create(p);
		
		Transaction t = new Transaction();
		t.setName("tx1");
		transactionDao.create(t);
		
		Profile profile = new Profile();
		profile.setName("P2");	
		profileDao.create(profile);
		
		BusinessDomain bd = new BusinessDomain();
		bd.setName("bd-test1");
		bd.setResponseSignatureRequired(false);
		bdDao.create(bd);
		
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setProfile(profile);
		icaDao.create(ica);
		
		Endpoint criteria = buildWSEndpoint(bd, profile, ica, c2, pc, p, t);
		
		epList = endpointDao.findEndpointsByCriteria(criteria);
		Assert.assertEquals(0, epList.size());
		
		endpointDao.create(criteria);
		epList = endpointDao.findEndpointsByCriteria(criteria);
		Assert.assertEquals(1, epList.size());
		
		criteria = new WSEndpoint();
		criteria.setIsActive(false);
		criteria.setBusinessDomain(bd);
		epList = endpointDao.findEndpointsByCriteria(criteria);
		Assert.assertEquals(0, epList.size());
	} 
	
	@Test public void testFindByCredentials(){
		List<? extends Endpoint> epList;
		
		Credentials c = new PartyCredentials();
		c.setUser("Cred-01");
		c.setPassword("XXX");
		c.setSignatureRequired(false);
		c.setPasswordEncrypted(false);
		credentialsDao.create(c);
		
		Credentials c2 = new PartyCredentials();
		c2.setUser("Cred-02");
		c2.setPassword("XXX");
		c2.setSignatureRequired(false);
		c2.setPasswordEncrypted(false);
		credentialsDao.create(c2);
		
		ProxyCredentials pc = new ProxyCredentials();
		pc.setUser("PCred-01");
		pc.setPassword("XXX");
		pc.setSignatureRequired(false);
		pc.setPasswordEncrypted(false);
		credentialsDao.create(pc);
		
		ProxyCredentials pc2 = new ProxyCredentials();
		pc2.setUser("PCred-02");
		pc2.setPassword("XXX");
		pc2.setSignatureRequired(false);
		pc2.setPasswordEncrypted(false);
		credentialsDao.create(pc2);
		
		endpointDao.create(buildWSEndpoint(null, null, null, c, null, null, null));
		endpointDao.create(buildWSEndpoint(null, null, null, c, null, null, null));
		
		endpointDao.create(buildWSEndpoint(null, null, null, null, pc2, null, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, pc2, null, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, pc2, null, null));
		
		epList = endpointDao.findByCredentials(c);
		Assert.assertEquals(2, epList.size());
		
		epList = endpointDao.findByCredentials(c2);
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.findByCredentials(pc);
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.findByCredentials(pc2);
		Assert.assertEquals(3, epList.size());
	} 
	
	@Test public void testGetThirdPartyEndpointsForTransaction(){} 
	
	@Test public void testGetTransactionEndpoints(){
		List<? extends Endpoint> epList;
		
		Transaction t = new Transaction();
		t.setName("tx1");
		transactionDao.create(t);
		
		Transaction t2 = new Transaction();
		t2.setName("tx2");
		transactionDao.create(t2);
		
		Transaction t3 = new Transaction();
		t3.setName("tx3");
		transactionDao.create(t3);
		
		Party party1 = buildParty("P1");
		Party party2 = buildParty("P2");
		Party party3 = buildParty("P3");
		
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party1, t));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, t));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, t));
		
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party3, t2));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, t2));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, t2));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, t2));
		
		epList = endpointDao.getTransactionEndpoints(t.getId(), party1.getId());
		Assert.assertEquals(1, epList.size());
		Assert.assertEquals(t.getId(), epList.get(0).getTansaction().getId());
		Assert.assertEquals(party1.getId(), epList.get(0).getParty().getId());
		
		epList = endpointDao.getTransactionEndpoints(t2.getId(), party2.getId());
		Assert.assertEquals(3, epList.size());
		Assert.assertEquals(t2.getId(), epList.get(0).getTansaction().getId());
		
		epList = endpointDao.getTransactionEndpoints(t3.getId(), null);
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.getTransactionEndpoints(t.getId(), party2.getId());
		Assert.assertEquals(2, epList.size());
		
		epList = endpointDao.getTransactionEndpoints(t2.getId(), party3.getId());
		Assert.assertEquals(1, epList.size());
		Assert.assertEquals(t2.getId(), epList.get(0).getTansaction().getId());
	} 
	
	@Test public void testGetInterchangeAgreementEndpoints(){
		List<? extends Endpoint> epList;
		
		Party party1 = buildParty("P1");
		Party party2 = buildParty("P2");
		
		Profile profile = new Profile();
		profile.setName("P2");	
		profileDao.create(profile);
		
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setProfile(profile);
		icaDao.create(ica);
		
		InterchangeAgreement ica2 = new InterchangeAgreement();
		ica2.setProfile(profile);
		icaDao.create(ica2);
		
		InterchangeAgreement ica3 = new InterchangeAgreement();
		ica3.setProfile(profile);
		icaDao.create(ica3);
		
		endpointDao.create(buildWSEndpoint(null, null, ica, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, ica, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, ica, null, null, party2, null));
		
		endpointDao.create(buildWSEndpoint(null, null, ica2, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, ica2, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, ica2, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, ica2, null, null, party1, null));
		
		epList = endpointDao.getInterchangeAgreementEndpoints(ica.getId(), party1.getId());
		Assert.assertEquals(2, epList.size());
		Assert.assertEquals(party1.getId(), epList.get(0).getParty().getId());
		Assert.assertEquals(ica.getId(), epList.get(0).getInterchangeAgreement().getId());
		
		epList = endpointDao.getInterchangeAgreementEndpoints(ica2.getId(), party1.getId());
		Assert.assertEquals(4, epList.size());
		Assert.assertEquals(party1.getId(), epList.get(0).getParty().getId());
		Assert.assertEquals(ica2.getId(), epList.get(0).getInterchangeAgreement().getId());
	} 
	
	@Test public void testGetProfileEndpoints(){
		List<? extends Endpoint> epList;
		
		Party party1 = buildParty("P1");
		Party party2 = buildParty("P2");
		
		Profile profile = new Profile();
		profile.setName("P1");	
		profileDao.create(profile);
		
		Profile profile2 = new Profile();
		profile2.setName("P2");	
		profileDao.create(profile2);
		
		Profile profile3 = new Profile();
		profile3.setName("P3");	
		profileDao.create(profile3);
		
		endpointDao.create(buildWSEndpoint(null, profile, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, profile, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, profile, null, null, null, party1, null));
		
		endpointDao.create(buildWSEndpoint(null, profile2, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, profile2, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, profile2, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, profile2, null, null, null, party1, null));
		
		epList = endpointDao.getProfileEndpoints(profile.getId(), party2.getId());
		Assert.assertEquals(0, epList.size());
		
		epList = endpointDao.getProfileEndpoints(profile.getId(), party1.getId());
		Assert.assertEquals(3, epList.size());
		Assert.assertEquals(party1.getId(), epList.get(0).getParty().getId());
		Assert.assertEquals(profile.getId(), epList.get(0).getProfile().getId());
		
		epList = endpointDao.getProfileEndpoints(profile2.getId(), party1.getId());
		Assert.assertEquals(4, epList.size());
		
		epList = endpointDao.getProfileEndpoints(profile3.getId(), party1.getId());
		Assert.assertEquals(0, epList.size());
	}
	
	@Test public void testGetThirdPartyEndpoints(){} 
	
	@Test public void testFindEndpointsByParty(){
		List<? extends Endpoint> epList;
		
		Party party1 = buildParty("P1");
		Party party2 = buildParty("P2");
		Party party3 = buildParty("P3");
		
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party1, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party1, null));
		
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, null));
		endpointDao.create(buildWSEndpoint(null, null, null, null, null, party2, null));

		epList = endpointDao.findEndpointsByParty(party1);
		Assert.assertEquals(3, epList.size());
		Assert.assertEquals(party1.getId(), epList.get(0).getParty().getId());
		
		epList = endpointDao.findEndpointsByParty(party2);
		Assert.assertEquals(4, epList.size());
		Assert.assertEquals(party2.getId(), epList.get(0).getParty().getId());
		
		epList = endpointDao.findEndpointsByParty(party3);
		Assert.assertEquals(0, epList.size());
	} 
	
	private Endpoint buildWSEndpoint(BusinessDomain bd, Profile pro, InterchangeAgreement ica, Credentials c, ProxyCredentials pc, Party p, Transaction tx){
		WSEndpoint edp = new WSEndpoint();
		populateEndpoint(edp);
		
		edp.setBusinessDomain(bd);
		edp.setProfile(pro);
		edp.setInterchangeAgreement(ica);
		edp.setProxyCredential(pc);
		edp.setCredentials(c);
		edp.setParty(p);
		edp.setTansaction(tx);
		edp.setWsEndpointURL("TEST"+UUID.randomUUID());
		edp.setSignCall(false);
		edp.setCheckResponseSignature(false);
		edp.setProxyHost("Testhost");
		edp.setProxyPort(1235);
		return edp;
	}
	
	private void populateEndpoint(Endpoint edp){
		edp.setIsActive(true);
	}
	
	private Party buildParty(String name){
		Party party = new Party();
		party.setName(name);
		partyDao.create(party);
		return party;
	}
	
}
