/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint_;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode_;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem_;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author batrian
 *
 */
@Repository
public class ProfileDAO extends TrustExDAO<Profile, Long> implements IProfileDAO {

	@Override
	public Profile getProfile(Long id) {
		return getUniqueByAttribute(Profile_.id, id);
	}

	
	@Override // @author batrian
	public List<BusinessDomain> getBusinessDomains(Profile profile) {
		
		List<BusinessDomain> bdList= entityManager
			.createQuery("select bd from BusinessDomain bd join bd.profiles as profiles where profiles.id = :profileId", BusinessDomain.class)
			.setParameter("profileId", profile.getId())
			.getResultList();
		
		return bdList;
	}


	@Override
	public List<Profile> findProfilesByCriteria(String name, String transactionName, BusinessDomain businessDomain) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);
		Root<Profile> p = cq.from(Profile.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(StringUtils.isNotEmpty(name)) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(p.get(Profile_.name))), "%" + name.trim().toUpperCase() + "%"));
		}
		
		if(StringUtils.isNotEmpty(transactionName)) {
//			Root<Transaction> tx = cq.from(Transaction.class);
			SetJoin<Profile, Transaction> txJoin = p.join(Profile_.transactions);
//			predicates.add(cb.and(txJoin.in(cb.like(cb.trim(Trimspec.BOTH, cb.upper(txJoin.get(Transaction_.name))), "%" + transactionName.trim().toUpperCase() + "%"))));
			predicates.add(cb.and(cb.like(cb.trim(Trimspec.BOTH, cb.upper(txJoin.get(Transaction_.name))), "%" + transactionName.trim().toUpperCase() + "%")));
//			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(txJoin.get(Transaction_.name))), "%" + transactionName.trim().toUpperCase() + "%"));
		}
		
		if(businessDomain != null && businessDomain.getId() != null) {
			predicates.add(cb.isMember(businessDomain, p.get(Profile_.businessDomains)));
		}
				
		cq.select(p).distinct(true);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		cq.orderBy(cb.asc(cb.lower(p.get(Profile_.name))));
		
		return entityManager.createQuery(cq).getResultList();
	}


	/*
	 * UC40_BR14 Profile cannot be deleted if it is used in an interchange agreement,
	 * by an end point, message response code, metadata item or retention policy configuration.
	 *  ETR_TB_RETENTION_POLICY no longer used
	 */
	@Override
	public Boolean isInUse(Profile profile) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<InterchangeAgreement> interchangeAgreementRoot = cq.from(InterchangeAgreement.class);
        cq.select(cb.count(interchangeAgreementRoot));
        cq.where(cb.equal(interchangeAgreementRoot.get(InterchangeAgreement_.profile), profile));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<Endpoint> endpointRoot = cq.from(Endpoint.class);
        cq.select(cb.count(endpointRoot));
        cq.where(cb.equal(endpointRoot.get(Endpoint_.profile), profile));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<MessageResponseCode> messageResponseCodeRoot = cq.from(MessageResponseCode.class);
        cq.select(cb.count(messageResponseCodeRoot));
        cq.where(cb.equal(messageResponseCodeRoot.get(MessageResponseCode_.profile), profile));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<MetaDataItem> metadataRoot = cq.from(MetaDataItem.class);
        cq.select(cb.count(metadataRoot));
        cq.where(cb.equal(metadataRoot.get(MetaDataItem_.profile), profile));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

		return false;
	}

    @Override
    public Profile findByName(String name) {
	    List<Profile> profiles = entityManager.createQuery("from Profile p where p.name = :name ", Profile.class)
                .setParameter("name", name).getResultList();

	    return CollectionUtils.isNotEmpty(profiles) ? profiles.get(0) : null;
    }

    /*
     * The owner side is the one without the mappedBy attribute, i.e. BusinessDomain.
     * JPA/Hibernate only cares about the owner side
     */
	@Override
	public Profile create(Profile profile) {
		if(profile.getBusinessDomains() != null) { // Shouldn't be null
			for(BusinessDomain bd : profile.getBusinessDomains()) {
				entityManager.find(BusinessDomain.class, bd.getId()).getProfiles().add(profile);
			}
		}
				
		return super.create(profile);
	}
	
	/*
	 * Same as for create (avobe) and check if business domains have been removed from profile.
	 */
	@Override
	public Profile update(Profile profile) {
		Set<BusinessDomain> previousBusinessDomains = new HashSet<BusinessDomain>(entityManager.find(Profile.class, profile.getId()).getBusinessDomains());
		Set<BusinessDomain> newBusinessDomains = profile.getBusinessDomains();

		for(BusinessDomain bd : newBusinessDomains) {
			if(!previousBusinessDomains.contains(bd)) {
				BusinessDomain bd2 = entityManager.find(BusinessDomain.class, bd.getId());
				bd2.getProfiles().add(profile);
			}
		}
		
		for(BusinessDomain bd : previousBusinessDomains) {
			if(!newBusinessDomains.contains(bd)) {
				BusinessDomain bd2 = entityManager.find(BusinessDomain.class, bd.getId());
				bd2.getProfiles().remove(profile);
			}
		}
		
		return super.update(profile);
	}
}
