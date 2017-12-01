/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Certificate;
import eu.europa.ec.cipa.etrustex.services.dao.ICertificateDAO;

/**
 * @author batrian
 *
 */
@Service
public class CertificateService implements ICertificateService {

	private static final Logger logger = LoggerFactory
			.getLogger(CertificateService.class);
	
	@Autowired
	private ICertificateDAO certificateDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Certificate getCertificate(Long id) {
		
		logger.info("getCertificate(id={})", id);
		return  certificateDAO.read(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Certificate createCertificate(Certificate certificate) {
		
		logger.info("createCertificate({})", certificate);
		
		//create the certificate
		Certificate createdCertificate = certificateDAO.create(certificate);
		
		//return the complete object
		return certificateDAO.read(createdCertificate.getId());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Certificate updateCertificate(Certificate certificate) {
		
		logger.info("updateCertificate({})", certificate);
		
		//update the certificate
		Certificate createdCertificate = certificateDAO.update(certificate);
		
		//return the complete object
		return certificateDAO.read(createdCertificate.getId());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCertificate(Long id) {
		logger.info("deleteCertificate({})", id);
		certificateDAO.delete(id);
	}
}
