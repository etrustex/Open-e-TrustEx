package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Credentials;

import java.util.List;

public interface ICredentialsDAO extends ITrustExDAO<Credentials, Long>{
	
	Boolean isSignatureRequired(String authenticatedUser);
	
	Boolean exists(Credentials credentials);

    Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort);

    // NO LONGER NEEDED AFTER ETRUSTEX-1902  type PROVIDER --> WS, JMS, AMQP
//    Boolean isUniqueProviderCredentials(Class endpointClass, String url, String username);

    List<Credentials> findByTypeAndUsername(Credentials credentials);

    List<Credentials> findByTypeAndUsernameLike(Credentials credentials);
}
