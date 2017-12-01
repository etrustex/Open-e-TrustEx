package eu.europa.ec.cipa.etrustex.integration.task;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.types.SlaType;

import java.util.List;

public interface ISlaPolicyService {

	List<SlaPolicy> getAllPolicies(); 
	
	List<SlaPolicy> getRetentionPolicies();
	
	List<SlaPolicy> getSlaPolicies();
	
	List<SlaPolicy> getPoliciesByType(SlaType type);
	
	/**
	 * validates the volume of data sent by a sender against the SLA policy 
	 * @param slaPolicySearchDTO
	 * @return true if SLA policy is not violated
	 */
	boolean validateWrapperVolume(SlaPolicySearchDTO slaPolicySearchDTO);
	
	/**
	 * validates the number of wrappers in a bundle against the SLA policy
	 * @param slaPolicySearchDTO
	 * @param numberOfWrappers
	 * @return true if SLA policy is not violated
	 */
	boolean validateNumberOfWrappers(SlaPolicySearchDTO slaPolicySearchDTO, int numberOfWrappers);
	
	/**
	 * validates the size of a wrapper against the SLA policy
	 * @param slaPolicySearchDTO
	 * @param wrapperSize wrapper size in MB
	 * @return true if SLA policy is not violated
	 */
	boolean validateWrapperSize(SlaPolicySearchDTO slaPolicySearchDTO, int wrapperSize);
	
	void applyRetentionPolicies();
	
	List<SlaPolicy> getPoliciesByCriteria(SlaPolicySearchDTO slaPolicySearchDTO);

    SlaPolicy create(SlaPolicy slaPolicy);

    SlaPolicy update(SlaPolicy slaPolicy);

    SlaPolicy findById(Long id);

    List<SlaPolicy> findPoliciesByCriteriaForView(SlaPolicy slaPolicy, List<BusinessDomain> businessDomains);

    void delete(Long id);
}
