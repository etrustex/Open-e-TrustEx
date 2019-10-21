package eu.europa.ec.etrustex.admin.old.web.services;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import eu.europa.ec.etrustex.admin.old.web.utils.LogsUtil;
import eu.europa.ec.etrustex.dao.*;
import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import eu.europa.ec.etrustex.types.EventNotificationType;

/**
 * @author guerrpa
 */
@Service
public class ICANotificationService implements IICANotificationService {

    private static final Logger logger = LoggerFactory.getLogger(ICANotificationService.class);

    private static final String PROFILE_NAME_TOOLBOX = "Toolbox";
    private static final String ROLE_CODE_ANY = "ANY";

    @Autowired
    private IInterchangeAgreementDAO interchangeAgreementDAO;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IPartyRoleDao partyRoleDAO;
    @Autowired
    private IPartyAgreementDAO partyAgreementDAO;
    @Autowired
    private ICIALevelDAO ciaLevelDAO;
    @Autowired
    private IPartyDAO partyDAO;
    @Autowired
    @Qualifier("jmsSenderService")
    private IMessageSenderService jmsSenderService;
    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    private IMetadataService metadataService;
    @Autowired
    private IRoleDAO roleDao;
    @Autowired
    private IProfileDAO profileDao;
    @Autowired
    private LogsUtil logsUtil;
    @Autowired
    private ILogService logService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public InterchangeAgreement createAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws JAXBException {
        checkDelegationAndRoles(ica, firstDelegation, secondDelegation);

        InterchangeAgreement interchangeAgreement = interchangeAgreementService.createInterchangeAgreement(ica);

        Long businessDomainId = ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();
        Long profileId = ica.getProfile().getId();

        // Get event notifications
        EventType eventType = eventNotificationService.getCreateInterchangeAgreementyEventNotification(interchangeAgreement);
        // Submit event notifications
        try {
            submitNotifications(businessDomainId, profileId, eventType);
        }catch (JAXBException e){
            throw e;
        }

        Profile toolboxProfile = profileDao.findByName(PROFILE_NAME_TOOLBOX);
        //Failure during toolboxICA creation should not impact the main ICA creation
        try {
            boolean isMetadataActivated = isAutoToolboxICACreationMDActivated(ica);
            boolean isNotToolbox = !toolboxProfile.getId().equals(profileId);
            boolean isToolboxNotConfigured = isToolboxICANotConfigured(ica, toolboxProfile);
            if (isMetadataActivated && isNotToolbox && isToolboxNotConfigured) {
                InterchangeAgreement interchangeAgreementToolbox = new InterchangeAgreement();
                interchangeAgreementToolbox.setAccessInfo(interchangeAgreement.getAccessInfo());
                interchangeAgreementToolbox.setProfile(toolboxProfile);
                interchangeAgreementToolbox.setCiaLevel(interchangeAgreement.getCiaLevel());
                interchangeAgreementToolbox.setValidityStartDate(interchangeAgreement.getValidityStartDate());

                Iterator<PartyRole> it = ica.getPartyRoles().iterator();
                PartyRole partyRole1 = it.next();
                PartyRole partyRole2 = it.next();

                Role anyRole = roleDao.findByCode(ROLE_CODE_ANY);

                interchangeAgreementToolbox.getPartyRoles().add(createOrLoadPartyRole(partyRole1.getParty(), anyRole, interchangeAgreement.getAccessInfo()));
                interchangeAgreementToolbox.getPartyRoles().add(createOrLoadPartyRole(partyRole2.getParty(), anyRole, interchangeAgreement.getAccessInfo()));

                InterchangeAgreement icaT = interchangeAgreementService.createInterchangeAgreement(interchangeAgreementToolbox);
                EventType eventTypeT = eventNotificationService.getCreateInterchangeAgreementyEventNotification(icaT);
                submitNotifications(businessDomainId, profileId, eventTypeT);
                saveLog(LogDTO.LOG_TYPE.SUCCESS, "Automatic Toolbox ICA Successfully created and notification submitted", icaT.getId());
            }
        }catch(JAXBException e){
            saveLog(LogDTO.LOG_TYPE.ERROR, "Problem while creating Toolbox ICA  "+e.getMessage(),null);
        }

        return interchangeAgreement;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public InterchangeAgreement updateAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception {
        InterchangeAgreement icaFrom = interchangeAgreementService.getInterchangeArgreement(ica.getId());

        // Get parties configured to receive notifications
        List<Party> previousConfiguredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId(), ica.getProfile().getId());

        checkDelegationAndRoles(ica, firstDelegation, secondDelegation);
        InterchangeAgreement interchangeAgreement = interchangeAgreementService.updateInterchangeAgreement(ica);


        // Get event notification
        EventType eventType = eventNotificationService.getUpdateInterchangeAgreementyEventNotification(icaFrom, interchangeAgreement);

        // New list of parties configured to receive notifications
        List<Party> newConfiguredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, interchangeAgreement.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId(), interchangeAgreement.getProfile().getId());

