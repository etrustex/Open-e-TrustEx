package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ETR_TB_PROFILE")
public class Profile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PRO_ID", nullable = false)
	@GeneratedValue
	private Long id;
	@Column(name = "PRO_NAME", nullable = false)
	private String name;
	
	@Column(name = "PRO_NAMESPACE", nullable = true)
	private String namespace;
	

	@Embedded
	private EntityAccessInfo accessInfo;
	
	@ManyToMany
	@JoinTable(name = "ETR_TB_PROFILE_TRANSACTION", 
		joinColumns = { @JoinColumn(name = "PTR_PRO_ID", referencedColumnName = "PRO_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "PTR_TRA_ID", referencedColumnName = "TRA_ID") })
	private Set<Transaction> transactions = new HashSet<Transaction>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRO_CIA_ID", nullable = true)
	private CIALevel ciaLevel;
	
	@ManyToMany(mappedBy="profiles" ,fetch=FetchType.EAGER)
	private Set<BusinessDomain> businessDomains = new HashSet<BusinessDomain>();

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

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public CIALevel getCiaLevel() {
		return ciaLevel;
	}

	public void setCiaLevel(CIALevel ciaLevel) {
		this.ciaLevel = ciaLevel;
	}
	
	public Set<BusinessDomain> getBusinessDomains() {
		return businessDomains;
	}

	public void setBusinessDomains(Set<BusinessDomain> businessDomains) {
		this.businessDomains = businessDomains;
	}

	
	@Override
	public String toString() {
		return "Profile(ID [" + getId().toString() + "] name[" + getName() + "])";
	}
	
	@Override
    public boolean equals(final Object obj) {
        
		if ((obj == null) || !(obj instanceof Profile)) {
            return false;
        }
        
        final Profile otherProfile = (Profile) obj;
        return new EqualsBuilder()
        		.append(getId(), otherProfile.getId())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder()
        		.append(getId())
        		.toHashCode();
    }
}