package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
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

import eu.europa.ec.etrustex.dao.ICIALevelDAO;
import eu.europa.ec.etrustex.domain.CIALevel;

public class CIALevelServiceTest extends AbstractEtrustExTest {
	
	@Autowired private ICIALevelService ciaLevelService;
	@Mock	   private ICIALevelDAO ciaLevelDao;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(ciaLevelService, "ciaLevelDao", ciaLevelDao);
	}
	
	@Test
	public void testGetCIALevelById() {
		ciaLevelService.getCIALevel(1L);
		verify(ciaLevelDao, times(1)).read(1L); 
	}
	
	@Test
	public void testRetrieveCIALevel() {
		when(ciaLevelDao.retrieveCIALevel(1, 1, 1)).thenReturn(null);
		assertNull(ciaLevelService.retrieveCIALevel(1, 1, 1));
		
		when(ciaLevelDao.retrieveCIALevel(1, 1, 1)).thenReturn(Arrays.asList(new CIALevel()));
		assertNotNull(ciaLevelService.retrieveCIALevel(1, 1, 1));
	}

}
