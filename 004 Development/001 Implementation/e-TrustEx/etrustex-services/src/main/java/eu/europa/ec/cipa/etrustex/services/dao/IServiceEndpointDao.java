package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.ServiceEndpoint;
import eu.europa.ec.cipa.etrustex.domain.Transaction;

public interface IServiceEndpointDao extends ITrustExDAO<ServiceEndpoint, Long> {
		
	public ServiceEndpoint getEndpointByName(String endpointName);
	
}
