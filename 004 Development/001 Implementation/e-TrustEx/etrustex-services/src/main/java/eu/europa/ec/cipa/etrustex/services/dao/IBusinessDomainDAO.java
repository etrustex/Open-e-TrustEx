package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;

/**
 * @author batrian
 *
 */
public interface IBusinessDomainDAO extends ITrustExDAO<BusinessDomain, Long> {

	public BusinessDomain findByName(String name);
}
