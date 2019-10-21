package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;

public class MessageRoutingDAOTest extends AbstractEtrustExTest {

	@Autowired private IMessageRoutingDAO mrDao;
	@Autowired private IEndpointDAO 	  endpointDao;
	@Autowired private IMessageDAO 	  	  messageDao;
	@Autowired private IPartyDAO 	  	  partyDao;
	@Autowired private ITransactionDAO 	  transactionDao;
	
	private MessageRouting mr1;
	private Message 	   msg1;
	private JMSEndpoint    edp1;
	private Transaction    transaction1;
	private Party 		   p1;
	
	@Before public void prepareMessageRouting(){
		p1 = new Party();
		p1.setName("P1");
		partyDao.create(p1);
		
		transaction1 = new Transaction();
		transaction1.setName("Transaction1");
		transaction1.setNamespace("ns1");
		transaction1.setRequestLocalName("RQ_1");
		transaction1.setVersion("1.0");
		transactionDao.create(transaction1);
		
		edp1 = new JMSEndpoint();
		edp1.setParty(p1);
		edp1.setDestinationJndiName("D");
		edp1.setIsSupportingReplyTo(false);
		edp1.setIsActive(true);
		endpointDao.create(edp1);
		
		msg1 = new Message();
		msg1.setDocumentId("ID1");
		msg1.setMessageDocumentTypeCode("DTC");
		msg1.setTransaction(transaction1);
		msg1.setIssuer(p1);
		msg1.setSender(p1);
		messageDao.create(msg1);
		
		mr1 = new MessageRouting(edp1,msg1);
		mr1.setProcessed(false);
		mrDao.create(mr1);
	}
	
	@Test public void testDeleteByMessageId(){
		List<MessageRouting> mrList = mrDao.findByMessageId(msg1.getId());
		Assert.assertFalse("Should not be empty",mrList.isEmpty());
		Assert.assertEquals(1, mrList.size());
		
		mrDao.deleteByMessageId(msg1.getId());
		mrList = mrDao.findByMessageId(msg1.getId());
		Assert.assertTrue("Should be empty",mrList.isEmpty());
	}
	
	@Test public void testFindByMessageId(){
		List<MessageRouting> mrList = mrDao.findByMessageId(msg1.getId());
		Assert.assertFalse("Should not be empty",mrList.isEmpty());
		Assert.assertEquals(1, mrList.size());
		Assert.assertEquals(mr1.getId(), mrList.get(0).getId());
		
		Message msg = new Message();
		msg.setDocumentId("ID-New1");
		msg.setMessageDocumentTypeCode("DTC");
		msg.setTransaction(transaction1);
		msg.setIssuer(p1);
		msg.setSender(p1);
		msg = messageDao.create(msg);
		
		mrList = mrDao.findByMessageId(msg.getId());
		Assert.assertTrue("Should be empty",mrList.isEmpty());
	}
	
	@Test public void testFindByEndpointId(){
		List<MessageRouting> mrList = mrDao.findByEndpointId(edp1.getId());
		Assert.assertFalse("Should not be empty",mrList.isEmpty());
		Assert.assertEquals(1, mrList.size());
		Assert.assertEquals(mr1.getId(), mrList.get(0).getId());
		
		JMSEndpoint edp = new JMSEndpoint();
		edp.setDestinationJndiName("D");
		edp.setIsSupportingReplyTo(false);
		edp.setIsActive(true);
		endpointDao.create(edp);
		
		mrList = mrDao.findByEndpointId(edp.getId());
		Assert.assertTrue("Should be empty",mrList.isEmpty());
	} 
	
	@Test public void testFindByEndpointIdAndNotDispatched(){
		List<MessageRouting> mrList = mrDao.findByEndpointIdAndNotDispatched(edp1.getId());
		Assert.assertFalse("Should not be empty",mrList.isEmpty());
		Assert.assertEquals(1, mrList.size());
		Assert.assertEquals(mr1.getId(), mrList.get(0).getId());
		
		mr1.setProcessed(true);
		mrDao.update(mr1);
		mrList = mrDao.findByEndpointIdAndNotDispatched(edp1.getId());
		Assert.assertTrue("Should be empty",mrList.isEmpty());
	}
	
	@Test public void testFindByEndpointPartyId(){
		List<MessageRouting> mrList = mrDao.findByEndpointPartyId(p1.getId());
		Assert.assertFalse("Should not be empty",mrList.isEmpty());
		Assert.assertEquals(1, mrList.size());
		Assert.assertEquals(mr1.getId(), mrList.get(0).getId());
		
		Party p = new Party();
		p.setName("P1-NEW");
		partyDao.create(p);
		
		mrList = mrDao.findByEndpointPartyId(p.getId());
		Assert.assertTrue("Should be empty",mrList.isEmpty());
	}
}
