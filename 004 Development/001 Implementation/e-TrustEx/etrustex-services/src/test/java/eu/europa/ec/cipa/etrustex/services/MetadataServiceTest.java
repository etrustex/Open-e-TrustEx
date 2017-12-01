package eu.europa.ec.cipa.etrustex.services;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Map;
import java.util.Set;

@ContextConfiguration(locations={"/etrustex-services-test-context.xml"})
public class MetadataServiceTest extends AbstractJUnit4SpringContextTests{
	
	
	@Autowired
	private IMetadataService metadataService;

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}
	@Ignore
	@Test
	public void testRetrieveResponseCodes() throws Exception{ 
		//metadataService.retrieveMetaData(null, null, new Long(8), null);
		Map<String, String> codes = metadataService.retrieveResponseCodeValues(null, null, null, null);
		Set<String> keys = codes.keySet();
		for (String string : keys) {
			System.out.println(string + " : " + codes.get(string) );
		}
		Assert.assertNotNull(codes);
		Assert.assertFalse(keys.isEmpty());
	}

/*	@Test
	public void testRetrieveResponseCodes(){
		assertTrue(true);
	}*/
}
