package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertTrue;
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

import eu.europa.ec.etrustex.dao.IDataExtractionDAO;
import eu.europa.ec.etrustex.dao.IMessageDAO;
import eu.europa.ec.etrustex.dao.IQueryResultDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;
import eu.europa.ec.etrustex.domain.query.QueryResult;

public class QueryServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IQueryService queryService;
	@Mock private IDataExtractionDAO decDAO;
	@Mock private ITransactionDAO txDAO;
	@Mock private IMessageDAO messageDAO;
	@Mock private IQueryResultDAO queryResultDAO;
	
	@Before
	public  void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(queryService, "decDAO", decDAO);
		ReflectionTestUtils.setField(queryService, "txDAO", txDAO);
		ReflectionTestUtils.setField(queryService, "messageDAO", messageDAO);
		ReflectionTestUtils.setField(queryService, "queryResultDAO", queryResultDAO);
	}
	
	@Test public void testGetXpathsForDocument(){
		String transactionRequestLocalName = "ln";
		String transactionLocalName		   = "txLn";
		Long id = 1L;
		
		Document doc = new Document();
		doc.setId(id);
		Transaction tx = new Transaction();
		tx.setDocument(doc);
		
		when(txDAO.getTransactionByNameSpace(transactionLocalName, transactionRequestLocalName)).thenReturn(null);
		assertTrue(queryService.getXpathsForDocument(transactionRequestLocalName, transactionLocalName).isEmpty());
		
		when(txDAO.getTransactionByNameSpace(transactionLocalName, transactionRequestLocalName)).thenReturn(tx);
		when(decDAO.getXpathsForDocument(id)).thenReturn(Arrays.asList(new DataExtractionConfig()));
		assertTrue(queryService.getXpathsForDocument(transactionRequestLocalName, transactionLocalName).size() == 1);
		
		tx.setDocument(null);
		assertTrue(queryService.getXpathsForDocument(transactionRequestLocalName, transactionLocalName).isEmpty());
	}
	
	@Test public void testSaveQueryResult(){
		String key = "key";
		String value = "value";
		Long messageId = 1L;
		Message msg = new Message();
		when(messageDAO.read(messageId)).thenReturn(msg);
		
		queryService.saveQueryResult(key, value, messageId);
		verify(queryResultDAO, times(1)).create(any(QueryResult.class));
	}
	
	@Test public void testRetieveQueryResultsForMessage(){
		Long id = 1L;
		queryService.retieveQueryResultsForMessage(id);
		verify(queryResultDAO, times(1)).getQRByMessageId(id);
	}
}
