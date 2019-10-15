package eu.europa.ec.etrustex.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IMessageRoutingDAO;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.ProxyCredentials;
import eu.europa.ec.etrustex.domain.WSCredentials;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.etrustex.services.util.EncryptionService;

public class MessageRoutingServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IMessageRoutingService messageRoutingService;
	@Mock private EncryptionService encryptionService;
	@Mock private IMessageRoutingDAO messageRoutingDAO;
	
	
	@Before public void testinit(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(messageRoutingService, "messageRoutingDAO", messageRoutingDAO);
		ReflectionTestUtils.setField(messageRoutingService, "encryptionService", encryptionService);
	}
	
	@Test public void testSave(){
		MessageRouting messageRouting = new MessageRouting();
    	messageRoutingService.save(messageRouting);
    	verify(messageRoutingDAO, times(1)).create(messageRouting);
	}
	
	@Test public void testMarkAsProcessed(){
		Long id = 1L;
		MessageRouting messageRouting = new MessageRouting();
		when(messageRoutingDAO.read(id)).thenReturn(messageRouting);
		messageRoutingService.markAsProcessed(id);
		verify(messageRoutingDAO, times(1)).update(messageRouting);
	}
	
	@Test public void testFindById(){
		Long id = 1L;
		MessageRouting messageRouting = new MessageRouting();
		Endpoint edp1 = new WSEndpoint();
		edp1.setCredentials(buildCredentials(new WSCredentials()));
		edp1.setProxyCredential(buildCredentials(new ProxyCredentials()));
		messageRouting.setEndpoint(edp1);
		messageRouting.setMessage(new Message());
		when(messageRoutingDAO.read(id)).thenReturn(messageRouting);
		messageRoutingService.findById(id);	
	}

    @Test public void testFindByMessageId(){
    	Long messageId = 1L;
    	messageRoutingService.findByMessageId(messageId);
    	verify(messageRoutingDAO, times(1)).findByMessageId(messageId);
    }

    @Test public void testFindByEndpointId(){
    	Long endpointId = 1L;
    	messageRoutingService.findByEndpointId(endpointId);
    	verify(messageRoutingDAO, times(1)).findByEndpointId(endpointId);
    }

    @Test public void testFindByEndpointIdAndNotDispatched(){
    	Long endpointId = 1L;
    	messageRoutingService.findByEndpointIdAndNotDispatched(endpointId);
    	verify(messageRoutingDAO, times(1)).findByEndpointIdAndNotDispatched(endpointId);
    }

    @Test public void testFindByEndpointPartyId(){
    	Long partyId = 1L;
    	messageRoutingService.findByEndpointPartyId(partyId);
    	verify(messageRoutingDAO, times(1)).findByEndpointPartyId(partyId);
    }

    @Test public void testDeleteByMessageId(){
    	Long messageId = 1L;
    	when(messageRoutingDAO.findByMessageId(messageId)).thenReturn(Arrays.asList(new MessageRouting()));
    	messageRoutingService.deleteByMessageId(messageId);
    	verify(messageRoutingDAO, times(1)).delete(any(MessageRouting.class));
    }
    
    private Credentials buildCredentials(Credentials cred){
		cred.setId(0L);
		cred.setUser("user");
		cred.setPassword("pwd");
		cred.setIv(new byte[10]);
		cred.setSalt(false);
		cred.setSignatureRequired(false);
		cred.setPasswordEncrypted(false);
		return cred;
	}
}
