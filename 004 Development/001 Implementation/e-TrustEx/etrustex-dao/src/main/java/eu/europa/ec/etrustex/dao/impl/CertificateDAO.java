/**
 * 
 */
package eu.europa.ec.etrustex.dao.impl;

import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.ICertificateDAO;
import eu.europa.ec.etrustex.domain.Certificate;

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
