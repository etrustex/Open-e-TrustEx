package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Credentials;

import java.util.List;

public interface ICredentialsDAO extends ITrustExDAO<Credentials, Long>{
	
	/**
	 * find if signature validation must be performed on incoming request or if ack must be signed by 
	 * ETX before sending it in the response
	 * @param authenticatedUser the username used for basic authentication
	 * @return the flag signagure required
	 */
	Boolean isSignatureRequired(String authenticatedUser);
	
	/**
	 * check if credential exists
	 * @param credentials the DTO containing the information on the searched credential
	 * @return true if credential exists
	 */
	Boolean exists(Credentials credentials);

	/**
	 * check if a Proxy credential already exists for the given parameters
	 * @param username the credential username
	 * @param proxyHost the proxy host
	 * @param proxyPort the proxy port
	 * @return true if credential exists, false otherwise
	 */
    Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort);

    /**
     * find credential with the properties given in the param DTO
     * @param credentials the DTO containing the information on the searched credential
     * @return the Credential found in the DB
     */
    List<Credentials> findByTypeAndUsername(Credentials credentials);
}
