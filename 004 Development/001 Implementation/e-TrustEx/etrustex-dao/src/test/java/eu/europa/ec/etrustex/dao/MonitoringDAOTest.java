package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.MonitoringQuery;

public class MonitoringDAOTest extends AbstractEtrustExTest {

	@Autowired private IMonitoringDAO monitoringDao;
	
	@Test public void testMonitoring(){
		List<MonitoringQuery> mqList = monitoringDao.runMonitoringQueries();
		
		int initSize = mqList.size();
		
		MonitoringQuery mq = new MonitoringQuery();
		mq.setQuery("SELECT * FROM DUAL");
		monitoringDao.create(mq);
		
		Assert.assertEquals(1, monitoringDao.runMonitoringQueries().size()-initSize);
	}
}
