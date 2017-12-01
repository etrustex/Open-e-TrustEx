/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.Certificate;

/**
 * @author batrian
 *
 */
@Repository
public class CertificateDAO extends TrustExDAO<Certificate, Long> implements ICertificateDAO {

	@Override
	public void delete(Long id) {
		Certificate certificate = read(id);
		super.delete(certificate);
	}
	

}
