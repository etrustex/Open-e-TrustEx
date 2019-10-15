package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.IMetadataDAO;
import eu.europa.ec.etrustex.dao.IMonitoringDAO;
import eu.europa.ec.etrustex.domain.MonitoringQuery;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class MonitoringServiceTest extends AbstractEtrustExTest {
	
	@Autowired @InjectMocks private IMonitoringService 	monitoringService;
	@Mock 	   				private IMonitoringDAO 		monitoringDao;
	@Mock 	   				private IMetadataDAO    	metadataDAO;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("MonitoringService");
		System.out.println("--------------------------------------------------");
	}
	
	@Before public void init2(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test public void testRunMonitoringQueries() {			
		List<MonitoringQuery> mQList = new ArrayList<MonitoringQuery>();
		mQList.add(new MonitoringQuery());
		mQList.add(new MonitoringQuery());
		Mockito.when(monitoringDao.runMonitoringQueries()).thenReturn(mQList);
		
		System.out.println("------------runMonitoringQueries------------");
		List<MonitoringQuery> list = monitoringService.runMonitoringQueries();
		assertNotNull(list);
		Assert.assertEquals(2, list.size());
	}
	
	@Test public void testMonitor() {
		Mockito.when(metadataDAO.getDefaultMetadataByType(MetaDataItemType.FILE_STORE_PATH.toString())).thenReturn(null);
		try{
			monitoringService.monitor();
			Assert.fail("Should throw a runtime exception if no filestore path is specified");
		}catch(RuntimeException e){}
		
		MetaDataItem md = new MetaDataItem();
		md.setRawItemType(MetaDataItemType.FILE_STORE_PATH.toString());
		List<MetaDataItem> list = new ArrayList<MetaDataItem>();
		list.add(md);
		Mockito.when(metadataDAO.getDefaultMetadataByType(MetaDataItemType.FILE_STORE_PATH.toString())).thenReturn(list);
		
		try{
			monitoringService.monitor();
			Assert.fail("Should throw a runtime exception if path is null");
		}catch(RuntimeException e){}
		
		md.setValue("blabla,blabla");
		Mockito.when(metadataDAO.getDefaultMetadataByType(MetaDataItemType.FILE_STORE_PATH.toString())).thenReturn(list);
		try{
			monitoringService.monitor();
			Assert.fail("Should throw a runtime exception if path doesn't exist");
		}catch(RuntimeException e){}		
	}

}
