/**
 * 
 */
package eu.europa.ec.etrustex.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.IBusinessDomainService;

/**
 * @author batrian
 * 
 */
@Service("businessDomainService")
public class BusinessDomainService implements IBusinessDomainService {

	@Autowired
	private IBusinessDomainDAO businessDomainDAO;
	
	@Autowired
	private IAuthorisationService authorisationService;

	private static final Logger logger = LoggerFactory
			.getLogger(BusinessDomainService.class);

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public BusinessDomain getBusinessDomain(Long id) {
		logger.info("BusinessDomain - getBusinessDomain({})", id);
		
		BusinessDomain bd  = businessDomainDAO.read(id);
		
		//@author batrian: initialization of profiles and transactions (otherwise LaztInitializationException)
		if (bd != null &&CollectionUtils.isNotEmpty(bd.getProfiles())){

			Hibernate.initialize(bd.getProfiles());
		
			for (Profile profile : bd.getProfiles()) {
				Hibernate.initialize(profile.getTransactions());
			}
		}
		
		return bd;
	}
	
	@Override
	@Transactional
	public BusinessDomain retrieveBusinessDomain(String authenticatedUser) {
		Party party = null;
		try {
			party = authorisationService.getMessageIssuer(authenticatedUser);
		} catch (UndefinedIdentifierException e) {
		}		 			
			
		if (party != null) {
			return party.getBusinessDomain();
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<BusinessDomain> getAllBusinessDomains() {
		logger.info("BusinessDomain - getAllBusinessDomains()");
		return businessDomainDAO.getAll();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public BusinessDomain findByName(String name) {
		return businessDomainDAO.findByName(name);
	}

}
