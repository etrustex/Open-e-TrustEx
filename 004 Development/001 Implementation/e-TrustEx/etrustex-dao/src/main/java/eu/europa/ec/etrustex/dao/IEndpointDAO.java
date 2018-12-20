package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.routing.Endpoint;

import java.util.List;

public interface IEndpointDAO extends ITrustExDAO<Endpoint, Long>{
	
	List<Endpoint> getTransactionEndpoints(Long transactionId, Long receiverPartyId);
	List<Endpoint>  getInterchangeAgreementEndpoints(Long icaId , Long receiverPartyId);
	List<Endpoint> getProfileEndpoints(Long profileId, Long receiverPartyId);
	List<Endpoint> getThirdPartyEndpointsForProfile(Long profileId, Long receiverPartyId, Long transactionId);
    List<? extends Endpoint> findEndpointsByParty(Party party);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endppointClass);
    List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endppointClass);
    List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint);
	List<Endpoint>  getThirdPartyEndpointsForTransaction(Long receiverPartyId, Long transactionId);
    List<? extends Endpoint> findByCredentials(Credentials credentials);
}
