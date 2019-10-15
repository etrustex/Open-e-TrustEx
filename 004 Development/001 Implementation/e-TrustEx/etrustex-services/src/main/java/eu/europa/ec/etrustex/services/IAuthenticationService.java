/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import eu.europa.ec.etrustex.dao.exception.AuthenticationFailedException;

/**
 * @author chiricr
 *
 */
public interface IAuthenticationService {
	
	/**
	 * authenticates user based on Base64 encoded username:password
	 * @param encodedUsernamePassword
	 */
	public String authenticate(String encodedUsernamePassword) throws AuthenticationFailedException, IOException, NoSuchAlgorithmException;

}
