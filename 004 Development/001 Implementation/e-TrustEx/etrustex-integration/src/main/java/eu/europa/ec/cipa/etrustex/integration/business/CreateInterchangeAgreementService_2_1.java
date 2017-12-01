package eu.europa.ec.cipa.etrustex.integration.business;

import ec.schema.xsd.commonaggregatecomponents_2_1.InterchangeAgreementType;
import ec.schema.xsd.commonaggregatecomponents_2_1.PartyRoleType;
import ec.schema.xsd.createinterchangeagreement_2_1.CreateInterchangeAgreementType;
import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsSenderService;
import eu.europa.ec.cipa.etrustex.services.*;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateInterchangeAgreementService_2_1 extends TrustExBusinessService
		implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory
			.getLogger(CreateInterchangeAgreementService_2_1.class);

	private static final String CREATE_ICA_RESPONSE_OK = "<ec:Ack xmlns:ec='ec:services:wsdl:CreateInterchangeAgreement-2_1' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1' xmlns:eccac='ec:schema:xsd:CommonAggregateComponents-2'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Ack>";
		
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
	private IEventNotificationService eventNotificationService;
	@Autowired
	private IPartyDAO partyDAO;	
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		try {
			logger.debug("Start processMessage");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(CreateInterchangeAgreementType.class.getPackage().getName());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(message.getPayload());
			JAXBElement<CreateInterchangeAgreementType> bodyObject = (JAXBElement)unmarshaller.unmarshal(reader);
			CreateInterchangeAgreementType createInterchangeAgreementType = bodyObject.getValue();
			InterchangeAgreementType interchangeAgreementType = createInterchangeAgreementType.getInterchangeAgreement();
			
			Long issuerId = message.getHeader().getIssuerPartyId();
			logger.debug("Issuer id:"+issuerId);
			Party issuerParty = partyService.getParty(issuerId);
			logger.debug("Issuer Party:"+issuerParty);
			
			InterchangeAgreement ica = new InterchangeAgreement();
			ica.setAccessInfo(createAccessInfo(message));
			//set validity date
			if (interchangeAgreementType.getValidityPeriod() != null && interchangeAgreementType.getValidityPeriod().getStartDate() != null) {
				GregorianCalendar dateCalendar = interchangeAgreementType.getValidityPeriod().getStartDate().getValue().toGregorianCalendar();				
				if (interchangeAgreementType.getValidityPeriod().getStartTime() != null) {
					GregorianCalendar timeCalendar = interchangeAgreementType.getValidityPeriod().getStartTime().getValue().toGregorianCalendar();
					dateCalendar.set(Calendar.HOUR, timeCalendar.get(Calendar.HOUR));
					dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
					dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
				}
				ica.setValidityStartDate(dateCalendar.getTime());
			}
			//set CIA level
			if (interchangeAgreementType.getSecurityInformation() != null) {
				Integer confidentialityLevel = Integer.valueOf(interchangeAgreementType.getSecurityInformation().getConfidentialityLevelCode().getValue());
				Integer integrityLevel = Integer.valueOf(interchangeAgreementType.getSecurityInformation().getIntegrityLevelCode().getValue());
				Integer availabilityLevel = Integer.valueOf(interchangeAgreementType.getSecurityInformation().getAvailabilityLevelCode().getValue());
				ica.setCiaLevel(ciaLevelService.retrieveCIALevel(confidentialityLevel, integrityLevel, availabilityLevel));
			} else {
				ica.setCiaLevel(ciaLevelService.retrieveCIALevel(0, 0, 0));
			}
			//set profile
			String profileName = interchangeAgreementType.getProfileID().getValue();
			if (StringUtils.isBlank(profileName)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Profile does not exist for the Issuer business domain");				
			}
			List<Profile> profiles = profileService.findProfilesByCriteria(interchangeAgreementType.getProfileID().getValue(), null, issuerParty.getBusinessDomain());
			if (profiles != null && !profiles.isEmpty()) {
				ica.setProfile(profiles.get(0));
			} else {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Profile does not exist for the Issuer business domain");
			}
			//set party roles		
			List<String> roles = new ArrayList<>();
			for (PartyRoleType partyRoleType : interchangeAgreementType.getPartyRole()) {
				PartyType partyType = partyRoleType.getParty();
				Party party = partyService.getPartyForBusinessDomain(partyType.getPartyName().get(0).getName().getValue().trim(), issuerParty.getBusinessDomain().getId());
				if (party == null) {
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Party does not exist for the Issuer business domain");
				}
				String roleCode = partyRoleType.getRoleCode().getValue();
				Role role = roleService.findByCode(roleCode);
				if (!roles.contains(roleCode)) {
					roles.add(roleCode);					
				} else if (BooleanUtils.isNotTrue(role.getBidirectionalFlag())) {
					//role must be bidirectional to be used more than once
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, 
							"Role code " + roleCode + " can only be used for one of the parties of the interchange agreement");
				}
				PartyRole partyRole = new PartyRole();
				partyRole.setParty(party);
				partyRole.setRole(role);
				partyRole.setAccessInfo(createAccessInfo(message));
				ica.addPartyRole(partyRole);
				//create party agreement if necessary
				if (partyType.getAgentParty() != null) {
					PartyType agentPartyType = partyType.getAgentParty();
					Party delegatedParty = partyService.getPartyForBusinessDomain(agentPartyType.getPartyName().get(0).getName().getValue().trim(), issuerParty.getBusinessDomain().getId());
					if (delegatedParty == null || BooleanUtils.isNotTrue(delegatedParty.getThirdPartyFlag())) {
						throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
								ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, 
								"Third party does not exist for the Issuer business domain or it cannot play the role of a third party");
					}
					if (!partyAgreementService.existsPartyAgreement(party, delegatedParty)) {
						PartyAgreement partyAgreement = new PartyAgreement();
						partyAgreement.setAuthorizingParty(party);
						partyAgreement.setDelegateParty(delegatedParty);
						partyAgreement.setAccessInfo(createAccessInfo(message));
						partyAgreementService.createPartyAgreement(partyAgreement);
					}
				}
			}
			interchangeAgreementService.createInterchangeAgreement(ica);			
			
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(CREATE_ICA_RESPONSE_OK);
			responseMessage.setHeader(message.getHeader());
			
			sendNotification(issuerParty.getBusinessDomain().getId(), ica);
			
			logger.debug("End processMessage");
			return responseMessage;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void sendNotification(Long businessDomainId, InterchangeAgreement ica) throws TechnicalErrorException {
		EventType eventType = eventNotificationService.getCreateInterchangeAgreementyEventNotification(ica);
		List<Party> configuredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, businessDomainId, null);

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

	public void setPartyAgreementService(
			IPartyAgreementService partyAgreementService) {
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

	public void setInterchangeAgreementService(
			IInterchangeAgreementService interchangeAgreementService) {
		this.interchangeAgreementService = interchangeAgreementService;
	}

}
