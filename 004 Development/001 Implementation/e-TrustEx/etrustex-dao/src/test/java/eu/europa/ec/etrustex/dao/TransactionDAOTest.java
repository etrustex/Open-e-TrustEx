package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;

public class TransactionDAOTest extends AbstractEtrustExTest {

	@Autowired private ITransactionDAO transactionDao;
	@Autowired private IProfileDAO     profileDAO;
	@Autowired private IMessageDAO 	   messageDao;
	@Autowired private IPartyDAO 	   partyDao;
	@Autowired private IDocumentDAO    documentDAO;
	@Autowired private IRoleDAO    	   roleDAO;
	
	private Transaction transaction1;
	private Transaction transaction2;
	
	@Before public void prepareTransactions(){
		Profile p1 = new Profile();
		p1.setName("P1");
		profileDAO.create(p1);
		
		Profile p2 = new Profile();
		p2.setName("P2");
		profileDAO.create(p2);
		
		transaction1 = new Transaction();
		transaction1.setName("Transaction1");
		transaction1.setNamespace("ns1");
		transaction1.setRequestLocalName("RQ_1");
		transaction1.setVersion("1.0");
		transaction1.getProfiles().add(p1);
		transactionDao.create(transaction1);
		
		transaction2 = new Transaction();
		transaction2.setName("Transaction2");
		transaction2.setNamespace("ns2");
		transaction2.setRequestLocalName("RQ_2");
		transaction2.setVersion("2.0");
		transaction2.getProfiles().add(p2);
		transactionDao.create(transaction2);
	}
	
	@Test public void testIsInUse(){
		Party p = new Party();
		p.setName("P1");
		partyDao.create(p);
		
		Message m = new Message();
		m.setDocumentId("ID1");
		m.setMessageDocumentTypeCode("DTC");
		m.setTransaction(transaction1);
		m.setIssuer(p);
		m.setSender(p);
		messageDao.create(m);
		
		Assert.assertTrue("Tx1 has a message associated to it",transactionDao.isInUse(transaction1));
		
		Assert.assertTrue("Tx2 is associated to a profile",transactionDao.isInUse(transaction2));
		
		Transaction tx = new Transaction();
		tx.setName("tx");		
		transactionDao.create(tx);
		Assert.assertFalse("Tx not associated",transactionDao.isInUse(tx));
		
		//TODO add other linked entities to the test
	}

	@Test public void testGetTransactionsByCriteria(){
		Document document1 = new Document();
		document1.setName("DOC_NAME_1");
		document1.setNamespace("NS_1");
		document1.setLocalName("DOC_LNAME_1");
		document1.setDocumentTypeCode("DOC_DTC_1");
		documentDAO.create(document1);
		
		Role role = new Role();
		role.setName("ANY");
		role.setCode("ANY_ROLE");
		roleDAO.create(role);
		
		transaction1.setSenderRole(role);
		transaction1.setReceiverRole(role);
		transaction1.setDocument(document1);
		transactionDao.update(transaction1);
			
		Document doc = new Document();
		doc.setName("DOC_NAME_1");		
		
		Transaction criteria = new Transaction();
		criteria.setName("Transaction1");
		criteria.setNamespace("ns1");
		criteria.setRequestLocalName("RQ_1");
		criteria.setDocument(doc);
		criteria.setSenderRole(role);
		criteria.setReceiverRole(role);
		
		List<Transaction> txList = transactionDao.getTransactionsByCriteria(criteria);
		
		//TODO finish criteria
		
		Assert.assertEquals(0, txList.size());
	}

	@Test public void testUpdate(){
		Profile p = new Profile();
		p.setName("P");
		profileDAO.create(p);
		
		Assert.assertEquals("Transaction1", transactionDao.read(transaction1.getId()).getName());
		
		transaction1.setName("NEW_NAME");
		transaction1.getProfiles();
		transaction1.getProfiles().add(p);
		transaction1 = transactionDao.update(transaction1);
		
		Assert.assertEquals("NEW_NAME", transactionDao.read(transaction1.getId()).getName());
		Assert.assertEquals(2, transactionDao.read(transaction1.getId()).getProfiles().size());
	}

	@Test public void testGetAll(){
		List<Transaction> txs = transactionDao.getAll();
		
		Transaction transaction = new Transaction();
		transaction.setName("transaction");		
		transactionDao.create(transaction);
		
		List<Transaction> txs2 = transactionDao.getAll();
		
		Assert.assertTrue("There should be at least two transactions ", txs2.size() - txs.size() == 1);
	}

