package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.IHumanReadableTemplateDAO;
import eu.europa.ec.etrustex.domain.HumanReadableTemplate;


public class HumanReadableTemplateServiceTest extends AbstractEtrustExTest{
	
	private final static Logger log = LoggerFactory.getLogger(HumanReadableTemplateServiceTest.class);
    private final static String hrName = "DEFAULT";
    
    @Autowired private IHumanReadableTemplateService humanReadableTemplateService;
	@Mock 	   private IHumanReadableTemplateDAO humanReadableTemplateDAO;
	
	@Before public void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(humanReadableTemplateService, "humanReadableTemplateDAO", humanReadableTemplateDAO);
	}

	@Test public void testFindByDocumentAndTransactionAndName(){
		Long documentId = 1L;
		Long transactionId = 2L;
		String hrName = "HR";
		
		when(humanReadableTemplateDAO.findByDocumentAndTransactionAndName(null, null, hrName)).thenReturn(null);
		assertNull(humanReadableTemplateService.findByDocumentAndTransactionAndName(null, null, hrName));
		verify(humanReadableTemplateDAO, times(1)).findByDocumentAndTransactionAndName(null, null, hrName);
		
		when(humanReadableTemplateDAO.findByTransactionAndName(transactionId, hrName)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		assertNotNull(humanReadableTemplateService.findByDocumentAndTransactionAndName(documentId, transactionId, hrName));
		verify(humanReadableTemplateDAO, times(1)).findByTransactionAndName(transactionId, hrName);
		
		when(humanReadableTemplateDAO.findByTransactionAndName(transactionId, hrName)).thenReturn(null);
		when(humanReadableTemplateDAO.findByDocumentIdAndName(documentId, hrName)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		assertNotNull(humanReadableTemplateService.findByDocumentAndTransactionAndName(documentId, transactionId, hrName));
		verify(humanReadableTemplateDAO, times(2)).findByTransactionAndName(transactionId, hrName);
		verify(humanReadableTemplateDAO, times(1)).findByDocumentIdAndName(documentId, hrName);
	}
	
	@Test public void testFindByDefault(){
		Long documentId = 1L;
		Long transactionId = 2L;
		
		when(humanReadableTemplateDAO.findByDefault(documentId, null)).thenReturn(new ArrayList<HumanReadableTemplate>());
		try {
			assertNull(humanReadableTemplateService.findByDefault(documentId, null));
		} catch (TechnicalErrorException e) {fail("Shouldn't fail");}
		verify(humanReadableTemplateDAO, times(1)).findByDefault(documentId, null);
		
		when(humanReadableTemplateDAO.findByDefault(documentId, null)).thenReturn(Arrays.asList(new HumanReadableTemplate(), new HumanReadableTemplate()));
		try {
			humanReadableTemplateService.findByDefault(documentId, null);
			fail("Should have thrown an exception");
		} catch (TechnicalErrorException e) {}
		verify(humanReadableTemplateDAO, times(2)).findByDefault(documentId, null);
		
		when(humanReadableTemplateDAO.findByDefault(documentId, null)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		try {
			assertNotNull(humanReadableTemplateService.findByDefault(documentId, null));
		} catch (TechnicalErrorException e) {fail("Shouldn't fail");}
		verify(humanReadableTemplateDAO, times(3)).findByDefault(documentId, null);
		
		when(humanReadableTemplateDAO.findByDefault(null, transactionId)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		try {
			assertNotNull(humanReadableTemplateService.findByDefault(documentId, transactionId));
		} catch (TechnicalErrorException e) {fail("Shouldn't fail");}
		verify(humanReadableTemplateDAO, times(1)).findByDefault(null, transactionId);
		
		when(humanReadableTemplateDAO.findByDefault(null, transactionId)).thenReturn(new ArrayList<HumanReadableTemplate>());
		when(humanReadableTemplateDAO.findByDefault(documentId, null)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		try {
			assertNotNull(humanReadableTemplateService.findByDefault(documentId, transactionId));
		} catch (TechnicalErrorException e) {fail("Shouldn't fail");}
		verify(humanReadableTemplateDAO, times(2)).findByDefault(null, transactionId);
		verify(humanReadableTemplateDAO, times(4)).findByDefault(documentId, null);
		
	}
	
	@Test public void testCreate(){
		HumanReadableTemplate hr = new HumanReadableTemplate();
		when(humanReadableTemplateDAO.create(hr)).thenReturn(hr);
		humanReadableTemplateService.create(hr);
		verify(humanReadableTemplateDAO, times(1)).create(hr);
	}
	
	@Test public void testDelete(){
		Long id = 1L;
		HumanReadableTemplate hr = new HumanReadableTemplate();
		hr.setId(id);
		
		when(humanReadableTemplateDAO.read(id)).thenReturn(null);
		humanReadableTemplateService.delete(id);
		verify(humanReadableTemplateDAO, times(0)).delete(any(HumanReadableTemplate.class));
		
		when(humanReadableTemplateDAO.read(id)).thenReturn(hr);
		humanReadableTemplateService.delete(id);
		verify(humanReadableTemplateDAO, times(1)).delete(any(HumanReadableTemplate.class));
	}
	
	@Test public void testUpdate(){
		HumanReadableTemplate hr = new HumanReadableTemplate();
		humanReadableTemplateService.update(hr);
		verify(humanReadableTemplateDAO, times(1)).update(hr);
	}
	
	@Test public void testFindByDocumentIdAndTransactionId(){
		Long documentId = 1L;
		Long transactionId = 2L;
		
		when(humanReadableTemplateDAO.findByDocumentIdAndTransactionId(documentId, transactionId)).thenReturn(Arrays.asList(new HumanReadableTemplate()));
		assertNotNull(humanReadableTemplateService.findByDocumentIdAndTransactionId(documentId, transactionId));
		verify(humanReadableTemplateDAO, times(1)).findByDocumentIdAndTransactionId(documentId, transactionId);
	}
	
	@Test
	public void testCRUD() {
		System.out.println("------Create-Read-Update-Delete------");
//		HumanReadableTemplate newHR = null;//generateHR();
//		log.info("Test started.....");
//		Long id = humanReadableTemplateService.create(newHR);
//		System.out.println(" HR has been added id :"+id);
//		
//		HumanReadableTemplate hr = humanReadableTemplateDAO.read(id);
//		assertNotNull(hr);
//		System.out.println("has been updated : "+hr.getId());
//		EntityAccessInfo info = new EntityAccessInfo();
//		info.setCreationDate(new Date());
//		hr.setAccessInfo(info);
//		hr = humanReadableTemplateService.update(hr);
//		System.out.println("HR has been updated : "+hr.getId());
//		
//		System.out.println("--------delete HR---------");
//		humanReadableTemplateService.delete(id);
//		HumanReadableTemplate expectedObject  = humanReadableTemplateDAO.read(id);
//		assertNull(expectedObject);
//		System.out.println("id:"+id+" has been deleted");
	}
	
//	@Test
//	public void testFindByDocumentAndTransactionAndName(){
//		System.out.println("--------findByDocumentAndTransactionAndName---------");
//		HumanReadableTemplate hrTemplate = humanReadableTemplateService.findByDocumentAndTransactionAndName(null, Long.valueOf(prop.getProperty("transaction.id")), "Test");
//		assertNotNull("HR not Exist ", hrTemplate);
//	}
//	
//	@Test
//	public void testNotExistName(){
//		System.out.println("--------findByDocumentAndTransactionAndName---------");
//		HumanReadableTemplate list = humanReadableTemplateService.findByDocumentAndTransactionAndName(null, Long.valueOf(prop.getProperty("transaction.id")), hrName +" 01");
//		assertEquals("HR not Exist ",null, list);
//	}
//
//	@Test
//	public void testDefaultTemplate(){
//		System.out.println("--------findByDefault--(At Transaction level)-------");
//		try {
//			HumanReadableTemplate hrTemplate = humanReadableTemplateService.findByDefault(null, Long.valueOf(prop.getProperty("transaction.id")));
//			assertNotNull("No HR template is configured", hrTemplate);
//		} catch (TechnicalErrorException e){
//			e.getStackTrace();
//		}
//	}
//	
//	private HumanReadableTemplate generateHR(){		
//		HumanReadableTemplate hr = new HumanReadableTemplate();		
//		hr.setName("DEFAULT");
//		Transaction transaction = transactionService.getTransaction(Long.valueOf(prop.getProperty("transaction.id")));
//		hr.setTansaction(transaction);
//		hr.setDocument(null);
//		hr.setIsDefault(Boolean.TRUE);
//		hr.setValue("<?xml version=1.0?>");
//		return hr;
//	}
	
	

}
