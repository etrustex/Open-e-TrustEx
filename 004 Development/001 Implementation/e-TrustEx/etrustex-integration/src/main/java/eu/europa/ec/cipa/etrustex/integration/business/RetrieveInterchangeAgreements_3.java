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

import ec.schema.xsd.commoncomponents_3.InterchangeAgreementType;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_3.ObjectFactory;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_3.RetrieveInterchangeAgreementsResponseType;
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

public class RetrieveInterchangeAgreements_3 extends TrustExBusinessService
		implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory
			.getLogger(RetrieveInterchangeAgreements_3.class);

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
			logger.trace("partyIdsAsSenders count [{}]", partyIdSchemesAsSenders.size());

			List<String> partyIdSchemesAsReceivers = queryFor(
					"//*:RetrieveInterchangeAgreementsRequest/*:ReceiverParty/*:EndpointID",
					docInfo, config);
			logger.trace("partyIdsAsReceivers count [{}]", partyIdSchemesAsReceivers.size());

			List<String> documentTypeIds = queryFor(
					"//*:RetrieveInterchangeAgreementsRequest/*:DocumentTypeCode",
					docInfo, config);
			logger.trace("documentTypeIds count [{}]", documentTypeIds.size());
			logger.debug("queries are done");
			
			checkBusinessRules(partyIdSchemesAsSenders, partyIdSchemesAsReceivers,
					documentTypeIds);

			Long requesterId = message.getHeader().getSenderPartyId();
			Party requester = authorisationService.getParty(requesterId);
			
			Set<Party> senders = getPartiesByPartyIdScheme(partyIdSchemesAsSenders, message.getHeader().getIssuer().getBusinessDomain());
			Set<Party> receivers = getPartiesByPartyIdScheme(partyIdSchemesAsReceivers, message.getHeader().getIssuer().getBusinessDomain());
			boolean specifiedOnlyNonexistentParties = senders == null && partyIdSchemesAsSenders.size() > 0 || receivers == null && partyIdSchemesAsReceivers.size() > 0;
			
			RetrieveInterchangeAgreementsResponseType response = new RetrieveInterchangeAgreementsResponseType();

			if (!specifiedOnlyNonexistentParties) {
				logger.trace("retrieving interchange agreement(s)");
				List<InterchangeAgreement> agreements = interchangeAgreementService
						.getInterchangeAgreements(requester, senders, receivers, documentTypeIds);
				logger.trace("retrieved [{}] interchange agreement(s)", agreements == null ? null : agreements.size());
	
				List<InterchangeAgreementType> agreementsInResponse = response.getInterchangeAgreement();
				Party sender = null;
				Party receiver = null;
				for (InterchangeAgreement ica : agreements) {
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
					agreementsInResponse.add(populateInterchangeAgreementType_3(ica, sender, receiver, ica.getCiaLevel()));
					logger.trace("added interchange agreement with ID [{}], requester [{}] -> receiver [{}] to result list in response", ica.getId(), requester.getName(), receiver.getName());
					
				}
			}

			ObjectFactory factory = new ObjectFactory();
			Transaction t = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName = factory.createRetrieveInterchangeAgreementsResponse(response).getName().getLocalPart();

			JAXBElement<RetrieveInterchangeAgreementsResponseType> responseElement = new JAXBElement<RetrieveInterchangeAgreementsResponseType>(
					new QName(t.getNamespace(),
							responseElementLocalName, "ec"),
							RetrieveInterchangeAgreementsResponseType.class,response);
			
			responseMessage.setPayload(responseElement);
			
			return responseMessage;
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
		}

	}

	private void checkBusinessRules(List<String> partyIdSchemesAsSenders,
			List<String> partyIdSchemesAsReceivers, List<String> documentTypeIds) {		
		if (documentTypeIds.size() > 500) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null); 
		}
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
