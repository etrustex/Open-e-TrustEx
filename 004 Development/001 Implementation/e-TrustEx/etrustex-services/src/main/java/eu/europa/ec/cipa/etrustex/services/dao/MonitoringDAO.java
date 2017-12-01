package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.MonitoringQuery;

@Repository
public class MonitoringDAO extends TrustExDAO<MonitoringQuery, Long>implements IMonitoringDAO {
	
	@Override
	public List<MonitoringQuery> runMonitoringQueries() {
		List<MonitoringQuery> queries = new ArrayList<>();
		for (MonitoringQuery query : getAll()) {
			List<?> result = entityManager.createNativeQuery(query.getQuery()).getResultList();
			if (CollectionUtils.isNotEmpty(result)) {
				queries.add(query);
			};
		}
		return queries;
	}

}
