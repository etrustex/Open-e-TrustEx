package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;

import java.util.List;

public interface IEndpointService {
	
	List<Endpoint> retrieveEndpoints(Long icaId,Long partyId, Long businessDomainId,Long transactionId);
	Endpoint findById(Long id);
	Endpoint create(Endpoint endpoint, String userId);
	Endpoint update(Endpoint endpoint, String userId);
	void delete(Endpoint endpoint);
	List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class endppointClass);
}
