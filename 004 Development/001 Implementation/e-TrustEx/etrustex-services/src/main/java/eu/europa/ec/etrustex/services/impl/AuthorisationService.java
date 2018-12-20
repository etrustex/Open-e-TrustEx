package eu.europa.ec.etrustex.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.dao.IInterchangeAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.IServiceEndpointDao;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgencyManager;
import eu.europa.ec.etrustex.types.MetaDataItemType;

@Service("authorisationService")
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
	public boolean checkPartyAgreementForTransaction(Party authorizingParty, Party delegateParty, Long transactionId) {
		return partyAgreementDAO.existsPartyAgreementForTransaction(authorizingParty, delegateParty, transactionId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Transaction> getTransactionsByDocumentTypeCd(String documentTypeCode) {
		return transactionDAO.getTransactionsByDocumentTypeCd(documentTypeCode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Transaction> getTransactionsForDocument(String documentNamespace, String documentlocalName) {
		return transactionDAO.getTransactionsForDocument(documentNamespace, documentlocalName);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public void checkTransactionAuthorisation(Party issuerParty, Party senderParty, Long transactionId) throws AuthorisationFailedException {
		List<InterchangeAgreement> agrs;

		if (issuerParty.getId().compareTo(senderParty.getId()) != 0 && !checkPartyAgreementForTransaction(senderParty, issuerParty, transactionId)) {
			throw new AuthorisationFailedException("error.unauthorized.noPartyAgreement");

		}
		agrs = interchangeAgreementDAO.getInterchangeAgreements(senderParty, null, transactionId, null);
		if (CollectionUtils.isEmpty(agrs)) {
			throw new AuthorisationFailedException("error.unauthorized.noICA");
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public InterchangeAgreement checkAuthorisation(Party issuerParty, Party senderParty, Party receiverParty, Long transactionId, Profile profile)
			throws AuthorisationFailedException {
		Boolean authorised = false;
		
		if(profile != null){
			boolean transactionIsOnProfile = false;
			for (Transaction tx : profile.getTransactions()) {
				if(transactionId.equals(tx.getId())){
					transactionIsOnProfile = true;
					break;
				}
			}
			if(!transactionIsOnProfile){
				throw new AuthorisationFailedException("error.unauthorized.mismatchTransactionProfile");
			}
		}
		
		List<InterchangeAgreement> agrs;
		InterchangeAgreement agr;

		if (issuerParty.getId().compareTo(senderParty.getId()) != 0) {
			if (!checkPartyAgreementForTransaction(senderParty, issuerParty, transactionId)) {
				throw new AuthorisationFailedException("error.unauthorized.noPartyAgreement");
			}
		}
		
		Long profileId = profile != null ? profile.getId() : null;
		agrs = interchangeAgreementDAO.getInterchangeAgreements(senderParty, receiverParty, transactionId, profileId);
		
		if (agrs == null || agrs.size() < 1) {
			throw new AuthorisationFailedException("error.unauthorized.noICA");
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
		
		if(agr.getProfile() != null && agr.getProfile().getTransactions() != null){
			Set<Transaction> transactions = agr.getProfile().getTransactions();
			for (Transaction transaction : transactions) {
				if ((transaction.getReceiverRole().getName().equalsIgnoreCase(receiverRole.getName()) || "ANY".equalsIgnoreCase(transaction
						.getReceiverRole().getCode()))
						&& ((transaction.getSenderRole().getName().equalsIgnoreCase(senderRole.getName()) || "ANY".equalsIgnoreCase(transaction
								.getSenderRole().getCode()))) && transactionId.compareTo(transaction.getId()) == 0) {
					authorised = true;
				}
			}
		}
		

		if (!authorised) {
			throw new AuthorisationFailedException("error.unauthorized.mismatchPartyRoles");
		}
		return agr;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getMessageIssuer(String authenticatedUser) throws UndefinedIdentifierException {
		if (StringUtils.isBlank(authenticatedUser)) {
			return null;
		}
		Party issuerParty = partyDAO.getMessageIssuer(authenticatedUser);
		if (issuerParty == null) {
			throw new UndefinedIdentifierException(ErrorResponseCode.AUTHENTICATION_FAILED.getDescription());
		}
		return issuerParty;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Transaction getTransactionByNameSpace(String transactionNameSpace, String transactionRequestLocalName) {
		return transactionDAO.getTransactionByNameSpace(transactionNameSpace, transactionRequestLocalName);
	}

	@Override
    @Transactional(readOnly = true)
	public Boolean isSignatureRequired(String authenticatedUser) {
		return credentialsDAO.isSignatureRequired(authenticatedUser);
	}
	
	private String resolveSeparator(){
		String separator = ":";
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long) null, null, null, null, null);
		if (metadata.containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)) {
			separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
		} else {
			logger.error("--> 'SCHEME_ID_SEPARATOR' Metadata is not configured so ':' will be used");
		}		
		return separator;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getParty(String partyIdWithScheme, BusinessDomain bd) throws UndefinedIdentifierException {

		if (partyIdWithScheme == null || partyIdWithScheme.length() == 0) {
			throw new UndefinedIdentifierException("Party Empty");
		}
		String separator = resolveSeparator();
		int separatorIndex = partyIdWithScheme.indexOf(separator);
		String schemeId = separatorIndex != -1 
				? partyIdWithScheme.substring(0,  separatorIndex) : IdentifierIssuingAgency.GLN.getSchemeID();			
		String identifier = separatorIndex != -1 
				? partyIdWithScheme.substring(separatorIndex + 1) : partyIdWithScheme;
		Party party = partyDAO.getParty(IdentifierIssuingAgencyManager.getAgencyOfSchemeID(schemeId), identifier, bd);
		if (party == null) {
			throw new UndefinedIdentifierException("Party " + partyIdWithScheme + " not found.");
		}
		return party;
	}

	/**
	 * For the moment a very permissive method Filters out the transaction only
	 * of there is a ServiceEndpoint configured and the transaction is in the
	 * exclusion list
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isTransactionAllowed(String endpointName, Transaction trans) {
		return serviceEndpointDAO.existsEndpointByNameAndTransaction(endpointName, trans);
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
