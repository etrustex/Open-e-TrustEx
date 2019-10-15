package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.MonitoringQuery;

public interface IMonitoringService {
	
	void monitor();
	
	List<MonitoringQuery> runMonitoringQueries();
	

}
