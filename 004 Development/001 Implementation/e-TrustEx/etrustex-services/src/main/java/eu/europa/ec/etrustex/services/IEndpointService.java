package eu.europa.ec.etrustex.services;

import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.routing.Endpoint;

public interface IEndpointService {
	
	Set<Endpoint> retrieveEndpoints(Long icaId, Long partyId, Long profileId, Long transactionId);
	Endpoint findById(Long id);
	Endpoint create(Endpoint endpoint, String userId);
	Endpoint update(Endpoint endpoint, String userId);
	void delete(Endpoint endpoint);
	List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class endppointClass);
}
