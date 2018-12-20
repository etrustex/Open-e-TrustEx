package eu.europa.ec.etrustex.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Certificate;

public class CertificateDaoTest extends AbstractEtrustExTest {

	@Autowired private ICertificateDAO certificateDao;
	
	@Test public void testDelete(){
		Certificate c = new Certificate();
		c.setUsage("Usage");
		c.setType("Type");
		c.setSerialNumber("SN");
		c.setIssuer("Issuer");
		c.setHolder("Holder");
		c.setEncodedData("Data");
		c.setIsActive(Boolean.TRUE);
		c.setIsRevoked(Boolean.TRUE);
		
		c = certificateDao.create(c);
		Assert.assertNotNull("ID Not Generated", c.getId());
		
		certificateDao.delete(c.getId());
		
		Assert.assertNull("Certificate not Deleted", certificateDao.read(c.getId()));
	}
}
