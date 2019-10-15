package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.CIALevel;

public class CIALevelDAOTest extends AbstractEtrustExTest {
	
	@Autowired private ICIALevelDAO cIALevelDAO;
	
	@Test public void testCRUD(){
		CIALevel cia1 = new CIALevel();
		cia1.setConfidentialityLevel(999);
		cia1.setIntegrityLevel(999);
		cia1.setAvailabilityLevel(999);	
		cia1 = cIALevelDAO.create(cia1);
		
		Assert.assertNotNull(cIALevelDAO.read(cia1.getId()));
		
		cia1.setConfidentialityLevel(888);
		cIALevelDAO.update(cia1);
		
		Assert.assertEquals("Value not Updated",(Integer)888, cIALevelDAO.read(cia1.getId()).getConfidentialityLevel());
		
		Assert.assertTrue(cIALevelDAO.getAll().size() >= 1);
		
		cIALevelDAO.delete(cia1);
		
		Assert.assertNull(cIALevelDAO.read(cia1.getId()));
	}
	
	@Test public void testRetrieveCIALevel(){
		CIALevel cia1 = new CIALevel();
		cia1.setConfidentialityLevel(999);
		cia1.setIntegrityLevel(999);
		cia1.setAvailabilityLevel(999);	
		cia1 = cIALevelDAO.create(cia1);
		
		List<CIALevel> levels = cIALevelDAO.retrieveCIALevel(999, 999, 999);
		Assert.assertTrue(levels.size() == 1);
		Assert.assertEquals((Integer)999,levels.get(0).getAvailabilityLevel());
		
		levels = cIALevelDAO.retrieveCIALevel(999, null, null);
		Assert.assertTrue(levels.isEmpty());
	}
}
