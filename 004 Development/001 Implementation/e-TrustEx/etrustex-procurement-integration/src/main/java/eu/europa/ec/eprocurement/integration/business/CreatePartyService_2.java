package eu.europa.ec.eprocurement.integration.business;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component("CreateParty-2.0")
public class CreatePartyService_2 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CreatePartyService_2.class);

	private static final String CREATE_PARTY_RESPONSE_OK = "<ec:Ack xmlns:ec='ec:services:wsdl:CreateParty-2' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Ack>";
	private static final String XPATH_PARTY_ID = " //*:CreateParty/*:Party/*:EndpointID";

	@Autowired
	private IPartyService partyService;
	@Autowired
	private IPartyAgreementService partyAgreementService;
	@Autowired
	private IBusinessDomainService businessDomainService;
	@Autowired
	private IEventNotificationService eventNotificationService;
	@Autowired
	private IPartyDAO partyDAO;
	@Autowired
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	
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
				throw new TechnicalErrorException("soapenv:Server", "Invalid XML", null, "21", "Technical Error Occured");
			}

			String partyId = null;
			try {
				partyId = queryForSingle(XPATH_PARTY_ID, docInfo, config);
				logger.debug("Party id:" + partyId);
			} catch (XPathException e) {
				logger.error("Not able to find party Id");
				partyId = null;
			}

			Long issuerId = message.getHeader().getIssuerPartyId();
			logger.debug("Issuer id:" + issuerId);
			Party issuerParty = partyService.getParty(issuerId);
			logger.debug("Issuer Party:" + issuerParty);

			// checks first if party exists already otherwise creates a new one.
			Party party = null;
			BusinessDomain businessDomain = businessDomainService.findByName(BusinessDomain.BD_NAME.eProcurement.name());
			List<Long> bdIds = new ArrayList<>();
			bdIds.add(businessDomain.getId());
            List<Party> parties = partyService.findPartiesByCriteria(partyId, null, partyId, bdIds, null);
			
			if (parties.size() > 0) {
				party = parties.get(0);
				logger.debug("Party already exists");
			} else {
				logger.debug("Create new party");
				party = new Party();
				party.setName(partyId);
				logger.debug("Create new party identifier");

				// create GLN
				PartyIdentifier identifier = new PartyIdentifier();
				identifier.setSchemeId(IdentifierIssuingAgency.GLN);
				identifier.setValue(partyId);
				identifier.setAccessInfo(createAccessInfo(message));
				identifier.setParty(party);
				party.getIdentifiers().add(identifier);

				// create LEF
				identifier = new PartyIdentifier();
				identifier.setSchemeId(IdentifierIssuingAgency.EC_LEF);
				identifier.setValue(partyId);
				identifier.setAccessInfo(createAccessInfo(message));
				party.getIdentifiers().add(identifier);
				identifier.setParty(party);

				// assign party the business domain eProcurement
				party.setBusinessDomain(businessDomain);

				party.setAccessInfo(createAccessInfo(message));
				party.setThirdPartyFlag(false);
				partyService.createParty(party);
			}
			// checks if agreement exists, if not creates it
			if (!partyAgreementService.existsPartyAgreement(party, issuerParty)) {
				logger.debug("Create new party agreement");
				PartyAgreement agreement = new PartyAgreement();
				agreement.setAuthorizingParty(party);
				agreement.setDelegateParty(issuerParty);
				agreement.setAccessInfo(createAccessInfo(message));
				agreement = partyAgreementService.createPartyAgreement(agreement);
			}
			
			responseMessage.setPayload(CREATE_PARTY_RESPONSE_OK);
			
			/* Submit event notification */
			EventType eventType = eventNotificationService.getCreatePartyEventNotification(party);
			submitNotifications(businessDomain.getId(), eventType);
			
			logger.debug(responseMessage.getPayload());
			logger.debug("Stop processMessage");
			return responseMessage;
		} catch (TechnicalErrorException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (MessageProcessingException mpe) {
			logger.error(mpe.getMessage(), mpe);
			throw mpe;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void submitNotifications(Long businessDomainId, EventType eventType) throws Exception {
		// Get parties configured to receive notifications
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

	public IPartyAgreementService getPartyAgreementService() {
		return partyAgreementService;
	}

	public void setPartyAgreementService(IPartyAgreementService partyAgreementService) {
		this.partyAgreementService = partyAgreementService;
	}

	public IPartyService getPartyService() {
		return partyService;
	}

	public void setPartyService(IPartyService partyService) {
		this.partyService = partyService;
	}

}
