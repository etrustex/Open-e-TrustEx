package eu.europa.ec.etrustex.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.services.util.EncryptionService;

public class EncryptionServiceTest extends AbstractEtrustExTest {
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Test public void testEncryptMessage(){
		try {
			byte[] iv = EncryptionService.generateIV();
			String msg = "Hellow";			
			String encryptedMsg = encryptionService.encryptMessageInAES(msg, iv);
			
			Assert.assertNotNull(encryptedMsg);
	        Assert.assertNotEquals(encryptedMsg, msg);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			Assert.fail("Something went wrong: "+e.getMessage());
		}	
	}
	
	@Test public void testDecrypMessage() {
		try {
			byte[] iv = EncryptionService.generateIV();
			String msg = "Hellow";			
			String encryptedMsg = encryptionService.encryptMessageInAES(msg, iv);			
			String decryptedMsg =encryptionService.decryptMessageInAES(encryptedMsg, iv);			
			
			Assert.assertNotNull(encryptedMsg);
			Assert.assertNotNull(decryptedMsg);
			Assert.assertEquals(msg, decryptedMsg);
		} catch (GeneralSecurityException | IOException e) {
			Assert.fail("Something went wrong: "+e.getMessage());
		}  		
	}
	
	@Test public void testDecryptionWithDifferentIV() {
		try {
			String msg = "Hellow";			
			String encryptedMsg = encryptionService.encryptMessageInAES(msg, EncryptionService.generateIV());			
			encryptionService.decryptMessageInAES(encryptedMsg, EncryptionService.generateIV());
			Assert.fail("Should not get here as the IV is different");
		} catch (GeneralSecurityException | IOException e) {
			Assert.assertTrue("Cannot be decrypted based on a wrong IV", e instanceof BadPaddingException);
		}								
	}
	
	@Test public void testRandomlyGeneratedIV(){
		try {
			byte[] iv1 = EncryptionService.generateIV();
			Assert.assertNotNull(iv1);
			Assert.assertThat(iv1, IsNot.not(IsEqual.equalTo(EncryptionService.generateIV())));		
		} catch (GeneralSecurityException e) {
			Assert.fail("Something went wrong: "+e.getMessage());
		}
	}
	
	@Test public void testDercryptRSA() throws Exception{
		try {
			encryptionService.dercryptRSA("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test public void testEncryptPassword(){
		String password1 = "Hello1";
		String password2 = "Hello1";
		
		try {
			Assert.assertEquals(encryptionService.encryptPassword(password1), encryptionService.encryptPassword(password2));
		} catch (NoSuchAlgorithmException e) {
			Assert.fail();
		}
	}
	
	@Test public void testEncryptPasswordByBCrypt(){
		boolean res = false;
		
		String password1 = "Hello1";
		String password2 = "Hello2";
		
		String encPassword1 = encryptionService.encryptPasswordByBCrypt(password1);
		String encPassword2 = encryptionService.encryptPasswordByBCrypt(password1);
		String encPassword3 = encryptionService.encryptPasswordByBCrypt(password2);
		
		res = encryptionService.compareBCryptPasswords(password1, encPassword1);
		Assert.assertTrue(res);
		res = encryptionService.compareBCryptPasswords(password1, encPassword2);
		Assert.assertTrue(res);
		res = encryptionService.compareBCryptPasswords(password1, encPassword3);
		Assert.assertFalse(res);
		res = encryptionService.compareBCryptPasswords(password2, encPassword3);
		Assert.assertTrue(res);
	}

}
