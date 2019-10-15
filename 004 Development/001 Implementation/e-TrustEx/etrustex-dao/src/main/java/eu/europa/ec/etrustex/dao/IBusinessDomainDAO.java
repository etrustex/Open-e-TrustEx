package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

/**
 * @author batrian
 *
 */
public interface IBusinessDomainDAO extends ITrustExDAO<BusinessDomain, Long> {

	/**
	 * find a BD by its unique name
	 * @param name the BD name
	 * @return the BD
	 */
	public BusinessDomain findByName(String name);
}
