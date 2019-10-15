/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Certificate;

/**
 * @author batrian
 *
 */
public interface ICertificateDAO extends ITrustExDAO<Certificate, Long> {
	
	/**
	 * delete a Certificate entity
	 * @param id the Certificate id
	 */
	public void delete(Long id);
	
}

