package eu.europa.ec.etrustex.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyRoleDao;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.services.IPartyRoleService;

@Service("partyRoleService")
public class PartyRoleService implements IPartyRoleService {
	@Autowired
	private IPartyRoleDao partyRoleDAO;
    @Autowired
    private IPartyAgreementDAO partyAgreementDAO;

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
        List<PartyListItemDTO> list = new ArrayList<>();

        for(Party party : partyRoleDAO.getPartiesForRoleAndDomain(roleId, businessDomainId)) {
            list.add(new PartyListItemDTO(party.getId(),
                    party.getName(),
                    party.getIdentifiers(),
                    partyAgreementDAO.getDelegateThirdPartiesNamesFor(party.getId())));
        }

		return list;
	}
}
