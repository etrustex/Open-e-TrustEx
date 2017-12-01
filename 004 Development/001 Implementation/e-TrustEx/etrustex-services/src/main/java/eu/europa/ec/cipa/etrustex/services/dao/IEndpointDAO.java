package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;

import java.util.List;

public interface IEndpointDAO extends ITrustExDAO<Endpoint, Long>{
	
	List<Endpoint> getTransactionEndpoints(Long transactionId, Long receiverPartyId);
	List<Endpoint>  getInterchangeAgreementEndpoints(Long icaId , Long receiverPartyId);
	List<Endpoint> getProfileEndpoints(Long profileId, Long receiverPartyId);
	List<Endpoint> getThirdPartyEndpoints(Long profileId, Long receiverPartyId);
    List<? extends Endpoint> findEndpointsByParty(Party party);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class endppointClass);
    List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint);
	List<Endpoint>  getThirdPartyEndpointsForTransaction(Long receiverPartyId, Long transactionId);
    List<? extends Endpoint> findByCredentials(Credentials credentials);
}
