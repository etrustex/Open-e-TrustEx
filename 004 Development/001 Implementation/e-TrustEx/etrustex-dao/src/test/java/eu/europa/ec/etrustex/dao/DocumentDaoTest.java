package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Transaction;

public class DocumentDaoTest extends AbstractEtrustExTest {

	@Autowired private IDocumentDAO documentDAO;
	@Autowired private ITransactionDAO transactionDAO;
	
	public static String DOC_NAME_1 	= "DOCUMENT_TEST_1";
	public static String DOC_LNAME_1 	= "DOCUMENT_LOCAL_N_TEST_1";
	public static String DOC_DTC_1 		= "DOCUMENT_TYPE_CODE_1";
	
	public static String DOC_NAME_2 	= "DOCUMENT_TEST_2";
	public static String DOC_LNAME_2 	= "DOCUMENT_LOCAL_N_TEST_2";
	public static String DOC_DTC_2 		= "DOCUMENT_TYPE_CODE_2";
	
	public static String DOC_NAME_3 	= "DOCUMENT_TEST_3";
	public static String DOC_LNAME_3 	= "DOCUMENT_LOCAL_N_TEST_3";
	public static String DOC_DTC_3 		= "DOCUMENT_TYPE_CODE_3";
	
	public static String DOC_UPD_NAME 	= "DOCUMENT_UPDATED_NAME";
		
	private Document document1;
	private Document document2;
	private Document document3;
	
	@Before public void prepareDocuments(){
		document1 = new Document();
		document1.setName(DOC_NAME_1);
		document1.setLocalName(DOC_LNAME_1);
		document1.setDocumentTypeCode(DOC_DTC_1);
		documentDAO.create(document1);
		
		document2 = new Document();
		document2.setName(DOC_NAME_2);
		document2.setLocalName(DOC_LNAME_2);
		document2.setDocumentTypeCode(DOC_DTC_2);
		documentDAO.create(document2);
		
		document3 = new Document();
		document3.setName(DOC_NAME_3);
		document3.setLocalName(DOC_LNAME_3);
		document3.setDocumentTypeCode(DOC_DTC_3);
		documentDAO.create(document3);
		
		flush();
	}
	
	@Test public void testDocumentCRUD(){
		document1.setName(DOC_UPD_NAME);
		documentDAO.update(document1);
		flush();
		Assert.assertEquals("Document not updated",DOC_UPD_NAME, documentDAO.read(document1.getId()).getName());
		Assert.assertTrue(documentDAO.getAll().size() >= 3);
		documentDAO.delete(document1.getId());
		documentDAO.delete(document2);		
		Assert.assertNull(documentDAO.read(document1.getId()));
		Assert.assertNull(documentDAO.read(document2.getId()));
	}
	
	@Test public void testGetDocuments(){		
		List<Document> doc1 	= documentDAO.getDocuments(document1.getName(), null, null);
		List<Document> doc1Bis 	= documentDAO.getDocuments(document1.getName(), document1.getLocalName(), document1.getDocumentTypeCode());
		List<Document> doc2 	= documentDAO.getDocuments(null, document2.getLocalName(), null);
		List<Document> doc3 	= documentDAO.getDocuments(null, null, document3.getDocumentTypeCode());
		
		Assert.assertEquals("Document ID is Wrong", document1.getId(), doc1.get(0).getId());
		Assert.assertEquals("Document Count is Wrong", 1, doc1.size());
		Assert.assertEquals("Document ID is Wrong", document1.getId(), doc1Bis.get(0).getId());
		Assert.assertEquals("Document Count is Wrong", 1, doc1Bis.size());
		Assert.assertEquals("Document ID is Wrong", document2.getId(), doc2.get(0).getId());
		Assert.assertEquals("Document Count is Wrong", 1, doc2.size());
		Assert.assertEquals("Document ID is Wrong", document3.getId(), doc3.get(0).getId());
		Assert.assertEquals("Document Count is Wrong", 1, doc3.size());
	}
	
	@Test public void testIsinUse(){
		Transaction tx = new Transaction();
		tx.setName("DummyName");
		tx.setDocument(document1);
		document1.getTransactions().add(tx);		
		transactionDAO.create(tx);
				
		Assert.assertTrue(documentDAO.isInUse(document1));
		Assert.assertFalse(documentDAO.isInUse(document2));
	}
	
}
