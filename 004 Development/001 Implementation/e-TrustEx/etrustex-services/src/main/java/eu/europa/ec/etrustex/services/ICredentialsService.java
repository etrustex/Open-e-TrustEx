/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.Credentials;


/**
 * @author batrian
 *
 */
public interface ICredentialsService {
	
	Boolean exists(Credentials credentials);

    Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort);

    List<Credentials> findByTypeAndUsername(Credentials credentials);
	
    Credentials get(Long id);
}
