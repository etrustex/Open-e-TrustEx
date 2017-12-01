package eu.europa.ec.cipa.etrustex.services;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.MonitoringQuery;

public interface IMonitoringService {
	
	void monitor();
	
	List<MonitoringQuery> runMonitoringQueries();
	

}
