/**
 * 
 */
package eu.europa.ec.cipa.admin.web.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.cipa.admin.web.dto.PartyIdentifierDTO;
import eu.europa.ec.cipa.etrustex.domain.Certificate;
import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.PartyCredentials;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.EventNotification;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.util.IMessageSenderService;
import eu.europa.ec.cipa.etrustex.services.IEventNotificationService;
import eu.europa.ec.cipa.etrustex.services.dao.IBusinessDomainDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ICertificateDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IEndpointDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IEventNotificationDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IMetadataDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyRoleDao;
import eu.europa.ec.cipa.etrustex.services.dao.admin.IUserAccessRightsDAO;
import eu.europa.ec.cipa.etrustex.services.exception.RecordNotFoundException;
import eu.europa.ec.cipa.etrustex.services.util.EncryptionService;
import eu.europa.ec.cipa.etrustex.types.EventNotificationType;

/**
 * @author guerrpa
 *
 */
@Service
public class PartyNotificationService implements IPartyNotificationService {

	@Autowired private IPartyDAO partyDAO;
    @Autowired private IPartyRoleDao partyRoleDao;
    @Autowired private IPartyAgreementDAO partyAgreementDAO;
    @Autowired private IBusinessDomainDAO businessDomainDAO;
	@Autowired private ICertificateDAO certificateDAO;
	@Autowired private EncryptionService encryptionService;
	@Autowired 
	@Qualifier("jmsSenderService")
	private IMessageSenderService jmsSenderService;
	@Autowired private IEventNotificationService eventNotificationService;
    @Autowired private IEventNotificationDAO eventNotificationDAO;
    @Autowired private IUserAccessRightsDAO userAccessRightsDAO;
    @Autowired private IMetadataDAO metadataDAO;
    @Autowired private IEndpointDAO endpointDAO;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Party createAndSendNotification(Party party, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception {
		String currentUser = party.getAccessInfo().getCreationId();
		Date currentDate =  Calendar.getInstance().getTime();
		
		EntityAccessInfo creationEai = new EntityAccessInfo();
		creationEai.setCreationDate(currentDate);
		creationEai.setCreationId(currentUser);
		creationEai.setModificationDate(null);
		
		party.setAccessInfo(creationEai);
		party.setName(party.getName().trim());
		
		if (party.getBusinessDomain() != null && party.getBusinessDomain().getId() != null){
			party.setBusinessDomain(businessDomainDAO.read(party.getBusinessDomain().getId()));
		}
		
		//TODO check if it should be trimmed
		Set<Certificate> certs = party.getCertificates();
		if(certs != null) {
			for (Certificate certificate : certs) {
				if (certificate != null){
					trimCertificateData(certificate);
					certificate.setAccessInfo(creationEai);
					certificate.setParty(party);
				}
			}
		}

		Credentials credentials = party.getCredentials();
		if (credentials != null){
			credentials.setAccessInfo(creationEai);
			credentials.setUser(credentials.getUser().trim());
			if (StringUtils.isNotEmpty(credentials.getPassword())){
				if (credentials.getPasswordEncrypted()){
					credentials.setPassword(encryptionService.encryptPassword(credentials.getPassword()));
				} 
			}
		}
		
		//link the created party to the party identifiers and create them
		for(PartyIdentifier pi: party.getIdentifiers()) {
			pi.setAccessInfo(creationEai);
			pi.setValue(pi.getValue().trim());
		}

		//create the party
		Party createdParty = partyDAO.create(party);


		/* Submit event notification */
		EventType eventType = eventNotificationService.getCreatePartyEventNotification(createdParty);
		submitNotifications(businessDomainId, eventType);

		return createdParty;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Party updateAndSendNotification(Party party, List<Long> partyIdentifiersToDelete, Long businessDomainId, PartyIdentifierDTO partyIdentifier) throws Exception {
		logger.debug("updateParty({})", party);

		Party existingParty = partyDAO.read(party.getId());

		// Get a copy of existing party for event notification (SerializationUtils.clone() throws ClassNotFoundException in weblogic)
		Party fromParty = partyDAO.read(party.getId());

		if (existingParty == null){
			throw new RecordNotFoundException("Party", party.getId());
		}

		String currentUser = party.getAccessInfo().getModificationId();
		Date currentDate =  Calendar.getInstance().getTime();
		
		EntityAccessInfo creationEai = new EntityAccessInfo();
		creationEai.setCreationDate(currentDate);
		creationEai.setCreationId(currentUser);
		
		if (party.getCredentials() != null && party.getCredentials().getId() != null){
			party.getCredentials().getAccessInfo().setModificationId(currentUser);
			party.getCredentials().getAccessInfo().setModificationDate(currentDate);
		}

		existingParty.getAccessInfo().setModificationId(currentUser);
		existingParty.getAccessInfo().setModificationDate(currentDate);
//		existingParty.setName(party.getName()); //read-only
		existingParty.setThirdPartyFlag(party.getThirdPartyFlag());
		existingParty.setNaturalPersonFlag(party.getNaturalPersonFlag());

		Set<Certificate> certs = party.getCertificates();
		
		// Check if certificates have been removed...
		for (Iterator<Certificate> iterator = existingParty.getCertificates().iterator(); iterator.hasNext();) {
			Certificate cert = iterator.next();
			if(!certs.contains(cert)) {
				// ...and remove them from persitent parent entity
				iterator.remove();
			}
    	}
		
		for (Certificate cert : certs) {
			trimCertificateData(cert);
			
			if(cert.getId() != null) {
				// updated existing certificates whether or not they have been modified. TODO Is that OK?
				cert.getAccessInfo().setModificationDate(currentDate);
				cert.getAccessInfo().setModificationId(currentUser);
				existingParty.getCertificates().add(certificateDAO.update(cert));
			} else {
				cert.setAccessInfo(creationEai);
				cert.setParty(party);
				existingParty.getCertificates().add(cert);
			}
		}
		

		Credentials cred = existingParty.getCredentials();
		Credentials newCred = party.getCredentials();
		if (newCred != null){
			if (cred != null && cred.getId() != null){
				cred.getAccessInfo().setModificationId(currentUser);
				cred.getAccessInfo().setModificationDate(currentDate);
				
				if (StringUtils.isNotEmpty(newCred.getPassword())){
					if (newCred.getPasswordEncrypted()){
						cred.setPassword(encryptionService.encryptPassword(newCred.getPassword()));
					} else {
						cred.setPassword(newCred.getPassword());
					}
				}
					
				cred.setPasswordEncrypted(newCred.getPasswordEncrypted());
				cred.setSignatureRequired(newCred.getSignatureRequired());
			} else {
				newCred.setAccessInfo(creationEai);
				if (newCred.getPasswordEncrypted()){
					newCred.setPassword(encryptionService.encryptPassword(newCred.getPassword()));
				}
				existingParty.setCredentials((PartyCredentials)newCred);
			}
		}
		
		
		Set<PartyIdentifier> existingPids = existingParty.getIdentifiers();
		Set<PartyIdentifier> pids = party.getIdentifiers();
		
		Map<Long, PartyIdentifier> partyIdbyId = new HashMap<>();
		for (PartyIdentifier id : existingPids){			
			partyIdbyId.put(id.getId(), id);
		}
		
		if (CollectionUtils.isEmpty(existingPids)){
			for (PartyIdentifier pid : pids) {
				pid.setAccessInfo(creationEai);
			}
			
			existingParty.setIdentifiers(pids);
		} else {
			
			if (partyIdentifiersToDelete == null){
				partyIdentifiersToDelete = new ArrayList<Long>(0);
			}
			
			//force delete first
			for (Long id : partyIdbyId.keySet()){
				
				if (CollectionUtils.isEmpty(partyIdentifiersToDelete)){
					break;
				}
				
				PartyIdentifier existingPid = partyIdbyId.get(id);
				
				if (partyIdentifiersToDelete.contains(id)){ //delete existing party identifier

					existingParty.getIdentifiers().remove(existingPid);	
					
				} 
				
			}
			partyDAO.flushEm();
			
			//force update second
			for (Long id : partyIdbyId.keySet()){//update existing party identifier
				PartyIdentifier existingPid = partyIdbyId.get(id);
				
				for (PartyIdentifier pid : pids) {
					if (pid != null && pid.getId() != null){
						if (existingPid.getId().longValue() == pid.getId().longValue()){
							existingPid.getAccessInfo().setModificationId(currentUser);
							existingPid.getAccessInfo().setModificationDate(currentDate);
							
							existingPid.setSchemeId(pid.getSchemeId());
							existingPid.setValue(pid.getValue());
						}
					}
				}	
			}
			partyDAO.flushEm();
			
			//insertion last
			for (PartyIdentifier pid : pids) {
				if (pid != null && pid.getId() == null){
					pid.setAccessInfo(creationEai);
					pid.setParty(existingParty);
					
					existingParty.getIdentifiers().add(pid);
				}
			}

			
		}
		
		//update the party
		Party updatedParty = partyDAO.update(existingParty);


		/* Submit event notification */
		EventType eventType = eventNotificationService.getUpdatePartyEventNotification(fromParty, updatedParty);
		submitNotifications(businessDomainId, eventType);


		//return the complete object
		return updatedParty;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteAndSendNotification(Long partyId, Long businessDomainId) throws Exception {
		Party party = partyDAO.read(partyId);

        /*
         * ETRUSTEX-1559  Admin Console - party delete fails
         * Delete any associated PartyRoles
         * Delete any associated PartyAgreement (and transactions via ETR_TB_PARTYAGREEMENT_TRAN)
         * Delete any associated EventNotification
         * Delete any associated Metadata
         * Delete any associated Endpoint
         * Delete any associated UserAccessRights
         */
        for(PartyRole pr : partyRoleDao.getPartyRoles(partyId)) {
            partyRoleDao.delete(pr);
        }

        for(PartyAgreement pa : partyAgreementDAO.findPartyAgreementsForParty(party)) {
            partyAgreementDAO.delete(pa);
        }

        for(EventNotification en : eventNotificationDAO.findByParty(party)) {
            eventNotificationDAO.delete(en);
        }

        MetaDataItem metaDataItem = new MetaDataItem();
        metaDataItem.setSender(party);
        for(MetaDataItem mdi : metadataDAO.getMetaDataItemsByCriteria(metaDataItem)) {
            metadataDAO.delete(mdi);
        }

        for(Endpoint e : endpointDAO.findEndpointsByParty(party)) {
            endpointDAO.delete(e);
        }

        for(UserAccessRights uar : userAccessRightsDAO.findByPartyId(partyId)) {
            userAccessRightsDAO.delete(uar);
        }

		partyDAO.delete(party);

		/* Submit event notification */
		EventType eventType = eventNotificationService.getDeletePartyEventNotification(party);
		submitNotifications(businessDomainId, eventType);

		return true;
	}
	

	private void trimCertificateData(Certificate cert){
		if (cert == null){
			return;
		}
		
		cert.setSerialNumber(cert.getSerialNumber() == null ? null : cert.getSerialNumber().trim());
		cert.setType(cert.getType() == null ? null : cert.getType().trim());
		cert.setUsage(cert.getUsage() == null ? null : cert.getUsage().trim());
		
		cert.setIssuer(cert.getIssuer() == null ? null : cert.getIssuer().trim());
		cert.setHolder(cert.getHolder() == null ? null : cert.getHolder().trim());
		
		cert.setEncodedData(cert.getEncodedData() == null ? null : cert.getEncodedData().trim());
		cert.setAttributes(cert.getAttributes() == null ? null : cert.getAttributes().trim());
		cert.setVersion(cert.getVersion() == null ? null : cert.getVersion().trim());
		
		cert.setSignatureAlgorithm(cert.getSignatureAlgorithm() == null ? null : cert.getSignatureAlgorithm().trim());
		cert.setSignatureValue(cert.getSignatureValue() == null ? null : cert.getSignatureValue().trim());
		
	}

	private void submitNotifications(Long businessDomainId, EventType eventType) throws Exception {
		// Get parties configured to receive notifications
		List<Party> configuredParties = partyDAO.getPartiesConfiguredToReceiveEventNotification(EventNotificationType.CFG, businessDomainId, null);

		for(Party p : configuredParties) {
			PartyIdentifier receiverId = p.getIdentifiers().iterator().next();
			PartyIdentifier senderId = eventNotificationService.getSenderId(p);
			jmsSenderService.sendEventNotification(eventType, receiverId, senderId);
		}
	}

}
