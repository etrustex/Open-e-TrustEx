package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.*;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IEndpointService;
import eu.europa.ec.etrustex.services.util.EncryptionService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EndpointService implements IEndpointService {

	@Autowired
	private IEndpointDAO endpointDAO;

	@Autowired
	private IPartyAgreementDAO partyAgreementDAO;

	@Autowired
	private IPartyDAO partyDAO;

	@Autowired
	private IBusinessDomainDAO businessDomainDAO;

	@Autowired
	private ICredentialsDAO credentialsDAO;

	@Autowired
	private IInterchangeAgreementDAO interchangeAgreementDAO;

	@Autowired
	private ITransactionDAO transactionDAO;

	@Autowired
	private IProfileDAO profileDAO;

	@Autowired
	private EncryptionService encryptionService;


	protected static final Logger logger = LoggerFactory.getLogger(EndpointService.class);

	/**
	 * Decrypts the passwords upon retrieval
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Endpoint findById(Long id) {
		Endpoint e = endpointDAO.read(id);

		if (e != null) {
			initialize(e);
		}else{
			return null;
		}

		try {
			decryptPasswords(e);
		} catch (UnrecoverableKeyException | InvalidKeyException | KeyStoreException | NoSuchAlgorithmException
				| IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IOException e1) {
			throw new RuntimeException(e1.getMessage());
		}

		return e;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Set<Endpoint> retrieveEndpoints(Long icaId, Long partyId,
										   Long profileId, Long transactionId) {

		Set<Endpoint> endpoints = new HashSet<>();

		if (partyId !=null && icaId != null){
			endpoints.addAll(endpointDAO.getInterchangeAgreementEndpoints(icaId, partyId));
		}

		if (partyId != null && transactionId != null){
			endpoints.addAll(endpointDAO.getTransactionEndpoints(transactionId, partyId));
			// ETRUSTEX-1363 search for endpoints configured on 3rd parties and transactions
			endpoints.addAll(endpointDAO.getThirdPartyEndpointsForTransaction(partyId, transactionId));
		}

		if (partyId != null && profileId != null){
			endpoints.addAll(endpointDAO.getProfileEndpoints(profileId, partyId));
		}
		if (partyId!=null && profileId !=null && transactionId != null){
			endpoints.addAll(endpointDAO.getThirdPartyEndpointsForProfile(profileId, partyId, transactionId));
		}
		return endpoints;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Endpoint create(Endpoint endpoint, String userId) {

		try {
			mergeProperties(endpoint, userId);
		} catch (IOException | GeneralSecurityException e) {
			throw new RuntimeException(e.getMessage());
		}

		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationId(userId);

		return endpointDAO.create(endpoint);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Endpoint update(Endpoint endpoint, String userId) {
		/*
		 * Delete orphan credentials if new credentials are added or changed
		 */
		Endpoint existingEndpoint = endpointDAO.read(endpoint.getId());
		deleteOrphanCredentials(existingEndpoint, endpoint.getCredentials(),false);
		deleteOrphanCredentials(existingEndpoint, endpoint.getProxyCredential(),false);

		try {
			mergeProperties(endpoint, userId);
		} catch (IOException | GeneralSecurityException e) {
			throw new RuntimeException(e.getMessage());
		}

		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setModificationId(userId);

		return endpointDAO.update(endpoint);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Endpoint endpoint) {
		endpoint = endpointDAO.read(endpoint.getId());

		/*
		 * UC130_BR25 Deleting the authentication or proxy credentials from the endpoint configuration shall also remove
		 * them from the System (prevention of orphan records) if there are no other endpoints using them.
		 */
		deleteOrphanCredentials(endpoint, endpoint.getProxyCredential(),true);
		deleteOrphanCredentials(endpoint, endpoint.getCredentials(), true);

		endpointDAO.delete(endpoint);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint) {
		List<? extends Endpoint> list = endpointDAO.findEndpointsByCriteria(endpoint);

		for(Endpoint e : list) {
			initialize(e);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass) {
		return credentials == null ? null : endpointDAO.findByCredentialsTypeAndUsernameLike(credentials, businessDomainId, endppointClass);
	}

	@Override
	@Transactional(readOnly = true)
	public List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class endppointClass) {
		return credentials == null ? null : endpointDAO.findByCredentialsTypeAndUsernameEquals(credentials, businessDomainId, endppointClass);
	}


	public IEndpointDAO getEndpointDAO() {
		return endpointDAO;
	}

	public void setEndpointDAO(IEndpointDAO endpointDAO) {
		this.endpointDAO = endpointDAO;
	}

	public IPartyAgreementDAO getPartyAgreementDAO() {
		return partyAgreementDAO;
	}

	public void setPartyAgreementDAO(IPartyAgreementDAO partyAgreementDAO) {
		this.partyAgreementDAO = partyAgreementDAO;
	}

	private void mergeProperties(Endpoint endpoint, String userId) throws IOException, GeneralSecurityException{
		EntityAccessInfo eai = new EntityAccessInfo();

		if(endpoint.getBusinessDomain() != null && endpoint.getBusinessDomain().getId() != null && !endpoint.getBusinessDomain().getId().equals(Long.valueOf(-1))) {
			endpoint.setBusinessDomain(businessDomainDAO.read(endpoint.getBusinessDomain().getId()));
		} else {
			endpoint.setBusinessDomain(null);
		}

		if(endpoint.getProfile() != null && endpoint.getProfile().getId() != null) {
			endpoint.setProfile(profileDAO.read(endpoint.getProfile().getId()));
		} else {
			endpoint.setProfile(null);
		}

		if(endpoint.getTansaction() != null && endpoint.getTansaction().getId() != null) {
			endpoint.setTansaction(transactionDAO.read(endpoint.getTansaction().getId()));
		} else {
			endpoint.setTansaction(null);
		}

		if(endpoint.getInterchangeAgreement() != null && endpoint.getInterchangeAgreement().getId() != null) {
			endpoint.setInterchangeAgreement(interchangeAgreementDAO.read(endpoint.getInterchangeAgreement().getId()));
		} else {
			endpoint.setInterchangeAgreement(null);
		}

		if(endpoint.getParty() != null && endpoint.getParty().getId() != null) {
			endpoint.setParty(partyDAO.read(endpoint.getParty().getId()));
		} else {
			endpoint.setParty(null);
		}


		if(endpoint.getCredentials() != null) {
			String username = endpoint.getCredentials().getUser();
			String password = endpoint.getCredentials().getPassword();

			if(endpoint.getCredentials().getId() == null) {
				if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
					// Credentials creation
					Credentials credentials = endpoint.getCredentials();
					eai.setCreationId(userId);
					populateCredentials(credentials, eai);
					encryptPassword(credentials);
					endpoint.setCredentials(credentialsDAO.create(credentials));
				} else {
					endpoint.setCredentials(null);
				}
			} else {
				// Credentials reused
				endpoint.setCredentials(credentialsDAO.read(endpoint.getCredentials().getId()));

				if(endpoint.getCredentials() != null) { // otherwise credentials have been removed
					boolean changed = false;

					if(!StringUtils.equals(endpoint.getCredentials().getUser(), username)) {
						// user changed
						endpoint.getCredentials().setUser(username);
						changed = true;
					}

					if(endpoint.getCredentials().getIv() == null){
						//Password not encrypted: The new password will be used and get encrypted
						endpoint.getCredentials().setPassword(password);
						encryptPassword(endpoint.getCredentials());
						changed = true;
					}else{
						String encryptedSentPassword = encryptionService.encryptMessageInAES(password, endpoint.getCredentials().getIv());
						if(!StringUtils.equals(endpoint.getCredentials().getPassword(), encryptedSentPassword)){
							endpoint.getCredentials().setPassword(password);
							encryptPassword(endpoint.getCredentials());
							changed = true;
						}
					}

					if(changed) {
						endpoint.getCredentials().getAccessInfo().setModificationId(userId);
					}
				}
			}
		}


		if(endpoint.getProxyCredential() != null) {
			String proxyUsername = endpoint.getProxyCredential().getUser();
			String proxyPassword = endpoint.getProxyCredential().getPassword();

			if(endpoint.getProxyCredential().getId() == null) {
				// ProxyCredential creation
				if(StringUtils.isNotEmpty(proxyUsername) && StringUtils.isNotEmpty(proxyPassword)) {
					Credentials credentials = endpoint.getProxyCredential();
					eai.setCreationId(userId);
					populateCredentials(credentials, eai);
					encryptPassword(credentials);
					endpoint.setProxyCredential(credentialsDAO.create(credentials));
				} else {
					endpoint.setProxyCredential(null);
				}
			} else {
				// ProxyCredential reused
				endpoint.setProxyCredential(credentialsDAO.read(endpoint.getProxyCredential().getId()));

				if(endpoint.getProxyCredential() != null) { // otherwise credentials have been removed
					boolean changed = false;

					if(!StringUtils.equals(endpoint.getProxyCredential().getUser(), proxyUsername)) {
						// user changed
						endpoint.getProxyCredential().setUser(proxyUsername);
						changed = true;
					}

					if(endpoint.getProxyCredential().getIv() == null){
						//Password not encrypted: The new password will be used and get encrypted
						endpoint.getProxyCredential().setPassword(proxyPassword);
						encryptPassword(endpoint.getProxyCredential());
						changed = true;
					}else{
						String encryptedSentPassword = encryptionService.encryptMessageInAES(proxyPassword, endpoint.getProxyCredential().getIv());
						if(!StringUtils.equals(endpoint.getProxyCredential().getPassword(), encryptedSentPassword)){
							endpoint.getProxyCredential().setPassword(proxyPassword);
							encryptPassword(endpoint.getProxyCredential());
							changed = true;
						}
					}

					if(changed) {
						endpoint.getProxyCredential().getAccessInfo().setModificationId(userId);
					}
				}
			}

		}
	}

	private void initialize(Endpoint e) {
		Hibernate.initialize(e.getCredentials());
		Hibernate.initialize(e.getProxyCredential());
		if(e.getBusinessDomain() != null) {
			Hibernate.initialize(e.getBusinessDomain().getProfiles());
		}
		Hibernate.initialize(e.getProfile());
		Hibernate.initialize(e.getTansaction());
		Hibernate.initialize(e.getInterchangeAgreement());
		Hibernate.initialize(e.getParty());
	}

	private void populateCredentials(Credentials credentials, EntityAccessInfo eai) {
		credentials.setPasswordEncrypted(false);
		credentials.setSignatureRequired(false);
		credentials.setParty(null);
		credentials.setAccessInfo(eai);
	}

	private void deleteOrphanCredentials(Endpoint existingEndpoint, Credentials credentials, Boolean isDeletingEnpoint) {
		// Check if new credentials added or changed
		Credentials existingEndpointCredentials = credentials instanceof ProxyCredentials ? existingEndpoint.getProxyCredential() : existingEndpoint.getCredentials();

		if (credentials != null && existingEndpointCredentials != null && existingEndpointCredentials.getId() != null){
			Credentials existingCredentials = credentialsDAO.read(existingEndpointCredentials.getId());
			// Check if the existing Credentials are used by another endpoint
			if (isNotUsedByAnotherEndpoint(existingEndpoint, credentials, existingCredentials)) {
				if(credentials.getId() != null){
					if( isDeletingEnpoint) {
						credentialsDAO.delete(existingCredentials);
					}

					if( !credentials.getId().equals(existingEndpointCredentials.getId())){
						credentialsDAO.delete(existingCredentials);
					}
				} else {
					credentialsDAO.delete(existingCredentials);
				}
			}
		}
	}

	private boolean isNotUsedByAnotherEndpoint(Endpoint existingEndpoint, Credentials credentials, Credentials existingCredentials) {
		boolean canBeDeleted = false;
		// Check if the existing Credentials are used by another endpoint
		List<? extends Endpoint> endpoints = endpointDAO.findByCredentials(existingCredentials);
		if (endpoints.size() == 1) {
			canBeDeleted = true;
			if (credentials instanceof ProxyCredentials) {
				endpointDAO.read(existingEndpoint.getId()).setProxyCredential(null);
			} else {
				endpointDAO.read(existingEndpoint.getId()).setCredentials(null);
			}
		}

		return canBeDeleted;
	}

	private void decryptPasswords(Endpoint endpoint) throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
		if (endpoint.getCredentials() != null && endpoint.getCredentials().getPassword() != null && endpoint.getCredentials().getIv() != null){
			String uncryptedPwd = encryptionService.decryptMessageInAES(endpoint.getCredentials().getPassword(),  endpoint.getCredentials().getIv());
			endpoint.getCredentials().setDecryptedPassword(uncryptedPwd);
		}
		if (endpoint.getProxyCredential() != null && endpoint.getProxyCredential().getPassword() != null && endpoint.getProxyCredential().getIv() != null){
			String uncryptedPwd = encryptionService.decryptMessageInAES(endpoint.getProxyCredential().getPassword(),  endpoint.getProxyCredential().getIv());
			endpoint.getProxyCredential().setDecryptedPassword(uncryptedPwd);
		}
	}

	private void encryptPassword(Credentials cred) throws GeneralSecurityException, UnsupportedEncodingException {
		if(cred.getPassword() != null){
			byte[] iv = EncryptionService.generateIV();
			String password = encryptionService.encryptMessageInAES(cred.getPassword(), iv);
			cred.setIv(iv);
			cred.setPassword(password);
			cred.setPasswordEncrypted(true);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsEndpointForUsernameAndUrl(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endpointClass, String url, Long endpointId, Long newCredentialId) {
		return endpointDAO.existsEndpointForUsernameAndUrl(credentials, businessDomainId, endpointClass, url, endpointId, newCredentialId);
	}
}
