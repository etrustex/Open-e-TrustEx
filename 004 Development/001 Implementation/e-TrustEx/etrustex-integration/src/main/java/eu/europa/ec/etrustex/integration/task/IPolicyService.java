package eu.europa.ec.etrustex.integration.task;

import java.util.List;

import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy;

public interface IPolicyService {

	List<Policy> getAllPolicies(); 
	
	List<Policy> getRetentionPolicies();
	
	List<Policy> getPoliciesByType(Class<? extends Policy> type);
		
	/**
	 * validates the number of wrappers in a bundle against the SLA policy
	 * @param policySearchDTO
	 * @param numberOfWrappers
	 * @return true if SLA policy is not violated
	 */
	boolean validateNumberOfWrappers(PolicySearchDTO policySearchDTO, int numberOfWrappers);
	
	void applyRetentionPolicies();
	
	List<Policy> getPoliciesByCriteria(PolicySearchDTO policySearchDTO);

	Policy create(Policy policy);

    Policy update(Policy policy);

    Policy findById(Long id);

    List<RetentionPolicy> findRetentionPoliciesByCriteriaForView(RetentionPolicy policy, List<BusinessDomain> businessDomains);

    void delete(Long id);
}
