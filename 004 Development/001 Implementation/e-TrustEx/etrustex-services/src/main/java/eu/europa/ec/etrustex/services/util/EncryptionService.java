/**
 * 
 */
package eu.europa.ec.etrustex.services.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.europa.ec.etrustex.dao.util.Cipher;

/**
 * @author batrian
 *
 */
public class EncryptionService {
	
	private static final String ALGORITHM_AES256 = "AES/CBC/PKCS5Padding";
	
	private static final String PREFIX_START_TAG = "{";
	private static final String PREFIX_END_TAG = "}";
	
	private static final String ALGORITHM_SHA_1 = "SHA-1";	
	   
	private static final Logger logger = LoggerFactory.getLogger(EncryptionService.class);
	
	private Boolean addAlgorithmPrefix;
	
	private String certificateAlias;	
	private String password;	
	private KeyStore keyStore;

	private KeyStore aesKeyStore;
	private String 	 aesAlias;
	private String 	 aesPassword;
	
	public String encryptPasswordByBCrypt(String password){
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
      String hashedPassword = passwordEncoder.encode(password);
      return hashedPassword;     
	}
	
	public boolean compareBCryptPasswords(String p1, String p2){
		return (new BCryptPasswordEncoder(12).matches(p1, p2));
	}
	
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
	
	public String dercryptRSA(String base64pwd) throws Exception{
		String decryptedValue = null;
		
		byte[] certB64 = Cipher.decodeFromBase64(base64pwd);

		Key key = keyStore.getKey(certificateAlias, password.toCharArray());
	
		java.security.cert.Certificate cer = keyStore.getCertificate(certificateAlias);
		PublicKey publicKey= cer.getPublicKey();
		KeyPair keyPair = new KeyPair(publicKey,(PrivateKey)key);
		PrivateKey privateKey = keyPair.getPrivate();
	
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
		
		decryptedValue = new String(cipher.doFinal(certB64), "UTF-8");

		return decryptedValue;
	}
	
	//Methods for AES encryption using jck store
	
	private SecretKeySpec getKey() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException{
		if (!aesKeyStore.containsAlias(aesAlias)) {
            throw new RuntimeException("Alias for key not found");
        }
		
		Key key = aesKeyStore.getKey(aesAlias, aesPassword.toCharArray());
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
		
		return secretKeySpec;
	}
	
	public String encryptMessageInAES(String messagePlainText, byte[] ivParam) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ALGORITHM_AES256);
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getKey(), new IvParameterSpec(ivParam));		
		byte[] encryptedTextBytes = cipher.doFinal(messagePlainText.getBytes("UTF-8"));

        return Cipher.encodeToBase64(encryptedTextBytes);			
	}
	
	public String decryptMessageInAES(String messageInBase64, byte[] ivParam) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException{
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(ALGORITHM_AES256);
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey(), new IvParameterSpec(ivParam));		
		
		byte[] encryptedTextBytes = Cipher.decodeFromBase64(messageInBase64);
		byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

        return new String(decryptedTextBytes);			
	}
	
	public static byte[] generateIV() throws GeneralSecurityException{
		SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] iv = new byte[javax.crypto.Cipher.getInstance(ALGORITHM_AES256).getBlockSize()];
		randomSecureRandom.nextBytes(iv);
		return iv;
	}
	
	//Setters
	
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCertificateAlias(String certificateAlias) {
		this.certificateAlias = certificateAlias;
	}

	public void setAesKeyStore(KeyStore aesKeyStore) {
		this.aesKeyStore = aesKeyStore;
	}

	public void setAesAlias(String aesAlias) {
		this.aesAlias = aesAlias;
	}

	public void setAesPassword(String aesPassword) {
		this.aesPassword = aesPassword;
	}
	
}
