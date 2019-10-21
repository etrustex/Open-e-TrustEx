package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.exception.AuthenticationFailedException;

public class AuthenticationServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IAuthenticationService authenticationService;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("authenticationService");
		System.out.println("--------------------------------------------------");
	}

	@Test
	public void testauthenticationService() throws AuthenticationFailedException, NoSuchAlgorithmException, IOException {
		System.out.println("-----------------authenticationService-------------------");
		String usernamePassword = "UGFydHlVc2VyMjE6UGFydHlVc2VyMjE=";
		String  str = authenticationService.authenticate(usernamePassword);
		assertEquals("PartyUser21", str);
	}

}
