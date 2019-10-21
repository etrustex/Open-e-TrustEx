package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Certificate;

public class CertificateServiceTest extends AbstractEtrustExTest {
	
	@Autowired private ICertificateService certificateService;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("CertificateService");
		System.out.println("--------------------------------------------------");
	}

	@Test
	public void testGetCertificate() {
		System.out.println("------getCertificate------");
		Certificate certificate = certificateService.getCertificate(25L);
		assertNotNull(certificate);
		System.out.println(certificate);
	}
	
	@Test
	public void CRUD(){
		System.out.println("------Create-Update-Delete------");
		Certificate certificate = new Certificate();
		certificate.setSignatureValue("test");
		certificate.setType("X509");
		certificate.setUsage("KEY_ENCIPHERMENT");
		certificate.setEncodedData("MIIBszCCARygAwIBAgIECTDpiTA");
		certificate.setHolder("CN test CA Certificate");
		certificate.setIsActive(Boolean.TRUE);
		certificate.setIsRevoked(Boolean.FALSE);
		certificate.setIssuer("Issuer test");
		certificate.setSerialNumber(UUID.randomUUID().toString());
		
		Certificate certSaved = certificateService.createCertificate(certificate);
		assertNotNull("Cannot save certificate",certSaved);
		System.out.println("Certificate has been saved   :"+certSaved);
		
		certificate.setSerialNumber("0123-456-789");
		certificate = certificateService.updateCertificate(certSaved);
		System.out.println("Certificate has been updated :"+certificate);
		
		certificateService.deleteCertificate(certificate.getId());
		System.out.println("Certificate has been deleted :");
		
	}

}
