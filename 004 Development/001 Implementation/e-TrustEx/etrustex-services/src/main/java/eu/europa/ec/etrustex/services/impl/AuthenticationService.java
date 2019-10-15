/**
 *
 */
package eu.europa.ec.etrustex.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.exception.AuthenticationFailedException;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.services.IAuthenticationService;
import eu.europa.ec.etrustex.services.ICredentialsService;
import eu.europa.ec.etrustex.services.util.EncryptionService;

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

	@Transactional
	public String authenticate(String encodedUsernamePassword) throws AuthenticationFailedException, IOException, NoSuchAlgorithmException {
		if (StringUtils.isEmpty(encodedUsernamePassword)) {
			throw new AuthenticationFailedException("Authentication failed");
		}
		//encodedUsernamePassword has the form "<base64-encoded username:password>"
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Base64.decode(encodedUsernamePassword, baos);
		String[] usernamePassword = new String[2];
		int index = baos.toString("UTF-8").indexOf(":");
		if (index == -1) {
			//incorrect format
			throw new AuthenticationFailedException("Authentication failed");
		}
		usernamePassword[0] = baos.toString().substring(0,index);
		usernamePassword[1] = baos.toString().substring(index+1);

		if (usernamePassword[1].isEmpty()) {
			throw new AuthenticationFailedException("Authentication failed");
		}
		Credentials partyCredentials = new PartyCredentials();
		partyCredentials.setUser(usernamePassword[0]);

		List<Credentials> credentialList = credentialsService.findByTypeAndUsername(partyCredentials);
		if(CollectionUtils.isEmpty(credentialList) || credentialList.get(0) == null) {
			//user does not exist
			throw new AuthenticationFailedException("Authentication failed");
		}
		Credentials credentials = credentialList.get(0);

		String encodedPassword = null;
		//if salt == false => old user, password encrypted without salt. otherwise password encrypted with salt
		if(credentials.getSalt() != null && credentials.getSalt()){
			if(!encryptionService.compareBCryptPasswords(usernamePassword[1], credentials.getPassword()))
				throw new AuthenticationFailedException("Authentication failed");
		}else{
			if(credentials.getPasswordEncrypted()){
				encodedPassword = encryptionService.encryptPassword(usernamePassword[1]);
			}else{
				encodedPassword = usernamePassword[1];
			}

			if (!credentials.getPassword().equals(encodedPassword)) {
				throw new AuthenticationFailedException("Authentication failed");
			}
		}


		return usernamePassword[0];
	}

}
