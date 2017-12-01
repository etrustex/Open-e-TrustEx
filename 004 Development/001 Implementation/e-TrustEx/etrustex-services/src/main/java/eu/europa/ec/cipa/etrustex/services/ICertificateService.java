/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Certificate;

/**
 * @author batrian
 *
 */
public interface ICertificateService {
	/**
	 * Retrieves the certificate corresponding to a given id.
	 * @param id
	 * 		the id of the certificate to retrieve
	 * @return
	 * 		the certificate corresponding to the specified id  
	 */
	public Certificate getCertificate(Long id);
	
	/**
	 * Creates a new certificate.
	 * @param certificate
	 * 		the certificate to create
	 * @return
	 * 		the newly created certificate
	 */
	public Certificate createCertificate(Certificate certificate);
	
	/**
	 * Update a certificate.
	 * @param certificate
	 * 		the certificate to update
	 * @return
	 * 		the updated certificate
	 */
	public Certificate updateCertificate(Certificate certificate);
	
	/**
	 * Deletes a certificate.
	 * @param id
	 * 		the id of the certificate to delete
	 */
	public void deleteCertificate(Long id);
}
