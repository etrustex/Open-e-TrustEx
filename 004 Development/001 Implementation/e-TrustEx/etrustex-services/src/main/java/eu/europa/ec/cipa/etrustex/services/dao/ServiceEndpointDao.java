package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.ServiceEndpoint;

public class ServiceEndpointDao extends TrustExDAO<ServiceEndpoint, Long> implements IServiceEndpointDao {

	@Override
	public ServiceEndpoint getEndpointByName(String endpointName){
		List<ServiceEndpoint> list = entityManager.createQuery(
						"from ServiceEndpoint se where se.endpointName =:endpointName", ServiceEndpoint.class).setParameter("endpointName", endpointName).getResultList();
		if(list == null || list.size() == 0){
			return null;
		}else{
			return list.get(0);
		}
	}
}
