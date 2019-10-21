package eu.europa.ec.etrustex.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IPolicyDAO;
import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.Transaction_;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.Policy_;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy_;

@Repository
public class PolicyDAO extends TrustExDAO<Policy, Long> implements IPolicyDAO {
		
	public List<Policy> getPoliciesByType(Class<? extends Policy> policy){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Policy> cq = cb.createQuery(Policy.class);
        Root<? extends Policy> root = cq.from(policy);  
        cq.select(root).distinct(true);

        return entityManager.createQuery(cq).getResultList();
	}

	public List<Policy> getPoliciesByCriteria(PolicySearchDTO policySearchDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Policy> criteriaQuery = criteriaBuilder.createQuery(Policy.class);
		Root<? extends Policy> policy = criteriaQuery.from(policySearchDTO.getPolicyType());
		criteriaQuery.select(policy);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (policySearchDTO.getSender() != null) {
			predicate = criteriaBuilder.equal(policy.get(Policy_.businessDomain),
					policySearchDTO.getSender().getBusinessDomain());
			predicates.add(predicate);
		}

		if (policySearchDTO.getTransaction() != null) {
			predicate = criteriaBuilder.equal(policy.get(Policy_.transaction),
					policySearchDTO.getTransaction());
			predicates.add(predicate);
		}

		predicate = criteriaBuilder.equal(policy.get(Policy_.activeFlag),
				Boolean.TRUE);
		predicates.add(predicate);

		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = criteriaBuilder.and(predicates.toArray(preds));
		criteriaQuery.where(clause);

		TypedQuery<Policy> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

    @Override
    public List<RetentionPolicy> findRetentionPoliciesByCriteriaForView(RetentionPolicy rPolicy, List<BusinessDomain> businessDomains) {
        if(rPolicy == null) {
            return null;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RetentionPolicy> cq = cb.createQuery(RetentionPolicy.class);
        Root<RetentionPolicy> policy = cq.from(RetentionPolicy.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Join<RetentionPolicy, Transaction> transJoin = policy.join(RetentionPolicy_.transaction, JoinType.INNER);
        if (rPolicy.getTransaction() != null && StringUtils.isNotBlank(rPolicy.getTransaction().getName())) {        	
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(transJoin.get(Transaction_.name))),
                    "%" + rPolicy.getTransaction().getName().trim().toUpperCase() + "%"));
        }

        if (CollectionUtils.isNotEmpty(businessDomains)) {
            predicates.add(policy.get(RetentionPolicy_.businessDomain).in(businessDomains));
        }

        if (rPolicy.getActiveFlag() != null) {
            predicates.add(cb.equal(policy.get(RetentionPolicy_.activeFlag), rPolicy.getActiveFlag()));
        }

        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.orderBy(cb.asc(cb.lower(transJoin.get(Transaction_.name))));

        return entityManager.createQuery(cq).getResultList();
    }
}
