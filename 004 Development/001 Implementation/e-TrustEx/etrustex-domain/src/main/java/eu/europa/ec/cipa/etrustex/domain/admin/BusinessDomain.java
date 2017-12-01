package eu.europa.ec.cipa.etrustex.domain.admin;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="CPA_TB_BUSINESS_DOMAIN")
public class BusinessDomain implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CBD_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@Column(name="CBD_RESP_SIG_REQUIRED", nullable=false)
	private Boolean responseSignatureRequired; 
	
	@Column(name ="CBD_NAME", nullable = false,unique=true)
	private String name;
	
	/*@OneToMany(mappedBy = "businessDomain", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private Set<Profile> profiles= new HashSet<Profile>();*/
	
	@OneToMany(mappedBy = "businessDomain", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private Set<Party> parties = new HashSet<Party>();
	
	@ManyToMany
	@JoinTable(name = "ETR_TB_PROFILE_BUSINESS_DOMAIN", 
		joinColumns = { @JoinColumn(name = "PBD_BD_ID", referencedColumnName = "CBD_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "PBD_PRO_ID", referencedColumnName = "PRO_ID") })
	private Set<Profile> profiles = new HashSet<Profile>();
	
	public static enum BD_NAME {eProcurement, PDA, DGJustice, Generic}
	
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

	public Set<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	public Set<Party> getParty() {
		return parties;
	}

	public void setParty(Set<Party> parties) {
		this.parties = parties;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	@Override
	public String toString() {
		return "BusinessDomain [id=" + id + ", name=" + name + ", numberOfProfiles="
				+ ((profiles != null && !profiles.isEmpty()) ? profiles.size() : 0) + "]";
	}

	@Override
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof BusinessDomain)) {
            return false;
        }
        
        final BusinessDomain otherBusinessDomain = (BusinessDomain) obj;
        return new EqualsBuilder().append(getId(), otherBusinessDomain.getId())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId())
        		.toHashCode();
    }
	
/*	public boolean equalsById(final Object obj) {
        if ((obj == null) || !(obj instanceof BusinessDomain)) {
            return false;
        }
		
        final BusinessDomain other = (BusinessDomain) obj;
        return new EqualsBuilder().append(getId(), other.getId())
        		.isEquals();
	}*/

	public Boolean getResponseSignatureRequired() {
		return responseSignatureRequired;
	}

	public void setResponseSignatureRequired(Boolean responseSignatureRequired) {
		this.responseSignatureRequired = responseSignatureRequired;
	}

}
