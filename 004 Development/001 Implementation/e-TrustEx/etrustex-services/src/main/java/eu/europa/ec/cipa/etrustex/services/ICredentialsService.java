/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Credentials;

import java.util.List;


/**
 * @author batrian
 *
 */
public interface ICredentialsService {
	
	Boolean exists(Credentials credentials);

    Boolean isUniqueProxyCredentials(String username, String proxyHost, Integer proxyPort);

    List<Credentials> findByTypeAndUsername(Credentials credentials);

    List<Credentials> findByTypeAndUsernameLike(Credentials credentials);
	
    Credentials get(Long id);
}
