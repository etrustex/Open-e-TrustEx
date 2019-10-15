package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.ServiceEndpoint;
import eu.europa.ec.etrustex.domain.Transaction;

public interface IServiceEndpointDao extends ITrustExDAO<ServiceEndpoint, Long> {
		
	public boolean existsEndpointByNameAndTransaction(String endpointName, Transaction transaction);
	
}
