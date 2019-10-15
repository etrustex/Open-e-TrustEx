package eu.europa.ec.etrustex.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IMonitoringDAO;
import eu.europa.ec.etrustex.domain.MonitoringQuery;

@Repository
public class MonitoringDAO extends TrustExDAO<MonitoringQuery, Long>implements IMonitoringDAO {
	
	@Override
	public List<MonitoringQuery> runMonitoringQueries() {
		List<MonitoringQuery> queries = new ArrayList<>();
		for (MonitoringQuery query : getAll()) {
			if (query.getQuery().startsWith("select count")) {
				BigDecimal count = (BigDecimal)entityManager.createNativeQuery(query.getQuery()).getSingleResult();
				if (count.longValue() > 0) {
					queries.add(query);
				}
			} else {
				List<?> result = entityManager.createNativeQuery(query.getQuery()).getResultList();
				if (CollectionUtils.isNotEmpty(result)) {
					queries.add(query);
				}
			}
		}
		return queries;
	}

}
