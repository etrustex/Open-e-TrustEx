package eu.europa.ec.etrustex.dao.impl;

import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IServiceEndpointDao;
import eu.europa.ec.etrustex.domain.ServiceEndpoint;
import eu.europa.ec.etrustex.domain.Transaction;

@Repository
public class ServiceEndpointDao extends TrustExDAO<ServiceEndpoint, Long> implements IServiceEndpointDao {

	@Override
	public boolean existsEndpointByNameAndTransaction(String endpointName, Transaction transaction) {
		long count = entityManager
				.createQuery("select count(se.id) from ServiceEndpoint se where se.endpointName =:endpointName and (se.transactions is empty or :transaction member of se.transactions)"
						, Long.class)
				.setParameter("endpointName", endpointName)
				.setParameter("transaction", transaction)
				.getSingleResult();
		return count > 0;
	}
}
