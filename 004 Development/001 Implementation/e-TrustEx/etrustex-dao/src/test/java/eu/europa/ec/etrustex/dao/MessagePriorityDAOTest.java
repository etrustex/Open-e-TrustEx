package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.MessagePriority;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;

public class MessagePriorityDAOTest extends AbstractEtrustExTest {

	@Autowired private IMessagePriorityDAO mpDao;
	@Autowired private IPartyDAO 		   partyDao;
	@Autowired private ITransactionDAO 	   transactionDao;
	
	@Test public void testFind(){
		Party party1 = new Party();
		party1.setName("UTEST_PARTY_1");
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("UTEST_PARTY_2");
		party2 = partyDao.create(party2);
		
		Transaction transaction1 = new Transaction();
		transaction1.setName("UTEST_TRANSACTION_1");
		transactionDao.create(transaction1);		
		
		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION_2");
		transactionDao.create(transaction2);
		
		MessagePriority mp0 = new MessagePriority();
		mp0.setPriority(1);
		mpDao.create(mp0);
		
		MessagePriority mp1 = new MessagePriority();
		mp1.setPriority(1);
		mp1.setSender(party1);
		mp1.setTransaction(transaction1);
		mpDao.create(mp1);
		
		MessagePriority mp2 = new MessagePriority();
		mp2.setPriority(1);
		mp2.setSender(party2);
		mp2.setTransaction(transaction2);
		mpDao.create(mp2);
		
		MessagePriority mp3 = new MessagePriority();
		mp3.setPriority(1);
		mp3.setSender(party1);
		mpDao.create(mp3);
		
		MessagePriority mp4 = new MessagePriority();
		mp4.setPriority(1);
		mp4.setTransaction(transaction1);
		mpDao.create(mp4);
		
		MessagePriority mp5 = new MessagePriority();
		mp5.setPriority(1);
		mp5.setSender(party2);
		mpDao.create(mp5);
		
		MessagePriority mp6 = new MessagePriority();
		mp6.setPriority(1);
		mp6.setTransaction(transaction2);
		mpDao.create(mp6);
		
		List<MessagePriority> mpList = mpDao.findBySenderOrTransaction(party1.getId(), transaction1.getId());
		Assert.assertEquals(3, mpList.size());
		
		mpList = mpDao.findBySenderOrTransaction(party1.getId(), transaction2.getId());
		Assert.assertEquals(2, mpList.size());
		
		mpList = mpDao.findBySenderOrTransaction(party2.getId(), transaction1.getId());
		Assert.assertEquals(2, mpList.size());
		
		mpList = mpDao.findBySenderOrTransaction(party2.getId(), transaction2.getId());
		Assert.assertEquals(3, mpList.size());
	}
}
