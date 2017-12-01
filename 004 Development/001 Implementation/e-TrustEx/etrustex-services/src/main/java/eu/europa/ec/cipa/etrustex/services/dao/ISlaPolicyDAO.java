package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.types.SlaType;

import java.util.List;

public interface ISlaPolicyDAO extends ITrustExDAO<SlaPolicy, Long> {
	
	List<SlaPolicy> getPoliciesByType(List<SlaType> types);
	
	/**
	 * retrieve policies based on the criteria in the DTO
	 * @param slaPolicySearchDTO
	 * @return
	 */
	List<SlaPolicy> getPoliciesByCriteria(SlaPolicySearchDTO slaPolicySearchDTO);

    /**
     * For admin console. Retrieve policies based on the criteria in the DTO
     * @param businessDomains
     * @param transactionName
     * @param isActive
	 * @return
     */
    List<SlaPolicy> findPoliciesByCriteriaForView(SlaPolicy slaPolicy, List<BusinessDomain> businessDomains);
}
