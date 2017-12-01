package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyRoleDao;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class PartyRoleService implements IPartyRoleService {
	@Autowired
	private IPartyRoleDao partyRoleDAO;
	
	private static final Logger logger = LoggerFactory
			.getLogger(PartyRoleService.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<PartyRole> getPartyRoles(Long partyId, Long roleId) {
		logger.info("getPartyRoles({}, {})", partyId, roleId);
		return partyRoleDAO.getPartyRoles(partyId, roleId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PartyRole createPartyRole(PartyRole pr) {
		logger.info("createPartyRole");
		return partyRoleDAO.create(pr);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<PartyListItemDTO> getPartiesByRoleAndDomain(Long roleId, Long businessDomainId) {
		logger.info("getPartiesByRoleAndDomain({}, {})", roleId, businessDomainId);

		return partyRoleDAO.getPartiesForRoleAndDomain(roleId, businessDomainId);
	}
}