	@Test public void testGetTransactionByNameSpace(){
		final String namespace = "NAMESPACE";
		final String localName = "LOCAL_NAME";
		
		Transaction tx = new Transaction();
		tx.setNamespace(namespace);
		tx.setRequestLocalName(localName);
		tx.setName("txName");
		tx = transactionDao.create(tx);
		
		Assert.assertNull(transactionDao.getTransactionByNameSpace("",""));
		
		Assert.assertNull(transactionDao.getTransactionByNameSpace(namespace,""));
		
		Assert.assertNull(transactionDao.getTransactionByNameSpace("",localName));
		
		Transaction tx2 = transactionDao.getTransactionByNameSpace(namespace, localName);
		
		Assert.assertNotNull(tx2);
		
		Assert.assertEquals(tx.getId(), tx2.getId());
	}

	@Test public void testCreate(){
		Transaction tx = new Transaction();
		tx.setName("txName");
		tx = transactionDao.create(tx);
		
		Assert.assertNotNull(tx);
		Assert.assertNotNull(tx.getId());
		
		//TODO add profiles
	}

	@Test public void testGetTransactionsForDocument(){
		Document document = new Document();
		document.setName("DOC_NAME");
		document.setNamespace("NS");
		document.setLocalName("DOC_LNAME");
		document.setDocumentTypeCode("DOC_DTC");
		documentDAO.create(document);
		
		Document document1 = new Document();
		document1.setName("DOC_NAME_1");
		document1.setNamespace("NS_1");
		document1.setLocalName("DOC_LNAME_1");
		document1.setDocumentTypeCode("DOC_DTC_1");
		documentDAO.create(document1);
		
		Document document2 = new Document();
		document2.setName("DOC_NAME_2");
		document2.setNamespace("NS_2");
		document2.setLocalName("DOC_LNAME_2");
		document2.setDocumentTypeCode("DOC_DTC_2");
		documentDAO.create(document2);
		
		transaction1.setDocument(document1);
		transactionDao.update(transaction1);
		transaction2.setDocument(document2);
		transactionDao.update(transaction2);
		
		List<Transaction> txList = transactionDao.getTransactionsForDocument("NS", "DOC_LNAME");
		
		Assert.assertTrue(txList.isEmpty());
		
		txList = transactionDao.getTransactionsForDocument("NS_1", "DOC_LNAME_1");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction1.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsForDocument("NS_2", "DOC_LNAME_2");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction2.getId(), txList.get(0).getId());
	}

	@Test public void testGetTransactionsByNameAndVersion(){
		List<Transaction> txList = transactionDao.getTransactionsByNameAndVersion("Transaction1", "1.0");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction1.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsByNameAndVersion("Transaction2", "2.0");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction2.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsByNameAndVersion("Transaction1", "2.0");
		Assert.assertTrue(txList.isEmpty());
		
		txList = transactionDao.getTransactionsByNameAndVersion("Transaction2", "1.0");
		Assert.assertTrue(txList.isEmpty());
	}

	@Test public void testGetTransactionsByNamespaceAndRequestLocalName(){
		List<Transaction> txList = transactionDao.getTransactionsByNamespaceAndRequestLocalName("ns1", "RQ_1");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction1.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsByNamespaceAndRequestLocalName("ns2", "RQ_2");
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction2.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsByNamespaceAndRequestLocalName("ns__", "RQ_1");
		Assert.assertTrue(txList.isEmpty());
		
		txList = transactionDao.getTransactionsByNamespaceAndRequestLocalName("ns1", "RQ_1__");
		Assert.assertTrue(txList.isEmpty());
	}

	@Test public void testGetTransactionsByDocumentTypeCd(){
		Document document = new Document();
		document.setName("DOC_NAME");
		document.setNamespace("NS");
		document.setLocalName("DOC_LNAME");
		document.setDocumentTypeCode("DOC_DTC");
		documentDAO.create(document);
		
		Document document1 = new Document();
		document1.setName("DOC_NAME_1");
		document1.setNamespace("NS_1");
		document1.setLocalName("DOC_LNAME_1");
		document1.setDocumentTypeCode("DOC_DTC_1");
		documentDAO.create(document1);
		
		Document document2 = new Document();
		document2.setName("DOC_NAME_2");
		document2.setNamespace("NS_2");
		document2.setLocalName("DOC_LNAME_2");
		document2.setDocumentTypeCode("DOC_DTC_2");
		documentDAO.create(document2);
		
		transaction1.setDocument(document1);
		transactionDao.update(transaction1);
		transaction2.setDocument(document2);
		transactionDao.update(transaction2);
		
		List<Transaction> txList = transactionDao.getTransactionsByDocumentTypeCd("DOC_DTC");
		
		Assert.assertTrue(txList.isEmpty());
		
		txList = transactionDao.getTransactionsByDocumentTypeCd("DOC_DTC_1");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction1.getId(), txList.get(0).getId());
		
		txList = transactionDao.getTransactionsByDocumentTypeCd("DOC_DTC_2");
		
		Assert.assertTrue(txList.size() == 1);
		Assert.assertEquals(transaction2.getId(), txList.get(0).getId());
	}
		
}
