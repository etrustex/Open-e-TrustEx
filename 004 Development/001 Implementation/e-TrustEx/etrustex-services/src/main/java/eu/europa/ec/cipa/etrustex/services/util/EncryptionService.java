/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Base64;

/**
 * @author batrian
 *
 */
public class EncryptionService {
	
	private static final String PREFIX_START_TAG = "{";
	private static final String PREFIX_END_TAG = "}";
	
	private static final String ALGORITHM_SHA_1 = "SHA-1";
	
	private Boolean addAlgorithmPrefix;

	public String encryptPassword(String password) throws NoSuchAlgorithmException{
		MessageDigest digest = MessageDigest.getInstance(ALGORITHM_SHA_1);
		byte[] hash = digest.digest(password.getBytes());
		String encodedPassword = new String(Base64.encode(hash));
		
		if (addAlgorithmPrefix){
			return PREFIX_START_TAG + ALGORITHM_SHA_1 + PREFIX_END_TAG + encodedPassword;
		} else {
			return encodedPassword;
		}
	}

	public void setAddAlgorithmPrefix(boolean addAlgorithmPrefix) {
		this.addAlgorithmPrefix = addAlgorithmPrefix;
	}
	
}
