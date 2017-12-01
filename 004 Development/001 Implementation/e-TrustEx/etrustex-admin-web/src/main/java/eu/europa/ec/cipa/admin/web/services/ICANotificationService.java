package eu.europa.ec.cipa.admin.web.services;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsSenderService;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.dao.*;
import eu.europa.ec.cipa.etrustex.services.exception.RecordInUseException;

import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author guerrpa
 *
 */
@Service
public class ICANotificationService implements IICANotificationService {

	@Autowired private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Autowired private IPartyRoleDao partyRoleDAO;
	@Autowired private IPartyAgreementDAO partyAgreementDAO;
	@Autowired private ICIALevelDAO ciaLevelDAO;
	@Autowired private IPartyDAO partyDAO;
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	@Autowired private IEventNotificationService eventNotificationService;

	private static final Logger logger = LoggerFactory.getLogger(ICANotificationService.class);


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement createAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception {
		checkDelegationAndRoles(ica, firstDelegation, secondDelegation);

        /*
         *  ETRUSTEX-1458 I was able to save an ICA with a blank validity start date
         */
        if (ica.getValidityStartDate() == null) {
            ica.setValidityStartDate(new Date());
        }

		InterchangeAgreement interchangeAgreement = interchangeAgreementDAO.create(ica);

		Long businessDomainId = ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();
		Long profileId = ica.getProfile().getId();

		// Get event notifications
		EventType eventType = eventNotificationService.getCreateInterchangeAgreementyEventNotification(interchangeAgreement);
		// Submit event notifications
		submitNotifications(businessDomainId, profileId, eventType);

		return interchangeAgreement;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InterchangeAgreement updateAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception {
		InterchangeAgreement icaFrom = interchangeAgreementDAO.read(ica.getId());

		// Get parties configured to receive notifications
		List<Party> previousConfiguredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId(), ica.getProfile().getId());

		checkDelegationAndRoles(ica, firstDelegation, secondDelegation);
		InterchangeAgreement interchangeAgreement = interchangeAgreementDAO.update(ica);


		// Get event notification
		EventType eventType = eventNotificationService.getUpdateInterchangeAgreementyEventNotification(icaFrom, interchangeAgreement);

		// New list of parties configured to receive notifications
		List<Party> newConfiguredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, interchangeAgreement.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId(), interchangeAgreement.getProfile().getId());

		newConfiguredParties.addAll(previousConfiguredParties);

		// Convert list to set to avoid repeated notifications
		Set<Party> configuredParties = new HashSet<>(newConfiguredParties);

		// Submit event notifications
		for(Party p : configuredParties) {
			PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
			PartyIdentifier senderId = eventNotificationService.getSenderId(p);
			jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
		}

		return interchangeAgreement;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteAndSendNotification(Long icaId) throws Exception {
		if (interchangeAgreementDAO.isUsed(icaId)) {
			throw new RecordInUseException("InterchangeAgreement", icaId);
		}

		InterchangeAgreement interchangeAgreement = interchangeAgreementDAO.read(icaId);

		Long profileId = interchangeAgreement.getProfile().getId();
		Long businessDomainId = interchangeAgreement.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();

		interchangeAgreementDAO.delete(interchangeAgreement);

		// Get event notifications
		EventType eventType = eventNotificationService.getDeleteInterchangeAgreementyEventNotification(interchangeAgreement);

		// Submit event notifications
		submitNotifications(businessDomainId, profileId, eventType);

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
		if (ica.getProfile().getCiaLevel() != null) {
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

	private void submitNotifications(Long businessDomainId, Long profileId, EventType eventType) throws Exception {
		// Get parties configured to receive notifications
		List<Party> configuredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, businessDomainId, profileId);

		for(Party p : configuredParties) {
			PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
			PartyIdentifier senderId = eventNotificationService.getSenderId(p);
			jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
		}
	}

}
