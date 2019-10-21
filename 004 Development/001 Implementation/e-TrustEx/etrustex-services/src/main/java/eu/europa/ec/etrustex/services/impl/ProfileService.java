/**
 * 
 */
package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.*;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.EventNotification;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IProfileService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author batrian
 *
 */
@Service("profileService")
public class ProfileService implements IProfileService {

	@Autowired
	private IProfileDAO profileDao;
	
	@Autowired
	private IBusinessDomainDAO businessDomainDAO;
	
	@Autowired 
	private ICIALevelDAO ciaLevelDAO;

    @Autowired
    private IEventNotificationDAO eventNotificationDAO;

    @Autowired
    private ITransactionDAO transactionDAO;

    @Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Profile getProfile(Long id) {
		Profile p = profileDao.getProfile(id);

		if(p != null) {
			for(BusinessDomain bd : p.getBusinessDomains()) {
				for(Profile p2 : bd.getProfiles()) {
					Hibernate.initialize(p2);
				}
				Hibernate.initialize(bd);
			}

			for(Transaction t : p.getTransactions()) {
				Hibernate.initialize(t);
			}
		}

		return p;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Profile create(Profile profile) {
		profile.setCiaLevel(getCiaLevel(profile));
		return profileDao.create(profile);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Profile update(Profile profile) {
		profile.setCiaLevel(getCiaLevel(profile));
		return profileDao.update(profile);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long id) {
		Profile p = profileDao.getProfile(id);

		/*
		 * UC40_BR15 Any association of the profile to a business domain or a transaction shall be removed before deletion,
		 * as well as any event notification configuration record linked to this profile.
		 */
		for (BusinessDomain bd : p.getBusinessDomains()) {
			bd.getProfiles().remove(p);
			businessDomainDAO.update(bd);
		}

		for (Transaction t : p.getTransactions()) {
		    t.getProfiles().remove(p);
		    transactionDAO.update(t);
        }

		for (EventNotification en : eventNotificationDAO.findByProfile(p) ) {
		    eventNotificationDAO.delete(en);
        }
		
		profileDao.delete(p);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Profile> getAllProfiles() {
		return profileDao.getAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Profile> findProfilesByCriteria(String name, String transactionName, BusinessDomain businessDomain) {
		List<Profile> profiles = profileDao.findProfilesByCriteria(name, transactionName, businessDomain);
		for(Profile p : profiles) {
			Hibernate.initialize(p.getTransactions());
		}
		
		return profiles;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isInUse(Profile profile) {
		return profileDao.isInUse(profile);
	}

    @Override
    @Transactional(readOnly = true)
    public Profile findByName(String name) {
        return profileDao.findByName(name);
    }

    private CIALevel getCiaLevel(Profile profile) {
		CIALevel ciaLevel;
		List<CIALevel> ciaLevels = ciaLevelDAO.retrieveCIALevel(profile.getCiaLevel().getConfidentialityLevel(), 
				profile.getCiaLevel().getIntegrityLevel(), profile.getCiaLevel().getAvailabilityLevel());
		if(ciaLevels.isEmpty()) {
			ciaLevel = new CIALevel();
			ciaLevel.setConfidentialityLevel(profile.getCiaLevel().getConfidentialityLevel());
			ciaLevel.setIntegrityLevel(profile.getCiaLevel().getIntegrityLevel());
			ciaLevel.setAvailabilityLevel(profile.getCiaLevel().getAvailabilityLevel());
			
			EntityAccessInfo eai = new EntityAccessInfo();
			eai.setCreationId(profile.getAccessInfo().getCreationId());
			ciaLevel.setAccessInfo(eai);
			
			ciaLevel = ciaLevelDAO.create(ciaLevel);
		} else {
			ciaLevel = ciaLevels.get(0);
		}
		
		return ciaLevel;
	}
}