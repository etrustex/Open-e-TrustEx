package eu.europa.ec.cipa.etrustex.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.ServiceEndpoint;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.services.dao.ICredentialsDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IInterchangeAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IServiceEndpointDao;
import eu.europa.ec.cipa.etrustex.services.dao.ITransactionDAO;
import eu.europa.ec.cipa.etrustex.services.exception.AuthorisationFailedException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.IIdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgencyManager;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class AuthorisationService implements IAuthorisationService {

	@Autowired
	private IPartyDAO partyDAO;
	@Autowired
	private ITransactionDAO transactionDAO;
	@Autowired
	private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Autowired
	private IPartyAgreementDAO partyAgreementDAO;
	@Autowired
	private ICredentialsDAO credentialsDAO;
	@Autowired
	private IMetadataService metadataService;
	@Autowired
	private IServiceEndpointDao serviceEndpointDAO;

	protected static final Logger logger = LoggerFactory.getLogger(AuthorisationService.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Transaction getTransactionById(Long tansactionId) {
		return transactionDAO.read(tansactionId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PartyAgreement retrievePartyAgreement(Party authorizingParty, Party delegateParty) {
		return partyAgreementDAO.retrievePartyAgreement(authorizingParty, delegateParty);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PartyAgreement checkPartyAgreementForTransaction(Party authorizingParty, Party delegateParty, Long transactionId) {
		PartyAgreement partyAgreement = null;
		try {
			partyAgreement = partyAgreementDAO.retrievePartyAgreement(authorizingParty, delegateParty);
		} catch (Exception e) {
			logger.error("no party agreement found"/* , e */);// no need to
																// display the
																// full stack
																// trace

		}

		if (partyAgreement != null) {
			Set<Transaction> transactions = partyAgreement.getTransactions();
			if (transactions != null && transactions.size() > 0) {
				for (Transaction t : transactions) {
					if (t.getId().compareTo(transactionId) == 0) {
						return partyAgreement;
					}
				}
			}
			// if no specific transactions restricts the agreement it means that
			// the agreement is valid for all transactions
			else {
				return partyAgreement;
			}
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Transaction> getTransactionsByDocumentTypeCd(String documentTypeCode) {
		return transactionDAO.getTransactionsByDocumentTypeCd(documentTypeCode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Transaction> getTransactionsForDocument(String documentNamespace, String documentlocalName) {
		// TODO Auto-generated method stub
		return transactionDAO.getTransactionsForDocument(documentNamespace, documentlocalName);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void checkTransactionAuthorisation(Party issuerParty, Party senderParty, Long transactionId) throws AuthorisationFailedException {
		List<InterchangeAgreement> agrs;

		if (issuerParty.getId().compareTo(senderParty.getId()) != 0) {
			PartyAgreement partyAgreement = checkPartyAgreementForTransaction(senderParty, issuerParty, transactionId);
			if (partyAgreement == null) {
				throw new AuthorisationFailedException("Issuer " + issuerParty.getName() + "is not athorised to submit documents for sender"
						+ senderParty.getName());
			}
		}
		agrs = interchangeAgreementDAO.getInterchangeAgreements(senderParty, null, transactionId);
		if (agrs == null || agrs.size() == 0) {
			throw new AuthorisationFailedException("Issuer " + issuerParty.getName() + "is not athorised to submit documents for sender"
					+ senderParty.getName());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public InterchangeAgreement checkAuthorisation(Party issuerParty, Party senderParty, Party receiverParty, Long transactionId)
			throws AuthorisationFailedException {
		Boolean authorised = false;

		List<InterchangeAgreement> agrs;
		InterchangeAgreement agr;

		if (issuerParty.getId().compareTo(senderParty.getId()) != 0) {
			PartyAgreement partyAgreement = checkPartyAgreementForTransaction(senderParty, issuerParty, transactionId);
			if (partyAgreement == null) {
				throw new AuthorisationFailedException("Issuer " + issuerParty.getName() + "is not athorised to submit documents for sender"
						+ senderParty.getName());
			}
		}

		agrs = interchangeAgreementDAO.getInterchangeAgreements(senderParty, receiverParty, transactionId);

		if (agrs == null || agrs.size() < 1) {
			throw new AuthorisationFailedException("Authorisation check Failed");
		}
		agr = agrs.get(0);

		Set<PartyRole> partyRoles = agr.getPartyRoles();
		Role senderRole = null;
		Role receiverRole = null;

		for (PartyRole partyRole : partyRoles) {
			if (partyRole.getParty().getId().equals(senderParty.getId())) {
				senderRole = partyRole.getRole();

			}
			if (partyRole.getParty().getId().equals(receiverParty.getId())) {
				receiverRole = partyRole.getRole();

			}
		}

		// TODO to review this part
		Set<Transaction> transactions = agr.getProfile().getTransactions();

		for (Transaction transaction : transactions) {
			if ((transaction.getReceiverRole().getName().equalsIgnoreCase(receiverRole.getName()) || "ANY".equalsIgnoreCase(transaction
					.getReceiverRole().getCode()))
					&& ((transaction.getSenderRole().getName().equalsIgnoreCase(senderRole.getName()) || "ANY".equalsIgnoreCase(transaction
							.getSenderRole().getCode()))) && transactionId.compareTo(transaction.getId()) == 0) {
				authorised = true;
			}
		}

		if (!authorised) {
			throw new AuthorisationFailedException("Authorisation check Failed");
		}
		return agr;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getMessageIssuer(String authenticatedUser) throws UndefinedIdentifierException {
		if (StringUtils.isBlank(authenticatedUser)) {
			return null;
		}
		try {
			return partyDAO.getMessageIssuer(authenticatedUser);
		} catch (Exception e) {
			throw new UndefinedIdentifierException(ErrorResponseCode.AUTHENTICATION_FAILED.getDescription());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Transaction getTransactionByNameSpace(String transactionNameSpace, String transactionRequestLocalName) {
		return transactionDAO.getTransactionByNameSpace(transactionNameSpace, transactionRequestLocalName);
	}

	@Override
	public Boolean isSignatureRequired(String authenticatedUser) {
		return credentialsDAO.isSignatureRequired(authenticatedUser);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getParty(String partyIdWithScheme, BusinessDomain bd) throws UndefinedIdentifierException {

		if (partyIdWithScheme == null || partyIdWithScheme.length() == 0) {
			throw new UndefinedIdentifierException("Party Empty");
		}

		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long) null, null, null, null);
		String separator = ":";
		if (metadata.containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)) {
			separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
		} else {
			logger.error("--> 'SCHEME_ID_SEPARATOR' Metadata is not configured so ':' will be used");
		}
		try {
			int separatorIndex = partyIdWithScheme.indexOf(separator);
			String schemeId = separatorIndex != -1 
					? partyIdWithScheme.substring(0,  separatorIndex) : IdentifierIssuingAgency.GLN.getSchemeID();			
			String identifier = separatorIndex != -1 
					? partyIdWithScheme.substring(separatorIndex + 1) : partyIdWithScheme;
			return getParty(schemeId, identifier, bd);
		} catch (Exception e) {
			throw new UndefinedIdentifierException("Party " + partyIdWithScheme + " not found. Message:" + e.getMessage());// hide
																															// stack
																															// trace.
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getParty(Long partyId) {
		return partyDAO.read(partyId);
	}

	@Override
	public Party getParty(String schemeIsoCode, String partyId, BusinessDomain bd) {
		IIdentifierIssuingAgency agency = IdentifierIssuingAgencyManager.getAgencyOfSchemeID(schemeIsoCode);
		return getParty(agency, partyId, bd);
	}

	/**
	 * For the moment a very permissive method Filters out the transaction only
	 * of there is a ServiceEndpoint configured and the transaction is in the
	 * exclusion list
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isTransactionAllowed(String endpointName, Transaction trans) {
		try {
			if (endpointName != null) {
				ServiceEndpoint se = serviceEndpointDAO.getEndpointByName(endpointName);
				if (se != null) {
					for (Transaction tx : se.getTransactions()) {
						if (tx.getId().equals(trans.getId())) {
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	public Party getParty(IIdentifierIssuingAgency shemeId, String partyId, BusinessDomain bd) {
		return partyDAO.getParty(shemeId, partyId, bd);
	}

	public IPartyDAO getPartyDAO() {
		return partyDAO;
	}

	public void setPartyDAO(IPartyDAO partyDAO) {
		this.partyDAO = partyDAO;
	}

	public ITransactionDAO getTransactionDAO() {
		return transactionDAO;
	}

	public void setTransactionDAO(ITransactionDAO transactionDAO) {
		this.transactionDAO = transactionDAO;
	}

	public IInterchangeAgreementDAO getInterchangeAgreementDAO() {
		return interchangeAgreementDAO;
	}

	public void setInterchangeAgreementDAO(IInterchangeAgreementDAO interchangeAgreementDAO) {
		this.interchangeAgreementDAO = interchangeAgreementDAO;
	}

	public IPartyAgreementDAO getPartyAgreementDAO() {
		return partyAgreementDAO;
	}

	public void setPartyAgreementDAO(IPartyAgreementDAO partyAgreementDAO) {
		this.partyAgreementDAO = partyAgreementDAO;
	}

	public ICredentialsDAO getCredentialsDAO() {
		return credentialsDAO;
	}

	public void setCredentialsDAO(ICredentialsDAO credentialsDAO) {
		this.credentialsDAO = credentialsDAO;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public IServiceEndpointDao getServiceEndpointDAO() {
		return serviceEndpointDAO;
	}

	public void setServiceEndpointDAO(IServiceEndpointDao serviceEndpointDAO) {
		this.serviceEndpointDAO = serviceEndpointDAO;
	}

}