        newConfiguredParties.addAll(previousConfiguredParties);

        // Convert list to set to avoid repeated notifications
        Set<Party> configuredParties = new HashSet<>(newConfiguredParties);

        // Submit event notifications
        for (Party p : configuredParties) {
            PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
            PartyIdentifier senderId = eventNotificationService.getSenderId(p);
            jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
        }

        return interchangeAgreement;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteAndSendNotification(Long icaId) throws RecordInUseException, JAXBException {
        if (interchangeAgreementService.isUsed(icaId)) {
            throw new RecordInUseException("InterchangeAgreement", icaId);
        }

        InterchangeAgreement interchangeAgreement = interchangeAgreementService.getInterchangeArgreement(icaId);

        Long profileId = interchangeAgreement.getProfile().getId();
        Long businessDomainId = interchangeAgreement.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();

        interchangeAgreementService.deleteInterchangeAgreement(interchangeAgreement);

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

    private PartyRole createOrLoadPartyRole(Party party, Role role, EntityAccessInfo eai){
        PartyRole pr = null;
        List<PartyRole> prs = partyRoleDAO.getPartyRoles(party.getId(), role.getId());
        if(prs.size() >= 1){
            pr = prs.get(0);
        }else{
            pr = new PartyRole();
            pr.setParty(party);
            pr.setRole(role);
            pr.setAccessInfo(eai);
            return pr;
        }
        return pr;
    }

    private boolean isToolboxICANotConfigured(InterchangeAgreement ica, Profile toolboxProfile){

        boolean notConf = false;

        Role anyRole = roleDao.findByCode(ROLE_CODE_ANY);

        if(ica.getPartyRoles().size() >= 2) {
            Iterator<PartyRole> pr = ica.getPartyRoles().iterator();
            PartyRole pr1 = pr.next();
            PartyRole pr2 = pr.next();
            notConf = interchangeAgreementDAO.isUniqueBy(toolboxProfile.getId(), pr1.getParty().getId(), anyRole.getId(), pr2.getParty().getId(), anyRole.getId(),pr1.getParty().getBusinessDomain().getId());
        }

        return notConf;
    }

    /**
     * Checks if the metadata AUTOMATIC_TOOLBOX_ICA_CREATION is present and activated
     * @param ica
     * @return
     */
    private boolean isAutoToolboxICACreationMDActivated(InterchangeAgreement ica){
        BusinessDomain bd = ica.getPartyRoles().iterator().next().getParty().getBusinessDomain();

        MetaDataItem crit = new MetaDataItem();
        crit.setBusinessDomain(bd);
        crit.setRawItemType(MetaDataItemType.AUTOMATIC_TOOLBOX_ICA_CREATION.name());
        List<MetaDataItem> metadataItems = metadataService.getMetaDataItemsByCriteria(crit);
        //If Business Domain metadata is is present, then it takes precedence otherwise move to the default metadata
        if (!CollectionUtils.isEmpty(metadataItems)) {
            if(metadataItems.size() > 1){
                saveLog(LogDTO.LOG_TYPE.ERROR, "Duplicate metadata configuration",null);
                return false;
            }
            if("true".equalsIgnoreCase(metadataItems.get(0).getValue())){
                return true;
            }
        }else{
            metadataItems = metadataService.getDefaultMetadataByType(MetaDataItemType.AUTOMATIC_TOOLBOX_ICA_CREATION.name());
            if (!CollectionUtils.isEmpty(metadataItems) && "true".equalsIgnoreCase(metadataItems.get(0).getValue())){
                return true;
            }
        }
        return false;
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

    private void submitNotifications(Long businessDomainId, Long profileId, EventType eventType) throws JAXBException {
        // Get parties configured to receive notifications
        List<Party> configuredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, businessDomainId, profileId);

        for (Party p : configuredParties) {
            PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
            PartyIdentifier senderId = eventNotificationService.getSenderId(p);
            jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
        }
    }

    private void saveLog(LogDTO.LOG_TYPE logType, String description, Long entityId) {
        LogDTO logDTO = logsUtil.getLog(LogDTO.LOG_OPERATION.CREATE,
                LogDTO.LOG_TYPE.SUCCESS, InterchangeAgreement.class, entityId, "create");

        logDTO.setLogType(logType);
        logDTO.setDescription(description);

        logService.saveLog(logDTO);
        logsUtil.printLog(logDTO);
    }
}
