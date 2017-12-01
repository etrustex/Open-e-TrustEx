package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.InterchangeAgreementType;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2.ObjectFactory;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2.RetrieveInterchangeAgreementsResponseType;
import eu.europa.ec.cipa.etrustex.domain.CIALevel;
import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class RetrieveInterchangeAgreements_2 extends TrustExBusinessService
		implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory
			.getLogger(RetrieveInterchangeAgreements_2.class);

	@Autowired
	protected IInterchangeAgreementService interchangeAgreementService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(
			TrustExMessage<String> message) throws BusinessException,
			TechnicalErrorException {

		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(
				null);
		responseMessage.setHeader(message.getHeader());
		DocumentInfo docInfo;
		try {
			final Configuration config = new Configuration();
			docInfo = config.buildDocument(new StreamSource(new StringReader(
					message.getPayload())));

			List<String> partyIdSchemesAsSenders = queryFor(
					"//*:RetrieveInterchangeAgreementsRequest/*:SenderParty/*:EndpointID",
					docInfo, config);
			logger.debug("partyIdsAsSenders count [{}]", partyIdSchemesAsSenders.size());

			List<String> partyIdSchemesAsReceivers = queryFor(
					"//*:RetrieveInterchangeAgreementsRequest/*:ReceiverParty/*:EndpointID",
					docInfo, config);
			logger.debug("partyIdsAsReceivers count [{}]", partyIdSchemesAsReceivers.size());

			List<String> documentTypeIds = queryFor(
					"//*:RetrieveInterchangeAgreementsRequest/*:DocumentTypeCode",
					docInfo, config);
			logger.debug("documentTypeIds count [{}]", documentTypeIds.size());
			logger.debug("queries are done");
			
			checkBusinessRules(documentTypeIds);

			Long requesterId = message.getHeader().getSenderPartyId();
			Party requester = authorisationService.getParty(requesterId);
			
			Set<Party> senders = getPartiesByPartyIdScheme(partyIdSchemesAsSenders, message.getHeader().getIssuer().getBusinessDomain());
			Set<Party> receivers = getPartiesByPartyIdScheme(partyIdSchemesAsReceivers, message.getHeader().getIssuer().getBusinessDomain());
			logger.debug("parties retrieved");
			boolean specifiedOnlyNonexistentParties = senders == null && partyIdSchemesAsSenders.size() > 0 || receivers == null && partyIdSchemesAsReceivers.size() > 0;
			
			RetrieveInterchangeAgreementsResponseType response = new RetrieveInterchangeAgreementsResponseType();

			if (!specifiedOnlyNonexistentParties) {
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
			Transaction t = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName = factory.createRetrieveInterchangeAgreementsResponse(response).getName().getLocalPart();

			JAXBElement<RetrieveInterchangeAgreementsResponseType> responseElement = new JAXBElement<RetrieveInterchangeAgreementsResponseType>(
					new QName(t.getNamespace(),
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

	private boolean needToSkipTransaction(Transaction tx, InterchangeAgreement ica, List<String> documentTypeIds, Set<String> seenDocumentTypeCode) {
		Document d = tx.getDocument();
		if (d == null) {
			logger.error("document is null for transaction ID [{}] name [{}], skipping transaction", tx.getId(), tx.getName());
			return true;
		}
		
		String tc = d.getDocumentTypeCode();
		logger.debug("document type code is [{}]", tc);
		// process each document seen first
		// if a document list is specified in the request, documents not on this list will also be skipped
		if (tc == null || (documentTypeIds.size() != 0 && !documentTypeIds.contains(tc))) {
			logger.debug("document type code [{}] is either null or is not on the specified list, will be skipped", tc);
			return true;
		}
		
		if(seenDocumentTypeCode.contains(tc)) {
			logger.debug("type code [{}] seen for ica [{}], document will be skipped for this tx", ica.getId(), tc);
			return true;
		}
		seenDocumentTypeCode.add(tc);
		
		return false;
	}

	private Map<Role, Set<Party>> lookupPartiesForIcaRoles(InterchangeAgreement ica) {
		Map<Role, Set<Party>> roleToParties = new HashMap<Role, Set<Party>>();
		for (PartyRole partyRole : ica.getPartyRoles()) {
			Role role = partyRole.getRole();
			Set<Party> parties = roleToParties.get(role);
			if (parties == null) {
				parties = new HashSet<Party>();
				roleToParties.put(role, parties);
			}
			parties.add(partyRole.getParty());
		}
		return roleToParties;
	}

	private void checkBusinessRules(List<String> documentTypeIds) {		
		if (documentTypeIds.size() > 500) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null); 
		}
	}

	/**
	 * Get the CIA level determined by ica, profile or transaction, in this order of priority.
	 * @param ica
	 * @param tx
	 * @return
	 */
	private CIALevel getCiaLevel(InterchangeAgreement ica, Transaction tx) {
		return ica.getCiaLevel() != null ? ica.getCiaLevel() : ica.getProfile().getCiaLevel() != null ? ica.getProfile().getCiaLevel() : tx.getCiaLevel();
	}

	private boolean seenFirstHere(Map<Party, Set<Party>> seenPartyPairs,
			Party sender, Party receiver) {
		boolean result;
		Set<Party> receivers = seenPartyPairs.get(sender);
		if (receivers == null) {
			receivers = new HashSet<Party>();
			seenPartyPairs.put(sender, receivers);
		}
		result = !receivers.contains(receiver);
		if (result) {
			receivers.add(receiver);
			logger.trace("sender [{}] -> receiver [{}] pair seen first", sender.getName(), receiver.getName());
		} else {
			logger.trace("already seen sender [{}] -> receiver [{}] pair", sender.getName(), receiver.getName());
		}
		return result;
	}

	/**
	 * Return a set of Party objects based on a list of IDs. Non-existent IDs are silently discarded. If no Party objects found, the result will be null.
	 * 
	 * @param partyIdSchemes A list of Party IDs
	 * @return A set of corresponding Party objects. Will return null instead of an empty set. If a set is returned, it will contain at least one Party object.
	 */
	private Set<Party> getPartiesByPartyIdScheme(List<String> partyIdSchemes, BusinessDomain businessDomain) {
		Set<Party> result = null;
		if (partyIdSchemes != null && partyIdSchemes.size() != 0) {
			for (String id : partyIdSchemes) {
				try {
					Party p = authorisationService.getParty(id, businessDomain);
					if(result == null) {
						result = new HashSet<Party>(partyIdSchemes.size());
					}
					result.add(p);
				} catch (UndefinedIdentifierException e) {
					logger.info("party does not exist for party id with scheme [{}], will be ignored", id);
					// IGNORE
				}
			}
		}
		return result;
	}
}
