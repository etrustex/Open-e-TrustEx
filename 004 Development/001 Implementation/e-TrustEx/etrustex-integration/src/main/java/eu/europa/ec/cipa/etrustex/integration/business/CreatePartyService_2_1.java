package eu.europa.ec.cipa.etrustex.integration.business;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.integration.util.JmsSenderService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.IPartyAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreatePartyService_2_1 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CreatePartyService_2_1.class);
	
	private static final String CREATE_PARTY_RESPONSE_OK = "<ec:Ack xmlns:ec='ec:services:wsdl:CreateParty-2_1' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Ack>";
	private static final String XPATH_PARTY_NAME = "//*:CreateParty/*:Party/*:PartyName/*:Name";
	private static final String XPATH_PERSON = "//*:CreateParty/*:Party/*:Person";
	private static final String XPATH_PARTY_IDENTIFIER = "//*:CreateParty/*:Party/*:PartyIdentification/*:ID";
	
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
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		try{
			logger.debug("Start processMessage");
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(
					null);
			responseMessage.setHeader(message.getHeader());
			final Configuration config = new Configuration();
			DocumentInfo docInfo = null;
			try {
				docInfo = config.buildDocument(new StreamSource(new StringReader(
						message.getPayload())));
			} catch (XPathException e) {
				logger.error("Not able to parse XML");
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
			
			String partyName = null;
			try {
				partyName = queryForSingle(
						XPATH_PARTY_NAME,
						docInfo,config);
				logger.debug("Party name:"+partyName);
			} catch (XPathException e) {
				logger.error("Not able to find party Id");
				throw new TechnicalErrorException("soapenv:Server",
						"Unable to retrieve information with query ", null,
						"301", "Technical Error Occured");
			}
								
			Long issuerId = message.getHeader().getIssuerPartyId();
			logger.debug("Issuer id:"+issuerId);
			Party issuerParty = partyService.getParty(issuerId);
			logger.debug("Issuer Party:"+issuerParty);
			
			//checks first if party exists already otherwise creates a new one.
			Party dbParty = partyService.getPartyForBusinessDomain(partyName.trim(), issuerParty.getBusinessDomain().getId());
			if (dbParty != null){
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			
			logger.debug("Create new party");
			Party party = new Party();
			party.setName(partyName);
			logger.debug("Create new party identifier");
			
			retrievePartyIdentifiers(docInfo, config, party, message);
			
			party.setBusinessDomain(issuerParty.getBusinessDomain());
			party.setNaturalPersonFlag(queryForNode(XPATH_PERSON, docInfo, config).next() != null);
			party.setAccessInfo(createAccessInfo(message));
			party.setThirdPartyFlag(false);
			partyService.createParty(party);	
			
			responseMessage.setPayload(CREATE_PARTY_RESPONSE_OK);
			
			sendNotification(issuerParty.getBusinessDomain().getId(), party);
			
			logger.debug(responseMessage.getPayload());
			logger.debug("Stop processMessage");			
			return responseMessage;
		} catch (MessageProcessingException mpe) {
			logger.error(mpe.getMessage(), mpe);
			throw mpe;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void sendNotification(Long businessDomainId, Party party) throws TechnicalErrorException {
		EventType eventType = eventNotificationService.getCreatePartyEventNotification(party);
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

	private void retrievePartyIdentifiers(DocumentInfo documentInfo, Configuration configuration, Party party, TrustExMessage<String> message) throws XPathException {

		final SequenceIterator<NodeInfo> iter = queryForNode(XPATH_PARTY_IDENTIFIER, documentInfo, configuration);

		NodeInfo node = iter.next();
		List<String> schemeIds = new ArrayList<>();
		Set<PartyIdentifier> identifiers = new HashSet<>();
		while (node != null) {
			PartyIdentifier partyIdentifier = new PartyIdentifier();
			partyIdentifier.setParty(party);
			String schemeId = node.getAttributeValue("", "schemeID");
			//schemeID is GLN if empty 
			schemeId = StringUtils.isNotEmpty(schemeId) ? schemeId : "GLN";
			if (schemeIds.contains(schemeId)) {
				//no two identifiers should have the same schemeID
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			schemeIds.add(schemeId);
			try {
				partyIdentifier.setSchemeId(IdentifierIssuingAgency.valueOf(schemeId.replaceFirst(":", "_")));
			} catch (IllegalArgumentException e) {
				//schemeID is not valid
				if (e.getMessage().startsWith("No enum constant")) {
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
				}
			}
			partyIdentifier.setAccessInfo(createAccessInfo(message));
			partyIdentifier.setValue(node.getStringValue());
			identifiers.add(partyIdentifier);
			node = iter.next();
		}
		party.setIdentifiers(identifiers);
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
