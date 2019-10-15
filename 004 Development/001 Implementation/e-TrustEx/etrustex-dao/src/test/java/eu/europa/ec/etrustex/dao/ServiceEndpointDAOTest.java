package eu.europa.ec.etrustex.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.ServiceEndpoint;


public class ServiceEndpointDAOTest extends AbstractEtrustExTest {

	@Autowired private IServiceEndpointDao serviceEndpointDao;
	
	@Test public void testCRUD(){
		ServiceEndpoint se = new ServiceEndpoint();
		se.setEndpointName("MyEndpoint1");
		se = serviceEndpointDao.create(se);
		
		Assert.assertNotNull(se.getId());
		
		Assert.assertTrue(serviceEndpointDao.existsEndpointByNameAndTransaction("MyEndpoint1", null));
				
		Assert.assertFalse(serviceEndpointDao.existsEndpointByNameAndTransaction("MyEndpoint1-TEstValue", null));
	}
}
