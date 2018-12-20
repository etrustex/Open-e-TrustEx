package eu.europa.ec.etrustex.integration.business;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.InterchangeAgreementType;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2.ObjectFactory;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2.RetrieveInterchangeAgreementsResponseType;
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;

public class RetrieveInterchangeAgreements_2 extends TrustExBusinessService
		implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(RetrieveInterchangeAgreements_2.class);

	@Autowired
	protected IInterchangeAgreementService interchangeAgreementService;
	
	private static final String SCHEME_ID_ATTRIBUTE_NAME = "schemeID";
	private static final String SENDER_PARTY_ID_NODE_XQUERY = "//*:RetrieveInterchangeAgreementsRequest/*:SenderParty/*:EndpointID";
	private static final String RECEIVER_PARTY_ID_NODE_XQUERY = "//*:RetrieveInterchangeAgreementsRequest/*:ReceiverParty/*:EndpointID";
	private static final String DOC_TYPE_CODE_XQUERY = "//*:RetrieveInterchangeAgreementsRequest/*:DocumentTypeCode";
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>> processMessage(
			TrustExMessage<String> message) throws BusinessException,
			TechnicalErrorException {

		TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>> responseMessage = 
				new TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>>(null);
		responseMessage.setHeader(message.getHeader());
		DocumentInfo docInfo;
		try {
			final Configuration config = new Configuration();
			docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			List<PartyIdentifier> senderPartyIdentifiers = extractPartyIdentifiers(docInfo, config, SENDER_PARTY_ID_NODE_XQUERY);
			logger.debug("partyIdsAsSenders count [{}]", senderPartyIdentifiers.size());
			List<PartyIdentifier> receiverPartyIdentifiers = extractPartyIdentifiers(docInfo, config, RECEIVER_PARTY_ID_NODE_XQUERY);			
			logger.debug("partyIdsAsReceivers count [{}]", receiverPartyIdentifiers.size());

			List<String> documentTypeIds = queryFor(DOC_TYPE_CODE_XQUERY, docInfo, config);
			logger.debug("documentTypeIds count [{}]", documentTypeIds.size());
			logger.debug("queries are done");
			
			checkBusinessRules(documentTypeIds);

			Long requesterId = message.getHeader().getSenderPartyId();
			Party requester = partyService.getParty(requesterId);
			
			Set<Party> senders = getPartiesByPartyIdScheme(senderPartyIdentifiers, message.getHeader().getIssuer().getBusinessDomain());
			Set<Party> receivers = getPartiesByPartyIdScheme(receiverPartyIdentifiers, message.getHeader().getIssuer().getBusinessDomain());
			logger.debug("parties retrieved");
			boolean partiesDoNotExist = (senders.isEmpty() && senderPartyIdentifiers.size() > 0) 
					|| (receivers.isEmpty() && receiverPartyIdentifiers.size() > 0);
			
			RetrieveInterchangeAgreementsResponseType response = new RetrieveInterchangeAgreementsResponseType();

			if (!partiesDoNotExist) {
				logger.debug("retrieving interchange agreement(s)");
				List<InterchangeAgreement> agreements = interchangeAgreementService
						.getInterchangeAgreements(requester, senders, receivers, documentTypeIds);
				logger.debug("retrieved [{}] interchange agreement(s)", agreements == null ? null : agreements.size());
	
				List<InterchangeAgreementType> agreementsInResponse = response.getInterchangeAgreement();

				for (InterchangeAgreement ica : agreements) {
					logger.debug("processing interchange agreement with ID [{}]", ica.getId());
					Party sender = null;
					Party receiver = null;
					//ETRUSTEX-1244 sender and receiver in response should match sender and receiver in request
					for (PartyRole partyRole : ica.getPartyRoles()) {
						Party party = partyRole.getParty();
						if (senders != null && senders.contains(party)) {
							sender = party;
						} else if (receivers != null && receivers.contains(party)) {
							receiver = party;
						}						
					}
					if (sender == null) {
						sender = requester;
						for (PartyRole partyRole : ica.getPartyRoles()) {					
							if (partyRole.getParty().getId().longValue() != sender.getId().longValue()) {
								receiver = partyRole.getParty();
								break;
							}
						}						
					} else if (receiver == null) {
						for (PartyRole partyRole : ica.getPartyRoles()) {					
							if (partyRole.getParty().getId().longValue() != sender.getId().longValue()) {
								receiver = partyRole.getParty();
								break;
							}
						}						
					}
					logger.debug("processing next interchange agreement with ID [{}]", ica.getId());
					agreementsInResponse.add(populateInterchangeAgreementType(ica, sender, receiver, ica.getCiaLevel()));
					logger.trace("added interchange agreement with ID [{}], requester [{}] -> receiver [{}] to result list in response", ica.getId(), requester.getName(), receiver.getName());
					
				}
			}
			logger.debug("building response");
			ObjectFactory factory = new ObjectFactory();
			Transaction transaction = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName = factory.createRetrieveInterchangeAgreementsResponse(response).getName().getLocalPart();

			JAXBElement<RetrieveInterchangeAgreementsResponseType> responseElement = new JAXBElement<RetrieveInterchangeAgreementsResponseType>(
					new QName(transaction.getNamespace(),
							responseElementLocalName, "ec"),
							RetrieveInterchangeAgreementsResponseType.class,response);
			
			responseMessage.setPayload(responseElement);
			logger.debug("finished building response");
			return responseMessage;
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
		}

	}

	private List<PartyIdentifier> extractPartyIdentifiers(DocumentInfo docInfo, final Configuration config, String xQuery) throws XPathException {
		SequenceIterator<NodeInfo> partyIdNodeIterator = queryForNode(xQuery, docInfo, config);
		List<PartyIdentifier> partyIdentifiers = new ArrayList<>();
		while (true) {
			NodeInfo partyIdNode = partyIdNodeIterator.next();
			if (partyIdNode == null) {
				break;
			}
			String partyId = partyIdNode.getStringValue();
			String schemeId = partyIdNode.getAttributeValue("", SCHEME_ID_ATTRIBUTE_NAME);
			PartyIdentifier partyIdentifier = new PartyIdentifier();
			partyIdentifier.setValue(partyId);
			partyIdentifier.setSchemeId(StringUtils.isNotBlank(schemeId) ? IdentifierIssuingAgency.valueOf(schemeId.replaceAll(":", "_")) : IdentifierIssuingAgency.GLN);
			partyIdentifiers.add(partyIdentifier);
		}
		return partyIdentifiers;
	}

	private void checkBusinessRules(List<String> documentTypeIds) {		
		if (documentTypeIds.size() > 500) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docTypeCode.maxNo")); 
		}
	}


	/**
	 * Return a set of Party objects based on a list of IDs. Non-existent IDs are silently discarded. If no Party objects found, the result will be null.
	 * 
	 * @param partyIdSchemes A list of Party IDs
	 * @return A set of corresponding Party objects. Will return null instead of an empty set. If a set is returned, it will contain at least one Party object.
	 */
	private Set<Party> getPartiesByPartyIdScheme(List<PartyIdentifier> partyIdSchemes, BusinessDomain businessDomain) {
		Set<Party> result = new HashSet<Party>();
		if (partyIdSchemes != null && partyIdSchemes.size() != 0) {
			for (PartyIdentifier partyIdentifier : partyIdSchemes) {
				Party p = partyService.getPartyByID(partyIdentifier.getSchemeId(), partyIdentifier.getValue(), businessDomain);
				if (p != null) {
					result.add(p);
				}
			}
		}
		return result;
	}
}
