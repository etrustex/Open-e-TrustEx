package eu.europa.ec.etrustex.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.types.FrequencyType;

public class LogDAOTest extends AbstractEtrustExTest {

	@Autowired private ILogDAO logDao;
	
	private static final int TEST_SLA_PARTY_ID = -20;
	
	@Test public void testGetVolumeCountForParty(){
		//logDaogetVolumeCountForParty(long partyId, FrequencyType frequencyType) {
	}
	
	
}
