package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.dao.IEndpointDAO;
import eu.europa.ec.etrustex.dao.IInterchangeAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.IProfileDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.ProxyCredentials;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.WSCredentials;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.AMQPEndpoint;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.etrustex.services.util.EncryptionService;

public class EndpointServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IEndpointService endpointService;
	@Mock private IEndpointDAO endpointDAO; 
	@Mock private IPartyAgreementDAO partyAgreementDAO;
	@Mock private IPartyDAO partyDAO;
	@Mock private IBusinessDomainDAO businessDomainDAO;
	@Mock private ICredentialsDAO credentialsDAO;
	@Mock private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Mock private ITransactionDAO transactionDAO;
	@Mock private IProfileDAO profileDAO;
	@Mock private EncryptionService encryptionService;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(endpointService, "endpointDAO", endpointDAO);
		ReflectionTestUtils.setField(endpointService, "partyAgreementDAO", partyAgreementDAO);
		ReflectionTestUtils.setField(endpointService, "partyDAO", partyDAO);
		ReflectionTestUtils.setField(endpointService, "businessDomainDAO", businessDomainDAO);
		ReflectionTestUtils.setField(endpointService, "credentialsDAO", credentialsDAO);
		ReflectionTestUtils.setField(endpointService, "interchangeAgreementDAO", interchangeAgreementDAO);
		ReflectionTestUtils.setField(endpointService, "transactionDAO", transactionDAO);
		ReflectionTestUtils.setField(endpointService, "profileDAO", profileDAO);
		ReflectionTestUtils.setField(endpointService, "encryptionService", encryptionService);
	}
	
	@Test public void testRetrieveEndpoints(){
		Long icaId = 1L;
		Long partyId = 2L;
		Long profileId = 3L;
		Long transactionId = 4L;
		
		assertTrue(endpointService.retrieveEndpoints(icaId, null, profileId, transactionId).isEmpty());
		
		Endpoint ep1 = new WSEndpoint();
		Party p1 = new Party();
		ep1.setParty(p1);
		PartyAgreement pa1 = new PartyAgreement();
		
		Endpoint ep2 = new WSEndpoint();
		Party p2 = new Party();
		ep2.setParty(p2);
		PartyAgreement pa2 = new PartyAgreement();
		Transaction tx = new Transaction();
		tx.setId(transactionId);
		pa2.getTransactions().add(tx);
		
		when(endpointDAO.getThirdPartyEndpointsForProfile(profileId, partyId, transactionId)).thenReturn(Arrays.asList(ep1, ep2));
		when(partyAgreementDAO.retrievePartyAgreement(any(Party.class), same(p1))).thenReturn(pa1);
		when(partyAgreementDAO.retrievePartyAgreement(any(Party.class), same(p2))).thenReturn(pa2);
		
		endpointService.retrieveEndpoints(icaId, partyId, profileId, transactionId);
		
		verify(endpointDAO, times(1)).getInterchangeAgreementEndpoints(icaId, partyId);
		verify(endpointDAO, times(1)).getTransactionEndpoints(transactionId, partyId);
		verify(endpointDAO, times(1)).getThirdPartyEndpointsForTransaction(partyId, transactionId);
		verify(endpointDAO, times(1)).getProfileEndpoints(profileId, partyId);
		
		
	}
	
	@Test public void testCreate(){	
		//Non persisted credentials
		WSEndpoint endpoint = buildWSEndpoint();
		endpointService.create(endpoint, "userId");
		verify(endpointDAO, times(1)).create(any(Endpoint.class));
		
		//Persisted credentials
		endpoint = buildWSEndpoint();
		endpoint.getCredentials().setId(1L);
		endpoint.getProxyCredential().setId(2L);
		
		when(credentialsDAO.read(endpoint.getCredentials().getId())).thenReturn(endpoint.getCredentials());
		when(credentialsDAO.read(endpoint.getProxyCredential().getId())).thenReturn(endpoint.getProxyCredential());
		endpointService.create(endpoint, "userId");
		verify(endpointDAO, times(2)).create(any(Endpoint.class));
	}
	
	@Test public void testFindById() throws Exception, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
		Long edpID = 1L;
		when(endpointDAO.read(edpID)).thenReturn(null);
		assertNull(endpointService.findById(edpID));
		
		Endpoint edp = buildWSEndpoint();
		when(endpointDAO.read(edpID)).thenReturn(edp);
		when(encryptionService.decryptMessageInAES(any(String.class), any(byte[].class))).thenReturn("PWD");
		
		assertNotNull(endpointService.findById(edpID));
	}
	
	@Test public void testUpdate(){
		when(endpointDAO.read(any(Long.class))).thenReturn(buildWSEndpoint());
		endpointService.update(new WSEndpoint(), "userId");
		verify(endpointDAO, times(1)).update(any(Endpoint.class));
	}
	
	@Test public void testdelete(){
		WSEndpoint endpoint = buildWSEndpoint();
		endpoint.setId(1L);
		
		when(endpointDAO.read(endpoint.getId())).thenReturn(endpoint);
		endpointService.delete(endpoint);
		verify(endpointDAO, times(1)).delete(any(Endpoint.class));
	}
	
	@Test public void testfindEndpointsByCriteria(){}
    @Test public void testfindByCredentialsTypeAndUsernameLike(){}
    @Test public void testfindByCredentialsTypeAndUsernameEquals(){}
	
	private WSEndpoint buildWSEndpoint(){
		WSEndpoint endpoint = new WSEndpoint();
		endpoint.setIsActive(true);
		endpoint.setWsEndpointURL("MYURL");
		endpoint.setCheckResponseSignature(false);
		endpoint.setSignCall(false);
		
		BusinessDomain bd = new BusinessDomain();
		bd.setId(0L);
		endpoint.setBusinessDomain(bd);
		
		Profile p = new Profile();
		p.setId(0L);
		endpoint.setProfile(p);
		
		Transaction t = new Transaction();
		t.setId(0L);
		endpoint.setTansaction(t);
		
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setId(0L);
		endpoint.setInterchangeAgreement(ica);
		
		Party party = new Party();
		party.setId(1L);
		endpoint.setParty(party);
		
		endpoint.setCredentials(buildCredentials(new WSCredentials()));
		endpoint.setProxyCredential(buildCredentials(new ProxyCredentials()));
		
		return endpoint;
	}
	
	private Credentials buildCredentials(Credentials cred){
		cred.setUser("user");
		cred.setPassword("pwd");
		cred.setIv(new byte[10]);
		cred.setSalt(false);
		cred.setSignatureRequired(false);
		cred.setPasswordEncrypted(false);
		return cred;
	}
	
}
