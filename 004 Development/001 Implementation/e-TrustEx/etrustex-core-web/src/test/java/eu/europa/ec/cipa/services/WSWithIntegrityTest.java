package eu.europa.ec.cipa.services;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.ws.client.core.WebServiceTemplate;


@ContextConfiguration(locations={"/etrustex-ws-test-context.xml"})
public class WSWithIntegrityTest  extends AbstractJUnit4SpringContextTests {	
	
	@Autowired
	private WebServiceTemplate webServiceTemplate;
	
	public WebServiceTemplate getWebServiceTemplate() {
		return webServiceTemplate;
	} 

	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate; 
	}
      
	@Test
	public void testWSCallWithIntegrity() throws Exception {
		try {  
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("wsSignatureTestRequest.xml");
			String inputStreamString = new Scanner(is,"UTF-8").useDelimiter("\\A").next();
			ETrustExWSRequestCallback reqCallback = new ETrustExWSRequestCallback("0088:000886688005",null,inputStreamString,"ec:services:wsdl:DocumentWrapper-2");
			ETrustExWSResponseCallback respCallback = new ETrustExWSResponseCallback();
			webServiceTemplate.setFaultMessageResolver(new FaultResolver()); 
			webServiceTemplate.sendAndReceive(reqCallback, respCallback);
//			   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
