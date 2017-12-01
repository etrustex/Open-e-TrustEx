/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.PartyCredentials;
import eu.europa.ec.cipa.etrustex.services.exception.AuthenticationFailedException;
import eu.europa.ec.cipa.etrustex.services.util.EncryptionService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

/**
 * @author chiricr
 *
 */
@Service
public class AuthenticationService implements IAuthenticationService {
	
	@Autowired
	private ICredentialsService credentialsService;
	
	@Autowired 
	private EncryptionService encryptionService;
	
	public String authenticate(String encodedUsernamePassword) throws AuthenticationFailedException, IOException, NoSuchAlgorithmException {
		if (StringUtils.isEmpty(encodedUsernamePassword)) {
			throw new AuthenticationFailedException("Authentication failed");
		}
		//authentication header has the form "Basic <base64-encoded username:password>"
//		encodedUsernamePassword = StringUtils.split(encodedUsernamePassword, " ")[1];
		OutputStream baos = new ByteArrayOutputStream();
		Base64.decode(encodedUsernamePassword, baos);		
		String[] usernamePassword = StringUtils.split(baos.toString(), ":");
		if (ArrayUtils.isEmpty(usernamePassword) || usernamePassword.length != 2) {
			throw new AuthenticationFailedException("Authentication failed");
		}
		Credentials partyCredentials = new PartyCredentials();
        partyCredentials.setUser(usernamePassword[0]);
		Credentials credentials = credentialsService.findByTypeAndUsername(partyCredentials).get(0);
		if (credentials == null) {
			//user does not exist
			throw new AuthenticationFailedException("Authentication failed");
		}
		String encodedPassword = encryptionService.encryptPassword(usernamePassword[1]);
		if (!credentials.getPassword().equals(encodedPassword)) {
			throw new AuthenticationFailedException("Authentication failed");
		}
	
		return usernamePassword[0];		
	}

}
