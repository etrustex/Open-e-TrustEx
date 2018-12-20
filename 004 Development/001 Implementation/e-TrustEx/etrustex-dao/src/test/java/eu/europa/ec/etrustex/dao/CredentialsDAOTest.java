package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.JMSCredentials;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.ProxyCredentials;
import eu.europa.ec.etrustex.domain.WSCredentials;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;

public class CredentialsDAOTest extends AbstractEtrustExTest {
	
	@Autowired private ICredentialsDAO credentialsDao;
	@Autowired private IEndpointDAO    endpointDao;
	
	@Test public void testIsUniqueProxyCredentials(){
		Credentials pc1 = buildCredentials(new ProxyCredentials(), "proxy-utest1", false);
		pc1 = credentialsDao.create(pc1);
		
		Credentials pc2 = buildCredentials(new ProxyCredentials(), "proxy-utest2", false);
		pc2 = credentialsDao.create(pc2);
		
		Endpoint edp1 = buildEndpoint();
		edp1.setProxyHost("host1");
		edp1.setProxyPort(111);
		edp1.setProxyCredential(pc1);
		endpointDao.create(edp1);
		
		Endpoint edp2 = buildEndpoint();
		edp2.setProxyHost("host2");
		edp2.setProxyPort(111);
		edp2.setProxyCredential(pc1);
		endpointDao.create(edp2);
		
		Boolean isUnique = credentialsDao.isUniqueProxyCredentials("proxy-utest1", "host1", 111);
		Assert.assertFalse(isUnique);
		
		isUnique = credentialsDao.isUniqueProxyCredentials("proxy-utest1", "host2", 111);
		Assert.assertFalse(isUnique);
		
		isUnique = credentialsDao.isUniqueProxyCredentials("proxy-utest1", "host11", 111);
		Assert.assertTrue(isUnique);
		
		isUnique = credentialsDao.isUniqueProxyCredentials("proxy-utest2", "host1", 111);
		Assert.assertTrue(isUnique);
		
		isUnique = credentialsDao.isUniqueProxyCredentials("proxy-utest1", "host1", 112);
		Assert.assertTrue(isUnique);
	}
	
	@Test public void testExists(){
		credentialsDao.create(buildCredentials(new WSCredentials(), "utest-user1", false));
		Credentials cred1 = buildCredentials(new WSCredentials(), "utest-user2", false);
		
		Boolean resultBool = credentialsDao.exists(buildCredentials(new WSCredentials(), "utest-user1", false));
		
		Assert.assertTrue(resultBool);
		
		resultBool = credentialsDao.exists(cred1);
		
		Assert.assertFalse(resultBool);
		
		resultBool = credentialsDao.exists(buildCredentials(new JMSCredentials(), "utest-user1", false));
		
		Assert.assertFalse(resultBool);
	} 
	
	@Test public void testIsSignatureRequired(){
		credentialsDao.create(buildCredentials(new PartyCredentials(), "utest-user-sigreq", true));
		credentialsDao.create(buildCredentials(new PartyCredentials(), "utest-user-signotreq", false));
		
		Boolean resultBool = credentialsDao.isSignatureRequired("utest-user-sigreq");
		Assert.assertTrue(resultBool);
		
		resultBool = credentialsDao.isSignatureRequired("utest-user-signotreq");
		Assert.assertFalse(resultBool);
		
		try {
			resultBool = credentialsDao.isSignatureRequired("utest-fakeUser");
			Assert.fail("Should not reach this stage");
		} catch (EmptyResultDataAccessException e) {}
	} 
	
	@Test public void testFindByTypeAndUsername(){
		credentialsDao.create(buildCredentials(new WSCredentials(), "utest-user1", false));
		Credentials cred1 = buildCredentials(new WSCredentials(), "utest-user2", false);
		
		List<Credentials> credList = credentialsDao.findByTypeAndUsername(buildCredentials(new WSCredentials(), "utest-user1", false));
		
		Assert.assertEquals(1, credList.size());
		
		credList = credentialsDao.findByTypeAndUsername(cred1);
		
		Assert.assertEquals(0, credList.size());
		
		credList = credentialsDao.findByTypeAndUsername(buildCredentials(new JMSCredentials(), "utest-user1", false));
		
		Assert.assertEquals(0, credList.size());
	}
	
	private Credentials buildCredentials(Credentials c, String username, boolean sig){
		c.setUser(username);
		c.setPassword("XXX");
		c.setSignatureRequired(sig);
		c.setPasswordEncrypted(false);
		
		return c;
	}
	
	private Endpoint buildEndpoint(){
		
		WSEndpoint edp = new WSEndpoint();
		edp.setIsActive(false);
		
		edp.setWsEndpointURL("TEST"+UUID.randomUUID());
		edp.setSignCall(false);
		edp.setCheckResponseSignature(false);
		
		return edp;
	}
}
