/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;


/**
 * @author batrian
 *
 */
@Repository
public class BusinessDomainDAO extends TrustExDAO<BusinessDomain, Long> implements IBusinessDomainDAO {
	
	@Override
	public BusinessDomain findByName(String name) {
		return entityManager.createQuery("from BusinessDomain bd where bd.name = :name", BusinessDomain.class)
			.setParameter("name", name)
			.getSingleResult();
	}
}
