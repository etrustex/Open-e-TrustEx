package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy;

public interface IPolicyDAO extends ITrustExDAO<Policy, Long> {
	
	List<Policy> getPoliciesByType(Class<? extends Policy> type);
	
	/**
	 * retrieve policies based on the criteria in the DTO
	 * @param policySearchDTO
	 * @return
	 */
	List<Policy> getPoliciesByCriteria(PolicySearchDTO policySearchDTO);

    /**
     * For admin console. Retrieve policies based on the criteria in the DTO
     * @param businessDomains
     * @param transactionName
     * @param isActive
	 * @return
     */
    List<RetentionPolicy> findRetentionPoliciesByCriteriaForView(RetentionPolicy policy, List<BusinessDomain> businessDomains);
}
