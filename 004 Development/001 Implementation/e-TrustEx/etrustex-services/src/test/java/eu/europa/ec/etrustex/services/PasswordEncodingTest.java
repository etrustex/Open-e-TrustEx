package eu.europa.ec.etrustex.services;

import java.io.File;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;


public class PasswordEncodingTest {
	
	@Test
	public void testEncodePassword() throws Exception{
		String password = "etrustex";
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		System.out.println(System.getProperty("java.io.tmpdir"));
		
		byte[] hash = digest.digest(password.getBytes());

		
		System.out.println(Base64.encodeBase64String(hash));
		
		File test = new File("e:\\data","testfile"); 
		test.mkdir();
		
	}
	
	

}
