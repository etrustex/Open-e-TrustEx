/**
 * 
 */
package eu.europa.ec.etrustex.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.IRoleDAO;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.services.IRoleService;

/**
 * @author batrian
 * 
 */
@Service
public class RoleService implements IRoleService {

	@Autowired
	private IRoleDAO roleDAO;

    @Autowired
    private IBusinessDomainDAO businessDomainDAO;
	
	private static final Logger logger = LoggerFactory
			.getLogger(RoleService.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Role createRole(Role role) {
		logger.info("createRole({})", role);
		return roleDAO.create(role);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Role getRole(Long id) {
		logger.info("getRole({})", id);
		return roleDAO.read(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRole(Long id) {
		logger.info("deleteRole({})", id);
		roleDAO.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public List<Role> getAll() {
		return roleDAO.getAll();
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    public Set<Role> getRolesForBusinessDomain(Long businessDomainId) {
        Set<Role> roles = new HashSet<>();
        BusinessDomain businessDomain = businessDomainDAO.read(businessDomainId);

        for (Profile profile : businessDomain.getProfiles()) {
            for (Transaction transaction : profile.getTransactions()) {
                roles.add(transaction.getSenderRole());
                roles.add(transaction.getReceiverRole());
            }
        }

        return roles;
    }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Boolean isInUse(Role role) {
		return roleDAO.isInUse(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Role updateRole(Role role) {
		return roleDAO.update(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isUniqueRole(Role role) {
		return roleDAO.isUnique(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isUniqueRoleCode(Role role) {
		return roleDAO.isUniqueCode(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isUniqueRoleName(Role role) {
		return roleDAO.isUniqueName(role);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Role> getRolesForProfile(Long profileId, Boolean includeTechnical){
		return roleDAO.getRolesForProfile(profileId, includeTechnical);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Role> getAllNonTechnical() {
		return roleDAO.getAllNonTechnical();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Role findByCode(String roleCode) {
		return roleDAO.findByCode(roleCode);
	}

}
