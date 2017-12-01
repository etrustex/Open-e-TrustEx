package eu.europa.ec.cipa.eprocurement.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.CIALevel;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.services.ICIALevelService;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyRoleService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.IProfileService;
import eu.europa.ec.cipa.etrustex.services.IRoleService;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;

public class CreateInterchangeAgreementService_2 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CreateInterchangeAgreementService_2.class);

	private static final String CREATE_ICA_RESPONSE_OK = "<ec:Ack xmlns:ec='ec:services:wsdl:CreateInterchangeAgreement-2' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1' xmlns:eccac='ec:schema:xsd:CommonAggregateComponents-2'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Ack>";
	private static final String XPATH_SENDER_PARTY_ID = " //*:InterchangeAgreement/*:SenderParty/*:EndpointID";
	private static final String XPATH_SENDER_PARTY_SCHEME_ID = " //*:InterchangeAgreement/*:SenderParty/*:EndpointID/@schemeID";
	private static final String XPATH_RECEIVER_PARTY_ID = " //*:InterchangeAgreement/*:ReceiverParty/*:EndpointID";
	private static final String XPATH_RECEIVER_PARTY_SCHEME_ID = " //*:InterchangeAgreement/*:ReceiverParty/*:EndpointID/@schemeID";
	private static final String DEFAULT_SCHEME = "GLN";
	@Autowired
	private IPartyService partyService;
	@Autowired
	private IPartyAgreementService partyAgreementService;
	@Autowired
	private IPartyRoleService partyRoleService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private ICIALevelService ciaLevelService;
	@Autowired
	private IProfileService profileService;
	@Autowired
	private IInterchangeAgreementService interchangeAgreementService;
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	@Autowired
	private IEventNotificationService eventNotificationService;
	@Autowired
	private IPartyDAO partyDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
		try {
			logger.debug("Start processMessage");
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(null);
			responseMessage.setHeader(message.getHeader());
			final Configuration config = new Configuration();
			DocumentInfo docInfo = null;
			try {
				docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			} catch (XPathException e) {
				logger.error("Not able to parse XML");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}
			String senderPartyId = null;
			try {
				senderPartyId = queryForSingle(XPATH_SENDER_PARTY_ID, docInfo, config);
				logger.debug("Sender Party id:" + senderPartyId);
			} catch (XPathException e) {
				logger.error("Not able to find sender party Id");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}
			String senderPartySchemeId = null;
			try {
				senderPartySchemeId = queryForSingle(XPATH_SENDER_PARTY_SCHEME_ID, docInfo, config);
			} catch (XPathException e) {
				senderPartySchemeId = DEFAULT_SCHEME;
			}
			if(senderPartySchemeId == null) senderPartySchemeId = DEFAULT_SCHEME;
			
			String receiverPartyId = null;
			try {
				receiverPartyId = queryForSingle(XPATH_RECEIVER_PARTY_ID, docInfo, config);
				logger.debug("Receiver Party id:" + receiverPartyId);
			} catch (XPathException e) {
				logger.error("Not able to find receiver party Id");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}
			String receiverPartySchemeId = null;
			try {
				receiverPartySchemeId = queryForSingle(XPATH_RECEIVER_PARTY_SCHEME_ID, docInfo, config);
			} catch (XPathException e) {
				receiverPartySchemeId = DEFAULT_SCHEME;
			}
			if(receiverPartySchemeId == null) receiverPartySchemeId = DEFAULT_SCHEME;
			
			// Retrieve Sender Party
			Party senderParty = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(senderPartySchemeId), senderPartyId, message.getHeader().getIssuer().getBusinessDomain());
			if (senderParty != null) {
				logger.debug("Sender Party exists");
			} else {
				logger.debug("Sender Party does not exist");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}
			// Retrieve Issuer Party
			Long issuerId = message.getHeader().getIssuerPartyId();
			logger.debug("Issuer id:" + issuerId);
			Party issuerParty = partyService.getParty(issuerId);
			logger.debug("Issuer Party:" + issuerParty);
			// Retrieve Sender Agreement

			try {
				partyAgreementService.retrievePartyAgreement(senderParty, issuerParty);
			} catch (Exception e) {
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}

			// Retrieve roles
			Role eoRole = roleService.getRole(8l);// get Economic Operator Role
			logger.debug("loaded eoRole" + eoRole.getId());
			Role caRole = roleService.getRole(7l);// get Contracting Authority
													// Role
			logger.debug("loaded caRole" + caRole.getId());
			Role bundleRole = roleService.getRole(6l);// get Bundle Exchanger
														// Role
			logger.debug("loaded bundleRole" + bundleRole.getId());

			// Retrieve Sender Party Role for Economic Operator
			// If not exist create the PartyRole otherwise use the reference
			List<PartyRole> prs = partyRoleService.getPartyRoles(senderParty.getId(), eoRole.getId());
			PartyRole senderPartyRole = null;
			if (prs.size() > 0) {
				logger.debug("Sender Party role exists");
				senderPartyRole = prs.get(0);
			} else {
				logger.debug("Sender Party role does not exist, create it");
				senderPartyRole = new PartyRole();
				senderPartyRole.setAccessInfo(createAccessInfo(message));
				senderPartyRole.setParty(senderParty);
				senderPartyRole.setRole(eoRole);
				senderPartyRole = partyRoleService.createPartyRole(senderPartyRole);
				logger.debug("Sender Party role created");
			}

			// Retrieve Receiver Party
			Party receiverParty = partyService.getPartyByID(IdentifierIssuingAgency.valueOf(receiverPartySchemeId), receiverPartyId, message.getHeader().getIssuer().getBusinessDomain());
			if (receiverParty != null) {
				logger.debug("Receiver Party exists");
			} else {
				logger.debug("Receiver Party does not exist");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}

			// Retrieve Receiver Party Role for Contracting Authority
			prs = partyRoleService.getPartyRoles(receiverParty.getId(), caRole.getId());
			PartyRole receiverPartyRole = null;
			if (prs.size() > 0) {
				receiverPartyRole = prs.get(0);
				logger.debug("Receiver Party role CA exists");
			} else {
				logger.debug("Receiver Party Role CA does not exist");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}

			// Search for ICA for profile 4 (EO-CA interchanges), if not exists
			// create it
			Profile profilePreAward = profileService.getProfile(4l);
			logger.debug("profile 4:" + profilePreAward.getId());
			CIALevel level = ciaLevelService.getCIALevel(2l);// get level 2 (no
																// encryption
																// and no
																// signature)
			logger.debug("level 2:" + level.getId());
			
			InterchangeAgreement preAwardICA = new InterchangeAgreement();
			
			List<InterchangeAgreement> icas = interchangeAgreementService
					.getInterchangeAgreements(senderPartyRole, receiverPartyRole, profilePreAward, level);
			if (icas.size() == 0) {
				logger.debug("ICA for profile 4 doesnot exist, create it");
				preAwardICA.setAccessInfo(createAccessInfo(message));
				preAwardICA.setCiaLevel(level);
				preAwardICA.setProfile(profilePreAward);
				preAwardICA.getPartyRoles().add(senderPartyRole);
				preAwardICA.getPartyRoles().add(receiverPartyRole);
				preAwardICA = interchangeAgreementService.createInterchangeAgreement(preAwardICA);
				logger.debug("ICA for profile 4 created");
			}else{
				preAwardICA = icas.get(0);
			}

			// Retrieve Sender Party Role for Bundle exchange

			prs = partyRoleService.getPartyRoles(senderParty.getId(), bundleRole.getId());
			senderPartyRole = null;
			if (prs.size() > 0) {
				logger.debug("Sender Party role BDL Exchanger exists");
				senderPartyRole = prs.get(0);
			} else {
				logger.debug("Sender Party role does not exist, create it");
				senderPartyRole = new PartyRole();
				senderPartyRole.setAccessInfo(createAccessInfo(message));
				senderPartyRole.setParty(senderParty);
				senderPartyRole.setRole(bundleRole);
				senderPartyRole = partyRoleService.createPartyRole(senderPartyRole);
				logger.debug("Sender Party role created");
			}

			// Retrieve Receiver Party Role for Bundle exchange
			prs = partyRoleService.getPartyRoles(receiverParty.getId(), bundleRole.getId());
			receiverPartyRole = null;
			if (prs.size() > 0) {
				logger.debug("receiver Party role BDL Exchanger exists");
				receiverPartyRole = prs.get(0);
			} else {
				//JIRA 1293: Add automatically the profile BundleExchanger to CA
				logger.debug("Receiver Party Role BDL Exchanger does not exist, create it");				
				receiverPartyRole = new PartyRole();								
				receiverPartyRole.setAccessInfo(createAccessInfo(message));
				receiverPartyRole.setParty(receiverParty);
				receiverPartyRole.setRole(bundleRole);
				receiverPartyRole = partyRoleService.createPartyRole(receiverPartyRole);				
				logger.debug("Receiver Party role created");
				//throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}

			// Search for ICA for profile 3 (Bundle exchange), if not exists
			// create it

			Profile profileBUN = profileService.getProfile(3l);
			logger.debug("profile 3:" + profileBUN.getId());

			icas = interchangeAgreementService.getInterchangeAgreements(senderPartyRole, receiverPartyRole, profileBUN, level);
			if (icas.size() == 0) {
				logger.debug("ICA for profile 3 doesnot exist, create it");
				InterchangeAgreement ica = new InterchangeAgreement();
				ica.setAccessInfo(createAccessInfo(message));
				ica.setCiaLevel(level);
				ica.setProfile(profileBUN);
				ica.getPartyRoles().add(senderPartyRole);
				ica.getPartyRoles().add(receiverPartyRole);
				ica = interchangeAgreementService.createInterchangeAgreement(ica);
				logger.debug("ICA for profile 3 created");
			}
			
			responseMessage.setPayload(CREATE_ICA_RESPONSE_OK);
			
			// Get event notifications
			EventType eventType = eventNotificationService.getCreateInterchangeAgreementyEventNotification(preAwardICA);
			// Submit event notifications
			submitNotifications(message.getHeader().getIssuer().getBusinessDomain().getId(), profilePreAward.getId(), eventType);						

			logger.debug("Stop processMessage");
			return responseMessage;
		} catch (TechnicalErrorException e) {
			// logger.error(e.getMessage(), e);//log occurs after.
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void submitNotifications(Long businessDomainId, Long profileId, EventType eventType) throws Exception {
		// Get parties configured to receive notifications
		List<Party> configuredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, businessDomainId, profileId);

		for(Party receiverParty : configuredParties) {
			PartyIdentifier receiverId = receiverParty.getIdentifiers().iterator().next();
			Party senderParty = partyDAO.getEventPublisherParty(receiverParty);
			if (senderParty == null) {
				throw new TechnicalErrorException("soapenv:Server",
						"No publisher found for subscriber " + receiverParty.getName(), null,
						"301", "Technical Error Occured");
			}
			PartyIdentifier senderId = senderParty.getIdentifiers().iterator().next();
			try {
				jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
			} catch (JAXBException e) {
				throw new TechnicalErrorException("soapenv:Server",
						"Unable to send event notification", null,
						"301", "Technical Error Occured");
			}
		}
	}

	public IPartyService getPartyService() {
		return partyService;
	}

	public void setPartyService(IPartyService partyService) {
		this.partyService = partyService;
	}

	public IPartyAgreementService getPartyAgreementService() {
		return partyAgreementService;
	}

	public void setPartyAgreementService(IPartyAgreementService partyAgreementService) {
		this.partyAgreementService = partyAgreementService;
	}

	public IPartyRoleService getPartyRoleService() {
		return partyRoleService;
	}

	public void setPartyRoleService(IPartyRoleService partyRoleService) {
		this.partyRoleService = partyRoleService;
	}

	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public IProfileService getProfileService() {
		return profileService;
	}

	public void setProfileService(IProfileService profileService) {
		this.profileService = profileService;
	}

	public ICIALevelService getCiaLevelService() {
		return ciaLevelService;
	}

	public void setCiaLevelService(ICIALevelService ciaLevelService) {
		this.ciaLevelService = ciaLevelService;
	}

	public IInterchangeAgreementService getInterchangeAgreementService() {
		return interchangeAgreementService;
	}

	public void setInterchangeAgreementService(IInterchangeAgreementService interchangeAgreementService) {
		this.interchangeAgreementService = interchangeAgreementService;
	}

}
