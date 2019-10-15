package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IMessagePriorityDAO;
import eu.europa.ec.etrustex.domain.MessagePriority;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;

public class MessagePriorityServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IMessagePriorityService messagePriorityService;
	@Mock	   private IMessagePriorityDAO messagePriorityDAO;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(messagePriorityService, "messagePriorityDAO", messagePriorityDAO);
	}

	@Test public void testFindBySenderOrTransaction() {
		Long senderId = 1L;
		Long transactionId = 1L;
		
		MessagePriority exactMatch = new MessagePriority();
		Party sender = new Party();
		sender.setId(senderId);
		exactMatch.setSender(sender);
		
		Transaction tran = new Transaction();
		tran.setId(transactionId);
		exactMatch.setTransaction(tran);
		
		when(messagePriorityDAO.findBySenderOrTransaction(senderId, transactionId)).thenReturn(null);
		assertNull(messagePriorityService.findBySenderOrTransaction(senderId, transactionId));
		
		
		MessagePriority txP = new MessagePriority();
		txP.setPriority(8);
		txP.setSender(sender);
		
		MessagePriority senderP = new MessagePriority();
		senderP.setPriority(6);
		senderP.setTransaction(tran);
		
		MessagePriority nonMatching = new MessagePriority();
		nonMatching.setPriority(10);
		
		when(messagePriorityDAO.findBySenderOrTransaction(senderId, transactionId)).thenReturn(Arrays.asList(exactMatch));
		MessagePriority returnedMP = messagePriorityService.findBySenderOrTransaction(senderId, transactionId);
		assertTrue(exactMatch.equals(returnedMP));
		
		when(messagePriorityDAO.findBySenderOrTransaction(senderId, transactionId)).thenReturn(Arrays.asList(txP, senderP, nonMatching));
		returnedMP = messagePriorityService.findBySenderOrTransaction(senderId, transactionId);
		assertTrue(txP.equals(returnedMP));
		
		txP.setPriority(2);
		returnedMP = messagePriorityService.findBySenderOrTransaction(senderId, transactionId);
		assertTrue(senderP.equals(returnedMP));
		
		when(messagePriorityDAO.findBySenderOrTransaction(senderId, transactionId)).thenReturn(Arrays.asList(txP, nonMatching));
		returnedMP = messagePriorityService.findBySenderOrTransaction(senderId, transactionId);
		assertTrue(txP.equals(returnedMP));
	}
	

}
