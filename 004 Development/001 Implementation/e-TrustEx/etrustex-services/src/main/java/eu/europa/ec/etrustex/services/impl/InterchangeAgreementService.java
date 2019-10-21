package eu.europa.ec.etrustex.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.europa.ec.etrustex.dao.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;

@Service
public class InterchangeAgreementService implements IInterchangeAgreementService {

	@Autowired
	private IInterchangeAgreementDAO interchangeAgreementDAO;

	@Autowired
	private IPartyRoleDao partyRoleDAO;

	@Autowired
	private IPartyAgreementDAO partyAgreementDAO;

	@Autowired
	private ICIALevelDAO ciaLevelDAO;

	@Autowired
	private IPartyDAO partyDAO;

	private static final Logger logger = LoggerFactory.getLogger(InterchangeAgreementService.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> getInterchangeAgreements(Party requester, Set<Party> senders, Set<Party> receivers, List<String> documentTypeIds) {
		if (senders == null) {
			senders = new HashSet<>();
		}
		if (receivers == null) {
			receivers = new HashSet<>();
		}
		if(senders.isEmpty() && receivers.isEmpty()){
			Set<InterchangeAgreement> icas = new HashSet<InterchangeAgreement>();
			senders.add(requester);
			icas.addAll(interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, senders, receivers));
			icas.addAll(interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, receivers, senders));
			return new ArrayList<InterchangeAgreement>(icas);
		}
		if ((senders.isEmpty() && !receivers.contains(requester)) || (receivers.isEmpty() && !senders.contains(requester))) {
			//ETRUSTEX-2671 && ETRUSTEX-2672
			if(senders.isEmpty()){
				senders.add(requester);
				return interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, senders, receivers);
			}else{
				receivers.add(requester);
				return interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, senders, receivers);
			}
		}
		return interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, senders, receivers);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> getInterchangeAgreements(PartyRole a, PartyRole b, Profile p, CIALevel l) {
		return interchangeAgreementDAO.getInterchangeAgreements(a, b, p, l);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement createInterchangeAgreement(InterchangeAgreement ica) {
		return interchangeAgreementDAO.create(ica);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement createInterchangeAgreement(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) {
		checkDelegationAndRoles(ica, firstDelegation, secondDelegation);
		return interchangeAgreementDAO.create(ica);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement updateInterchangeAgreement(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) {
		checkDelegationAndRoles(ica, firstDelegation, secondDelegation);
		return interchangeAgreementDAO.update(ica);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement updateInterchangeAgreement(InterchangeAgreement ica) {
		ica.setCiaLevel(decideCIALevels(ica));
		return interchangeAgreementDAO.update(ica);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public InterchangeAgreement getInterchangeArgreement(Long icaID) {
		InterchangeAgreement ica = interchangeAgreementDAO.read(icaID);
		if (ica != null && ica.getPartyRoles() != null) {
			ica.getPartyRoles().iterator();
		}
		return ica;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteInterchangeAgreement(InterchangeAgreement ica) {
		if (isUsed(ica.getId())) {
			throw new RecordInUseException("InterchangeAgreement", ica.getId());
		}
		ArrayList<Long> toDelete = new ArrayList<Long>();
		for(PartyRole pr : ica.getPartyRoles()){
			if(pr.getIcas().size()==1){
				toDelete.add(pr.getId());
			}
		}
		interchangeAgreementDAO.delete(ica);
		for (Long id: toDelete) {
			partyRoleDAO.delete(partyRoleDAO.read(id));
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> findInterchangeAgreementsByCriteria(ICASearchDTO dto) {
		List<InterchangeAgreement> result = interchangeAgreementDAO.findInterchangeAgreementsByCriteria(dto);
		for (InterchangeAgreement agr : result) {
			Hibernate.initialize(agr.getPartyRoles());
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteInterchangeAgreementById(Long icaId) {

		if (isUsed(icaId)) {
			throw new RecordInUseException("InterchangeAgreement", icaId);
		}
		InterchangeAgreement ica = getInterchangeArgreement(icaId);
		ArrayList<Long> toDelete = new ArrayList<Long>();
		for(PartyRole pr : ica.getPartyRoles()){
			if(pr.getIcas().size()==1){
				toDelete.add(pr.getId());
			}
		}
		interchangeAgreementDAO.delete(ica);
		for (Long id: toDelete) {
			partyRoleDAO.delete(partyRoleDAO.read(id));
		}
		return true;
	}

	private CIALevel decideCIALevels(InterchangeAgreement ica) {
		CIALevel ciaLevels = ica.getCiaLevel();

		logger.info("Deciding interchange agreement CIA levels to apply [Order: interchange agreement -> profile -> default(0, 0, 0)]... ");

		// if any CIA level information provided check database
		if (ciaLevels != null && ciaLevels.getConfidentialityLevel() != null && ciaLevels.getIntegrityLevel() != null
				&& ciaLevels.getAvailabilityLevel() != null) {

			List<CIALevel> ciaLvl = ciaLevelDAO.retrieveCIALevel(ciaLevels.getConfidentialityLevel(), ciaLevels.getIntegrityLevel(),
					ciaLevels.getAvailabilityLevel());

			if (CollectionUtils.isNotEmpty(ciaLvl)) {
				return ciaLvl.get(0);// TODO: Warning: take first result (DB is
				// configured to allow multiple
				// identical
			}

			return ciaLevels;
		}

		// if no CIA level information provided, take profile CIA level
		// information as default
		if (ica.getProfile() != null && ica.getProfile().getCiaLevel() != null) {
			return ica.getProfile().getCiaLevel();
		}

		// if no CIA level information available, use default 0 - 0 - 0 levels
		List<CIALevel> defaultCIALevels = ciaLevelDAO.retrieveCIALevel(0, 0, 0);
		if (CollectionUtils.isEmpty(defaultCIALevels)) {
			CIALevel defaultCIALvls = new CIALevel();

			EntityAccessInfo eai = new EntityAccessInfo();
			eai.setCreationId(ica.getAccessInfo().getCreationId());

			defaultCIALvls.setAccessInfo(eai);
			defaultCIALvls.setConfidentialityLevel(0);
			defaultCIALvls.setIntegrityLevel(0);
			defaultCIALvls.setAvailabilityLevel(0);
			return defaultCIALvls;
		}

		return defaultCIALevels.get(0);// Warning: take first result (DB is
		// configured to allow multiple
		// identical
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Long> getPartiesInInterchangeAgreementFor(Long profileId, Long firstPartyRoleId, Long firsPartyId, Long secondPartyRoleId) {

		Set<Long> values = new TreeSet<Long>();
		List<InterchangeAgreement> agreements = interchangeAgreementDAO.getInterchangeAgreements(firsPartyId, firstPartyRoleId, profileId,
				secondPartyRoleId);
		for (InterchangeAgreement agr : agreements) {
			for (PartyRole pr : agr.getPartyRoles()) {
				values.add(pr.getParty().getId());
			}
		}

		List<Long> result = new ArrayList<Long>(values);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean isUnique(Long profileId, Long firstPartyId, Long firstPartyRoleId, Long secondPartyId, Long secondPartyRoleId) {
		logger.info("InterchangeAgreementService - isUnique(ica)...");
		return interchangeAgreementDAO.isUniqueBy(profileId, firstPartyId, firstPartyRoleId, secondPartyId, secondPartyRoleId, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean containsParty(Party party) {
		return interchangeAgreementDAO.containsParty(party);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean existICAwithSameRoleForParties(Role role) {
		return interchangeAgreementDAO.existICAwithSameRoleForParties(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isUsed(Long id) {
		return interchangeAgreementDAO.isUsed(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> getInterchangeAgreementsByProfile(Long profileId) {
		return interchangeAgreementDAO.getInterchangeAgreementsByProfile(profileId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> findByParty(Party party) {
		return interchangeAgreementDAO.findByParty(party);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<PartyListItemDTO> getPartiesInExistingAgreement(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId) {
		List<PartyListItemDTO> list = new ArrayList<>();

		//  JPA 2.0 spec doesn't support use of collections as parameter in constructor expressions
		for(Party party : interchangeAgreementDAO.getPartiesInExistingAgreement(firstPartyId, firstPartyRoleId, profileId, secondPartyRoleId)) {
			list.add(new PartyListItemDTO(party.getId(),
					party.getName(),
					party.getIdentifiers(),
					partyAgreementDAO.getDelegateThirdPartiesNamesFor(party.getId())));
		}

		return list;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<PartyListItemDTO> getSecondParties(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId, Long businessDomainId) {
		List<PartyListItemDTO> list = new ArrayList<>();

		List<Party> parties = new ArrayList<Party>();
		parties.addAll(interchangeAgreementDAO.getSecondParties(firstPartyId, firstPartyRoleId, profileId, secondPartyRoleId, businessDomainId));
		parties.addAll(partyDAO.findPartiesByCriteria(null, null, null, Arrays.asList(businessDomainId), false, true));
		for(Party party : parties) {
			list.add(new PartyListItemDTO(party.getId(),
					party.getName(),
					party.getIdentifiers(),
					partyAgreementDAO.getDelegateThirdPartiesNamesFor(party.getId())));
		}

		return list;
	}

	private void checkDelegationAndRoles(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) {
		checkPartyAgreement(firstDelegation);
		checkPartyAgreement(secondDelegation);
		ica.setCiaLevel(decideCIALevels(ica));
		reuseExistingPartyRole(ica);
	}

	private void checkPartyAgreement(PartyAgreement agreement) {
		if (agreement != null && !partyAgreementDAO.existsPartyAgreement(agreement.getAuthorizingParty(), agreement.getDelegateParty())) {
			partyAgreementDAO.create(agreement);
		}
	}

	private void reuseExistingPartyRole(InterchangeAgreement ica) {

		Iterator<PartyRole> it = ica.getPartyRoles().iterator();
		PartyRole partyRole1 = it.next();
		PartyRole partyRole2 = it.next();

		List<PartyRole> existingPartyRoles = partyRoleDAO.getPartyRoles(partyRole1.getParty().getId(), partyRole1.getRole().getId());
		if (CollectionUtils.isNotEmpty(existingPartyRoles)) {
			ica.getPartyRoles().remove(partyRole1);
			ica.getPartyRoles().add(existingPartyRoles.get(0));
		}

		existingPartyRoles = partyRoleDAO.getPartyRoles(partyRole2.getParty().getId(), partyRole2.getRole().getId());
		if (CollectionUtils.isNotEmpty(existingPartyRoles)) {
			ica.getPartyRoles().remove(partyRole2);
			ica.getPartyRoles().add(existingPartyRoles.get(0));
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<InterchangeAgreement> getInterchangeAgreements(Party requester, Party party1, Party party2,	List<String> documentTypeIds) {

		Set<Party> set1 = new HashSet<Party>();
		Set<Party> set2 = new HashSet<Party>();

		if (party1 != null) {
			set1.add(party1);
		}
		if (party2 != null) {
			set2.add(party2);
		}

		if((party1 == null || !party1.equals(requester)) && (party2 == null || !party2.equals(requester))){
			return new ArrayList<InterchangeAgreement>();
		}

		return interchangeAgreementDAO.getInterchangeAgreements(documentTypeIds, set1, set2);
	}


}
