/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Certificate;

/**
 * @author batrian
 *
 */
public interface ICertificateDAO extends ITrustExDAO<Certificate, Long> {
	
	public void delete(Long id);
	
}

