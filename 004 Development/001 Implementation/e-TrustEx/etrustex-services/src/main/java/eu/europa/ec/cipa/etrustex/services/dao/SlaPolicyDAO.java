package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Transaction_;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy_;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.types.SlaType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SlaPolicyDAO extends TrustExDAO<SlaPolicy, Long> implements ISlaPolicyDAO {

	public List<SlaPolicy> getPoliciesByType(List<SlaType> types){
		return entityManager.createQuery(
				"FROM SlaPolicy sla WHERE sla.type IN (:slaTypes) ",SlaPolicy.class).
				setParameter("slaTypes", types).getResultList();
	}

	public List<SlaPolicy> getPoliciesByCriteria(SlaPolicySearchDTO slaPolicySearchDTO) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SlaPolicy> criteriaQuery = criteriaBuilder.createQuery(SlaPolicy.class);
		Root<SlaPolicy> policy = criteriaQuery.from(SlaPolicy.class);
		criteriaQuery.select(policy);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (slaPolicySearchDTO.getSender() != null) {
			predicate = criteriaBuilder.equal(policy.get(SlaPolicy_.businessDomain),
					slaPolicySearchDTO.getSender().getBusinessDomain());
			predicates.add(predicate);
		}


		if (slaPolicySearchDTO.getSlaType() != null) {
			predicate = criteriaBuilder.equal(policy.get(SlaPolicy_.type),
					slaPolicySearchDTO.getSlaType());
			predicates.add(predicate);
		}

		if (slaPolicySearchDTO.getTransaction() != null) {
			predicate = criteriaBuilder.equal(policy.get(SlaPolicy_.transaction),
					slaPolicySearchDTO.getTransaction());
			predicates.add(predicate);
		}

		predicate = criteriaBuilder.equal(policy.get(SlaPolicy_.activeFlag),
				Boolean.TRUE);
		predicates.add(predicate);

		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = criteriaBuilder.and(predicates.toArray(preds));
		criteriaQuery.where(clause);

		TypedQuery<SlaPolicy> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

    @Override
    public List<SlaPolicy> findPoliciesByCriteriaForView(SlaPolicy slaPolicy, List<BusinessDomain> businessDomains) {
        if(slaPolicy == null) {
            return null;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SlaPolicy> cq = cb.createQuery(SlaPolicy.class);
        Root<SlaPolicy> policy = cq.from(SlaPolicy.class);
        List<Predicate> predicates = new ArrayList<Predicate>();


        if (slaPolicy.getTransaction() != null && StringUtils.isNotBlank(slaPolicy.getTransaction().getName())) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(policy.get(SlaPolicy_.transaction).get(Transaction_.name))),
                    "%" + slaPolicy.getTransaction().getName().trim().toUpperCase() + "%"));
        }

        if (CollectionUtils.isNotEmpty(businessDomains)) {
            predicates.add(policy.get(SlaPolicy_.businessDomain).in(businessDomains));
        }

        if (slaPolicy.getActiveFlag() != null) {
            predicates.add(cb.equal(policy.get(SlaPolicy_.activeFlag), slaPolicy.getActiveFlag()));
        }

        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.orderBy(cb.asc(cb.lower(policy.get(SlaPolicy_.transaction).get(Transaction_.name))));

        return entityManager.createQuery(cq).getResultList();
    }
}
