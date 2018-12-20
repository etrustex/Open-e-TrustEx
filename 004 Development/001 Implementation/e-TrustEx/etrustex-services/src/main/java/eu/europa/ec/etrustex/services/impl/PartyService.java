/**
 * 
 */
package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.ICertificateDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.util.EncryptionService;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author batrian
 *
 */
@Service("partyService")
public class PartyService implements IPartyService {

	@Autowired
	private IPartyDAO partyDAO;

	@Autowired
	private IBusinessDomainDAO businessDomainDAO;

	@Autowired
	private ICertificateDAO certificateDAO;

	@Autowired
	private IPartyAgreementDAO partyAgreementDAO;

	private static final Logger logger = LoggerFactory.getLogger(PartyService.class);

	@Autowired
	private EncryptionService encryptionService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Party createParty(Party party) throws NoSuchAlgorithmException {

		String currentUser = party.getAccessInfo().getCreationId();
		Date currentDate = Calendar.getInstance().getTime();

		EntityAccessInfo creationEai = new EntityAccessInfo();
		creationEai.setCreationDate(currentDate);
		creationEai.setCreationId(currentUser);
		creationEai.setModificationDate(null);

		party.setAccessInfo(creationEai);
		party.setName(party.getName().trim());

		if (party.getBusinessDomain() != null && party.getBusinessDomain().getId() != null) {
			party.setBusinessDomain(businessDomainDAO.read(party.getBusinessDomain().getId()));
		}

		// TODO check if it should be trimmed
		Set<Certificate> certs = party.getCertificates();
		if (certs != null) {
			for (Certificate certificate : certs) {
				if (certificate != null) {
					trimCertificateData(certificate);
					certificate.setAccessInfo(creationEai);
					certificate.setParty(party);
				}
			}
		}

		Credentials credentials = party.getCredentials();
		if (credentials != null) {
			credentials.setAccessInfo(creationEai);
			credentials.setUser(credentials.getUser().trim());
			if (StringUtils.isNotEmpty(credentials.getPassword())) {
				if (credentials.getPasswordEncrypted()) {
					credentials.setSalt(true);
					credentials.setPassword(encryptionService.encryptPasswordByBCrypt(credentials.getPassword()));
				}
			}
		}

		// link the created party to the party identifiers and create them
		for (PartyIdentifier pi : party.getIdentifiers()) {
			pi.setAccessInfo(creationEai);
			pi.setValue(pi.getValue().trim());
		}

		// create the party
		Party createdParty = partyDAO.create(party);

		return createdParty;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Party updateParty(Party party, List<Long> partyIdentifiersToDelete) throws NoSuchAlgorithmException {

		logger.debug("updateParty({})", party);

		Party existingParty = getParty(party.getId());

		if (existingParty == null) {
			throw new RecordNotFoundException("Party", party.getId());
		}

		String currentUser = party.getAccessInfo().getModificationId();
		Date currentDate = Calendar.getInstance().getTime();

		EntityAccessInfo creationEai = new EntityAccessInfo();
		creationEai.setCreationDate(currentDate);
		creationEai.setCreationId(currentUser);

		if (party.getCredentials() != null && party.getCredentials().getId() != null) {
			party.getCredentials().getAccessInfo().setModificationId(currentUser);
			party.getCredentials().getAccessInfo().setModificationDate(currentDate);
		}

		existingParty.getAccessInfo().setModificationId(currentUser);
		existingParty.getAccessInfo().setModificationDate(currentDate);
		// existingParty.setName(party.getName()); //read-only
		existingParty.setThirdPartyFlag(party.getThirdPartyFlag());
		existingParty.setNaturalPersonFlag(party.getNaturalPersonFlag());

		Set<Certificate> certs = party.getCertificates();

		// Check if certificates have been removed...
		for (Iterator<Certificate> iterator = existingParty.getCertificates().iterator(); iterator.hasNext();) {
			Certificate cert = iterator.next();
			if (!certs.contains(cert)) {
				// ...and remove them from persitent parent entity
				iterator.remove();
			}
		}

		for (Certificate cert : certs) {
			trimCertificateData(cert);

			if (cert.getId() != null) {
				// updated existing certificates whether or not they have been
				// modified. TODO Is that OK?
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
		if (newCred != null) {
			if (cred != null && cred.getId() != null) {
				cred.getAccessInfo().setModificationId(currentUser);
				cred.getAccessInfo().setModificationDate(currentDate);

				if (StringUtils.isNotEmpty(newCred.getPassword())) {
					if (newCred.getPasswordEncrypted()) {
						cred.setPassword(encryptionService.encryptPasswordByBCrypt(newCred.getPassword()));
					} else {
						cred.setPassword(newCred.getPassword());
					}
				}

				cred.setPasswordEncrypted(newCred.getPasswordEncrypted());
				cred.setSignatureRequired(newCred.getSignatureRequired());
			} else {
				newCred.setAccessInfo(creationEai);
				if (newCred.getPasswordEncrypted()) {
					newCred.setPassword(encryptionService.encryptPasswordByBCrypt(newCred.getPassword()));
				}
				existingParty.setCredentials(newCred);
			}
		}

		Set<PartyIdentifier> existingPids = existingParty.getIdentifiers();
		Set<PartyIdentifier> pids = party.getIdentifiers();

		Map<Long, PartyIdentifier> partyIdbyId = new HashMap<Long, PartyIdentifier>();
		for (PartyIdentifier id : existingPids) {
			partyIdbyId.put(id.getId(), id);
		}

		if (CollectionUtils.isEmpty(existingPids)) {
			for (PartyIdentifier pid : pids) {
				pid.setAccessInfo(creationEai);
			}

			existingParty.setIdentifiers(pids);
		} else {

			if (partyIdentifiersToDelete == null) {
				partyIdentifiersToDelete = new ArrayList<Long>(0);
			}

			// force delete first
			for (Long id : partyIdbyId.keySet()) {

				if (CollectionUtils.isEmpty(partyIdentifiersToDelete)) {
					break;
				}

				PartyIdentifier existingPid = partyIdbyId.get(id);

				if (partyIdentifiersToDelete.contains(id)) { // delete existing
																// party
																// identifier

					existingParty.getIdentifiers().remove(existingPid);

				}

			}
			partyDAO.flushEm();

			// force update second
			for (Long id : partyIdbyId.keySet()) {// update existing party
													// identifier
				PartyIdentifier existingPid = partyIdbyId.get(id);

				for (PartyIdentifier pid : pids) {
					if (pid != null && pid.getId() != null) {
						if (existingPid.getId().longValue() == pid.getId().longValue()) {
							existingPid.getAccessInfo().setModificationId(currentUser);
							existingPid.getAccessInfo().setModificationDate(currentDate);

							existingPid.setSchemeId(pid.getSchemeId());
							existingPid.setValue(pid.getValue());

							// partyIdentifierDAO.update(existingPid);
						}
					}
				}
			}
			partyDAO.flushEm();

			// insertion last
			for (PartyIdentifier pid : pids) {
				if (pid != null && pid.getId() == null) {
					pid.setAccessInfo(creationEai);
					pid.setParty(existingParty);

					existingParty.getIdentifiers().add(pid);
				}
			}

		}

		// update the party
		Party updatedParty = partyDAO.update(existingParty);

		// return the complete object
		return updatedParty;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteById(Long partyId) {
		Party party = getParty(partyId);
		partyDAO.delete(party);
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<PartyListItemDTO> getAllPartiesForBusinessDomain(Long businessDomainId) {
		return partyDAO.getAllPartiesForBusinessDomain(businessDomainId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Party> getAllThirdPartiesForBusinessDomain(Long businessDomainId) {
		return partyDAO.getThirdPartiesForBusinessDomain(businessDomainId);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getParty(Long id) {
		if (id == null) {
			//chiricr: passing a null id will throw IllegalArgumentException: id to load is required for loading
			return null;
		}
		Party party = partyDAO.read(id);

		// needed due to lazy loading
		if (party != null) {
			Hibernate.initialize(party.getCertificates());
			Hibernate.initialize(party.getCredentials());
			Hibernate.initialize(party.getBusinessDomain());
		}

		return party;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Boolean isUniquePartyNamePerDomain(String partyName, Long businessDomainId) {

		return partyDAO.isUniquePartyPerBusinessDomain(partyName == null ? partyName : partyName.trim(), businessDomainId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getPartyByID(IdentifierIssuingAgency schemeId, String idValue, BusinessDomain businessDomain) {
		if (idValue == null) {
			return null;
		}
		if (schemeId == null) {
			schemeId = IdentifierIssuingAgency.GLN;
		}
		return partyDAO.getParty(schemeId, idValue, businessDomain);
		
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Party> findPartiesByCriteria(String name, String username, String identifierValue, List<Long> businessDomainIds, Boolean isThirdParty) {

        List<Party> result = partyDAO.findPartiesByCriteria(name, username, identifierValue, businessDomainIds, isThirdParty);

        // needed due to lazy loading
        for (Party party : result) {
            Hibernate.initialize(party.getCredentials());
            Hibernate.initialize(party.getBusinessDomain());
            Hibernate.initialize(party.getIdentifiers());
        }

        return result;
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Party> getAllThirdPartiesFor(Long partyId) {
		return partyAgreementDAO.getDelegateThirdPartiesFor(partyId);
	}

	private void trimCertificateData(Certificate cert) {
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Party getPartyForBusinessDomain(String partyName, Long businessDomainId) {
		return partyDAO.getPartyForBusinessDomain(partyName, businessDomainId);
	}
}
