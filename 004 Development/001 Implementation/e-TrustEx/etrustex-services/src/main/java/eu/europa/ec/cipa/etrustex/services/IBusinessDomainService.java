/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;

/**
 * @author batrian
 *
 */
public interface IBusinessDomainService {
	public BusinessDomain getBusinessDomain(Long id);
	public List<BusinessDomain> getAllBusinessDomains();
	
	/**
	 * get the business domain from either sender, receiver or issuer
	 * @param authenticatedUser
	 * @param receiverIdWithScheme
	 * @param senderIdWithScheme
	 * @return
	 */
	public BusinessDomain retrieveBusinessDomain(String authenticatedUser);
	
	public BusinessDomain findByName(String name);
}
