package eu.europa.ec.etrustex.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.PartyCredentials;

public class CredentialsServiceTest extends AbstractEtrustExTest {
	
	@Autowired private ICredentialsService credentialsService;
	@Mock	   private ICredentialsDAO credentialsDao;
	
	@Before public void testInit(){ 
		MockitoAnnotations.initMocks(this);		
		ReflectionTestUtils.setField(credentialsService, "credentialsDao", credentialsDao);
	}
	
	@Test public void testExists(){
		Credentials cred = new PartyCredentials();
		credentialsService.exists(cred);
		verify(credentialsDao, times(1)).exists(cred);
	}

	@Test public void testIsUniqueProxyCredentials(){
		String username   = "U";
		String proxyHost  = "H";
		Integer proxyPort = 1;
		credentialsService.isUniqueProxyCredentials(username, proxyHost, proxyPort);
		verify(credentialsDao, times(1)).isUniqueProxyCredentials(username, proxyHost, proxyPort);
	}

	@Test public void testFindByTypeAndUsername(){
		Credentials cred = new PartyCredentials();
		credentialsService.findByTypeAndUsername(cred);
		verify(credentialsDao, times(1)).findByTypeAndUsername(cred);
	}
		
	@Test public void testGet(){
		Long credId = 1L;
		credentialsService.get(credId);
		verify(credentialsDao, times(1)).read(credId);
	}	
	
}
