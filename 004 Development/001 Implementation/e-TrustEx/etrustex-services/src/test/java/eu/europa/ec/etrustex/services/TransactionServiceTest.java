package eu.europa.ec.etrustex.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.ICIALevelDAO;
import eu.europa.ec.etrustex.dao.IProfileDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;

public class TransactionServiceTest extends AbstractEtrustExTest {
	
	@Autowired ITransactionService transactionService;
	@Mock private ITransactionDAO transactionDAO;
	@Mock private ICIALevelDAO ciaLevelDAO;
	@Mock private IProfileDAO profileDAO;
	
	@Before public void testInit(){
		//Injection doesn't work with transactional service through @InjectMocks so we have to do it through reflection 
		MockitoAnnotations.initMocks(this);		
		ReflectionTestUtils.setField(transactionService, "transactionDAO", transactionDAO);
		ReflectionTestUtils.setField(transactionService, "ciaLevelDAO", ciaLevelDAO);
		ReflectionTestUtils.setField(transactionService, "profileDAO", profileDAO);
	}
	
	@Test public void testCreate(){
		Transaction transaction = new Transaction();
		CIALevel cia = new CIALevel();
		cia.setAvailabilityLevel(1);
		cia.setConfidentialityLevel(1);
		cia.setIntegrityLevel(1);
		transaction.setCiaLevel(cia);
		
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(new ArrayList<CIALevel>());
		transactionService.create(transaction);
		verify(transactionDAO,times(1)).create(any(Transaction.class));
		verify(ciaLevelDAO,times(1)).create(any(CIALevel.class));
		
		transaction = new Transaction();
		transaction.setCiaLevel(cia);
		
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(new ArrayList<CIALevel>(Arrays.asList(cia)));
		transactionService.create(transaction);
		verify(transactionDAO,times(2)).create(any(Transaction.class));
		verify(ciaLevelDAO,times(1)).create(any(CIALevel.class));
	}
	
	@Test public void testupdate(){
		Transaction transaction = new Transaction();
		CIALevel cia = new CIALevel();
		cia.setAvailabilityLevel(1);
		cia.setConfidentialityLevel(1);
		cia.setIntegrityLevel(1);
		transaction.setCiaLevel(cia);
		
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(new ArrayList<CIALevel>());
		transactionService.update(transaction);
		verify(transactionDAO,times(1)).update(any(Transaction.class));
		verify(ciaLevelDAO,times(1)).create(any(CIALevel.class));
		
		transaction = new Transaction();
		transaction.setCiaLevel(cia);
		
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(new ArrayList<CIALevel>(Arrays.asList(cia)));
		transactionService.update(transaction);
		verify(transactionDAO,times(2)).update(any(Transaction.class));
		verify(ciaLevelDAO,times(1)).create(any(CIALevel.class));
	}
	
	@Test public void testdelete(){	
		Long txId = 5L;
		Transaction transaction = new Transaction();
		transaction.setId(txId);
		
		Profile p1 = new Profile();
		p1.getTransactions().add(transaction);
		transaction.getProfiles().add(p1);
		
		when(transactionDAO.read(txId)).thenReturn(transaction);
		transactionService.delete(txId);
		assertTrue(p1.getTransactions().isEmpty());
		verify(profileDAO, times(1)).update(any(Profile.class));
		verify(transactionDAO,times(1)).delete(any(Transaction.class));
	}
	
	@Test public void testGetTransactionsByNameAndVersion(){
		String name = "name";
		String version = "2.0";
		transactionService.getTransactionsByNameAndVersion(name, version);
		verify(transactionDAO, times(1)).getTransactionsByNameAndVersion(name, version);
	}
	
	@Test public void testGetTransactionsByNamespaceAndRequestLocalName(){
		String namespace = "ns";
		String requestLocalName = "rln";
		transactionService.getTransactionsByNamespaceAndRequestLocalName(namespace, requestLocalName);
		verify(transactionDAO, times(1)).getTransactionsByNamespaceAndRequestLocalName(namespace, requestLocalName);
	}
	
	@Test public void testGetTransactionsByCriteria(){
		Transaction transaction = new Transaction();
		transaction.getProfiles().add(new Profile());
		when(transactionDAO.getTransactionsByCriteria(transaction)).thenReturn(new ArrayList<Transaction>());
		List<Transaction> txList = transactionService.getTransactionsByCriteria(transaction);
		assertTrue(txList.isEmpty());
		
		when(transactionDAO.getTransactionsByCriteria(transaction)).thenReturn(new ArrayList<Transaction>(Arrays.asList(transaction)));
		txList = transactionService.getTransactionsByCriteria(transaction);
		verify(transactionDAO, times(2)).getTransactionsByCriteria(transaction);
	}
	
	@Test public void testGetAll(){
		transactionService.getAll();
		verify(transactionDAO, times(1)).getAll();
	}
	
	@Test public void testIsInUse(){
		Transaction tx = new Transaction();
		
		when(transactionDAO.isInUse(tx)).thenReturn(true);
		assertTrue(transactionService.isInUse(tx));
		
		when(transactionDAO.isInUse(tx)).thenReturn(false);
		assertFalse(transactionService.isInUse(tx));
	}
}
