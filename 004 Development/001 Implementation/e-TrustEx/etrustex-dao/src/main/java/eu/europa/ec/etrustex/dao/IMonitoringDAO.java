package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.MonitoringQuery;

public interface IMonitoringDAO extends ITrustExDAO<MonitoringQuery, Long> {
	
	/**
	 * executes the monitoring queries from the DB
	 * @return a list of queries that returned results or count > 0
	 */
	public List<MonitoringQuery> runMonitoringQueries();

}
