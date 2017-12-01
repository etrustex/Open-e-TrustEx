package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.MonitoringQuery;

public interface IMonitoringDAO extends ITrustExDAO<MonitoringQuery, Long> {
	
	public List<MonitoringQuery> runMonitoringQueries();

}
