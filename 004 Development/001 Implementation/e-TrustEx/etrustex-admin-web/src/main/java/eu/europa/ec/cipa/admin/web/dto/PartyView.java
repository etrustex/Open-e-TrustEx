/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.Certificate;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyCredentials;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author batrian
 * 
 */
public class PartyView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Boolean isThirdParty;
	private Boolean naturalPersonFlag;
	private BusinessDomainElement businessDomain;
	private Boolean changePasswordFlag;
	private List<Certificate> certificates;
	private CredentialsDTO credentials;
	private List<PartyIdentifierDTO> identifiers;
	private String newIdentifierType;
	private String newIdentifierValue;
	
	private static final Logger logger = LoggerFactory.getLogger(PartyView.class.getName());
	
	public PartyView() {
	}

	public PartyView(Party p) {
		super();

		if (p != null) {
			logger.debug("Converting domain object to form object [domainObjectId = {}]...", p.getId());
			
//			this.eai = p.getAccessInfo();
			
			this.id = p.getId();
			this.name = p.getName();
			this.businessDomain = new BusinessDomainElement(p.getBusinessDomain());
			this.isThirdParty = p.getThirdPartyFlag() != null ? p.getThirdPartyFlag() : false;
			this.naturalPersonFlag = p.getNaturalPersonFlag() != null ? p.getNaturalPersonFlag() : false;

			if (CollectionUtils.isNotEmpty(p.getCertificates())){
				this.certificates = new ArrayList<Certificate>(p.getCertificates());
			}
			
			this.credentials = new CredentialsDTO(p.getCredentials());
			this.changePasswordFlag = false;
			
			if (CollectionUtils.isNotEmpty(p.getIdentifiers())){
				this.identifiers = new ArrayList<PartyIdentifierDTO>();  
				for (PartyIdentifier pi : p.getIdentifiers()) {
					this.identifiers.add(new PartyIdentifierDTO(pi));
				}
			}
		}
	}
	
	public Party toDomain() {
		Party p = new Party();
		
		/*if (this.eai != null){
			p.setAccessInfo(this.eai);
		} else {
			p.setAccessInfo(new EntityAccessInfo());
		}*/
		p.setId(this.id);
		p.setName(this.name);
		p.setThirdPartyFlag(this.isThirdParty);
		p.setNaturalPersonFlag(this.naturalPersonFlag);

		if (this.businessDomain != null){
			BusinessDomain bd = new BusinessDomain();
			bd.setId(this.businessDomain.getId());
//			bd.setName(this.businessDomain.getName());
			p.setBusinessDomain(bd);
		}
		
		if(this.certificates != null) {
			p.setCertificates(new HashSet<Certificate>(this.certificates));
		}
		
		if (this.credentials != null){
			p.setCredentials((PartyCredentials)this.credentials.toDomain(CredentialsDTO.CredentialsType.PARTY));
		}
		
		if (CollectionUtils.isNotEmpty(this.identifiers)){
			for (PartyIdentifierDTO partyIdentifier : this.identifiers) {
				if (!partyIdentifier.getIsMarkedForDeletion()){
					p.addIdentifier(partyIdentifier.toDomain());
				}
			}
		}
		
		return p;
	}

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessDomainElement getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomainElement businessDomain) {
		this.businessDomain = businessDomain;
	}

	public Boolean getIsThirdParty() {
		return isThirdParty;
	}

	public void setIsThirdParty(Boolean isThirdParty) {
		this.isThirdParty = isThirdParty;
	}

	public Boolean getNaturalPersonFlag() {
		return naturalPersonFlag;
	}

	public void setNaturalPersonFlag(Boolean naturalPersonFlag) {
		this.naturalPersonFlag = naturalPersonFlag;
	}

	public CredentialsDTO getCredentials() {
		return credentials;
	}

	public void setCredentials(CredentialsDTO credentials) {
		this.credentials = credentials;
	}

	public List<PartyIdentifierDTO> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<PartyIdentifierDTO> identifiers) {
		this.identifiers = identifiers;
	}

	public String getNewIdentifierType() {
		return newIdentifierType;
	}

	public void setNewIdentifierType(String newIdentifierType) {
		this.newIdentifierType = newIdentifierType;
	}

	public String getNewIdentifierValue() {
		return newIdentifierValue;
	}

	public void setNewIdentifierValue(String newIdentifierValue) {
		this.newIdentifierValue = newIdentifierValue;
	}

	public Boolean getChangePasswordFlag() {
		return changePasswordFlag;
	}

	public void setChangePasswordFlag(Boolean changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}
}
