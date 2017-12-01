package eu.europa.ec.cipa.etrustex.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

@Entity
@Table(name="ETR_TB_PARTY",
	uniqueConstraints={ @UniqueConstraint(columnNames={"PTY_PARTY_NAME", "PTY_BUSINESS_DOMAIN"})})
public class Party implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="PTY_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name="PTY_PARTY_NAME",nullable=false ,unique=true)
	private String name;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL , fetch = FetchType.EAGER, orphanRemoval=true)
	private Set<PartyIdentifier> identifiers= new HashSet<PartyIdentifier>();
	
	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="PTY_CRED_ID", unique=true, nullable=true,updatable=true)
	private PartyCredentials credentials;
	
	
	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL , fetch = FetchType.EAGER, orphanRemoval=true)
	private Set<Certificate> certificates = new HashSet<Certificate>();
	
	@Column(name="PTY_THIRD_PTY_FL")
	private Boolean thirdPartyFlag;
	
	@Column(name="PTY_NATURAL_PERS_FL")
	private Boolean naturalPersonFlag;
	
	@ManyToOne(fetch=FetchType.EAGER/*, cascade = CascadeType.MERGE*/)
	@JoinColumn(name="PTY_BUSINESS_DOMAIN", nullable=true, updatable=true)
	private BusinessDomain businessDomain;

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

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public Set<PartyIdentifier> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(Set<PartyIdentifier> identifiers) {
		this.identifiers = identifiers;
	}

	public PartyCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(PartyCredentials credentials) {
		this.credentials = credentials;
	}

	public Boolean getThirdPartyFlag() {
		return thirdPartyFlag;
	}

	public void setThirdPartyFlag(Boolean thirdPartyFlag) {
		this.thirdPartyFlag = thirdPartyFlag;
	}
	
	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}

	/**
	 * Adds new party identifier to the party. It is used for keeping relationships consistency.  
	 * @param partyIdentifier
	 */
    public void addIdentifier(PartyIdentifier partyIdentifier) {
    	if (this.identifiers == null){
    		this.identifiers = new HashSet<PartyIdentifier>();
    	}
    	
    	if (this.identifiers.contains(partyIdentifier)){
    		return;
    	}
    	
        this.identifiers.add(partyIdentifier);
        if (partyIdentifier.getParty() != this) {
        	partyIdentifier.setParty(this);
        }
    }
    
    /**
	 * Removes a party identifier from the party. It is used for keeping relationships consistency.  
	 * @param partyIdentifier
	 */
    public void removeIdentifier(PartyIdentifier partyIdentifier) {
    	if (!this.identifiers.contains(partyIdentifier)){
    		return;
    	}
    	
        this.identifiers.remove(partyIdentifier);
        partyIdentifier.setParty(null);
    }
	
	@Override
	public String toString() {
		return "Party [id=" + id 
				+ ", name=" + name 
				+ ", thirdPartyFlag=" + thirdPartyFlag
				+ ", businessDomain=" + getBusinessDomainName() + "]";
	}
	
	@Override
    public boolean equals(final Object obj) {
		
		if ((obj == null) || !(obj instanceof Party)) {
            return false;
        }
        
        if (obj == this){
        	return true;
        }
        
        final Party otherParty = (Party) obj;
        
        return new EqualsBuilder().append(getName(), otherParty.getName())
        		.append(getBusinessDomainName(), getBusinessDomainName(otherParty.getBusinessDomain()))
        		.isEquals();
         
    }
	
	@Override
    public int hashCode() {
		
		return new HashCodeBuilder().append(getName())
        		.append(getBusinessDomainName())
        		.toHashCode();
        
    }
	
	public boolean equalsById(final Object obj) {
        if ((obj == null) || !(obj instanceof Party)) {
            return false;
        }
		
        final Party other = (Party) obj;
        return new EqualsBuilder().append(getId(), other.getId())
        		.isEquals();
	}
	
	
	private String getBusinessDomainName(){
		
		return getBusinessDomainName(getBusinessDomain());
		
	}
	
	private String getBusinessDomainName(BusinessDomain bd){
		
		if (bd == null || bd.getName() == null){ 
			return null;
		} else {
			return bd.getName();
		}
		
	}

	public Set<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(Set<Certificate> certificates) {
		this.certificates = certificates;
	}

	public Boolean getNaturalPersonFlag() {
		return naturalPersonFlag;
	}

	public void setNaturalPersonFlag(Boolean naturalPersonFlag) {
		this.naturalPersonFlag = naturalPersonFlag;
	}
}
