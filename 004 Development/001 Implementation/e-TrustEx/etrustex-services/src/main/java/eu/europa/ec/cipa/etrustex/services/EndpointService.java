package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.dao.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


	protected static final Logger logger = LoggerFactory.getLogger(EndpointService.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Endpoint findById(Long id) {
		Endpoint e = endpointDAO.read(id);
		initialize(e);
		
		return e;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Endpoint> retrieveEndpoints(Long icaId, Long partyId,
			Long profileId, Long transactionId) {
		
		List<Endpoint> endpoints = new ArrayList<>();
				
		if (partyId !=null && icaId != null){
			endpoints.addAll(endpointDAO.getInterchangeAgreementEndpoints(icaId, partyId));
		}
		
		if (partyId != null && transactionId != null){
			endpoints.addAll(endpointDAO.getTransactionEndpoints(transactionId, partyId));
			//ETRUSTEX-1363 search for endpoints configured on 3rd parties and transactions
			endpoints.addAll(endpointDAO.getThirdPartyEndpointsForTransaction(partyId, transactionId));
		} 
		
		if (partyId != null && profileId != null){
			endpoints.addAll(endpointDAO.getProfileEndpoints(profileId, partyId));
		} 
		if (partyId!=null && profileId !=null && transactionId != null){
			List<Endpoint> tempEndpoints = endpointDAO.getThirdPartyEndpoints(profileId, partyId); 
			//filter with agreement transactions.
			if (tempEndpoints!=null){
				logger.info("Found " + tempEndpoints.size() + " endpoints for third parties");
				for(Endpoint ep : tempEndpoints){
					Party party = new Party();
					party.setId(partyId);				
					PartyAgreement pa = partyAgreementDAO.retrievePartyAgreement(party, ep.getParty());
					logger.info("Agreement "+pa.getId());
					if (pa.getTransactions() == null || pa.getTransactions().size() == 0){
						logger.info("add endpoint "+ep.getId());
						endpoints.add(ep);//if no transaction present it means valid for all transactions.
					}else{
						//if some transactions present it means valid only for these transactions
						for(Transaction t : pa.getTransactions()){
							if (t.getId().equals( transactionId)){
								logger.info("add endpoint "+ep.getId());
								endpoints.add(ep);//valid for current transaction
								break;
							}						
						}
					}
				}
			}else{
				logger.info("Found no endpoint for third parties");
			}			
		}
		return endpoints; 
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Endpoint create(Endpoint endpoint, String userId) {
		mergeProperties(endpoint, userId);
		
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
        deleteOrphanCredentials(existingEndpoint, endpoint.getCredentials());
        deleteOrphanCredentials(existingEndpoint, endpoint.getProxyCredential());

		mergeProperties(endpoint, userId);
		
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
	    deleteOrphanCredentials(endpoint, endpoint.getProxyCredential());
        deleteOrphanCredentials(endpoint, endpoint.getCredentials());

        endpointDAO.delete(endpoint);

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
    public List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass) {
        return credentials == null ? null : endpointDAO.findByCredentialsTypeAndUsernameLike(credentials, businessDomainId, endppointClass);
    }

    @Override
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
	
	private void mergeProperties(Endpoint endpoint, String userId) {
        EntityAccessInfo eai = new EntityAccessInfo();

		if(endpoint.getBusinessDomain() != null && endpoint.getBusinessDomain().getId() != null && !endpoint.getBusinessDomain().getId().equals(new Long(-1))) {
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
                    endpoint.setCredentials(credentialsDAO.create(credentials));
                } else {
                    endpoint.setCredentials(null);
                }
            } else {
                // Credentials reused
                endpoint.setCredentials(credentialsDAO.read(endpoint.getCredentials().getId()));
                boolean changed = false;

                if(!StringUtils.equals(endpoint.getCredentials().getUser(), username)) {
                    // user changed
                    endpoint.getCredentials().setUser(username);
                    changed = true;
                }

                if(!StringUtils.equals(endpoint.getCredentials().getPassword(), password)) {
                    // password changed
                    endpoint.getCredentials().setPassword(password);
                    changed = true;
                }

                if(changed) {
                    endpoint.getCredentials().getAccessInfo().setModificationId(userId);
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
                    endpoint.setProxyCredential(credentialsDAO.create(credentials));
                } else {
                    endpoint.setProxyCredential(null);
                }
            } else {
                // ProxyCredential reused
                endpoint.setProxyCredential(credentialsDAO.read(endpoint.getProxyCredential().getId()));
                boolean changed = false;

                if(!StringUtils.equals(endpoint.getProxyCredential().getUser(), proxyUsername)) {
                    // user changed
                    endpoint.getProxyCredential().setUser(proxyUsername);
                    changed = true;
                }

                if(!StringUtils.equals(endpoint.getProxyCredential().getPassword(), proxyPassword)) {
                    // password changed
                    endpoint.getProxyCredential().setPassword(proxyPassword);
                    changed = true;
                }

                if(changed) {
                    endpoint.getProxyCredential().getAccessInfo().setModificationId(userId);
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

	private void deleteOrphanCredentials(Endpoint existingEndpoint, Credentials credentials) {
        // Check if new credentials added or changed
        Credentials existingEndpointCredentials = credentials instanceof ProxyCredentials ? existingEndpoint.getProxyCredential() : existingEndpoint.getCredentials();

        if (credentials != null
                && (credentials.getId() == null || Objects.equals(credentials, existingEndpointCredentials))) {

            // Check if the endpoint already had credentials
            if (existingEndpointCredentials != null && existingEndpointCredentials.getId() != null) {
                Credentials existingCredentials = credentialsDAO.read(existingEndpointCredentials.getId());

                // Check if the existing Credentials are used by another endpoint
                List<? extends Endpoint> endpoints = endpointDAO.findByCredentials(existingCredentials);
                if (endpoints.size() == 1) {
                    // Delete credentials
                    if (credentials instanceof ProxyCredentials) {
                        existingEndpoint.setProxyCredential(null);
                    } else {
                        existingEndpoint.setCredentials(null);
                    }

                    credentialsDAO.delete(existingCredentials);
                }
            }
        }
    }
}
