package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ETR_TB_INTERCHANGE_AGR")
public class InterchangeAgreement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ICA_ID", nullable = false)
	@GeneratedValue
	private Long id;
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "ICA_PRO_ID", unique = false, nullable = false, updatable = true)
	private Profile profile;
	@ManyToMany
	@JoinTable(name = "ETR_TB_ICA_PARTYROLE", joinColumns = { @JoinColumn(name = "ICA_ID", referencedColumnName = "ICA_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "PAR_ID", referencedColumnName = "PAR_ID") })
	private Set<PartyRole> partyRoles = new HashSet<PartyRole>();

	@Column(name = "ICA_VAL_START_DT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date validityStartDate;

	@Embedded
	private EntityAccessInfo accessInfo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "INT_CIA_ID", nullable = true)
	private CIALevel ciaLevel;

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Set<PartyRole> getPartyRoles() {
		return partyRoles;
	}

	public void setPartyRoles(Set<PartyRole> partyRoles) {
		this.partyRoles = partyRoles;
	}

	public Date getValidityStartDate() {
		return validityStartDate;
	}

	public void setValidityStartDate(Date validityStartDate) {
		this.validityStartDate = validityStartDate;
	}

	public CIALevel getCiaLevel() {
		return ciaLevel;
	}

	public void setCiaLevel(CIALevel ciaLevel) {
		this.ciaLevel = ciaLevel;
	}

	/**
	 * Adds new PartyRole to the InterchangeAgreement. It is used for keeping
	 * relationships consistency.
	 * 
	 * @param partyRole
	 */
	public void addPartyRole(PartyRole partyRole) {
		if (this.partyRoles.contains(partyRole)) {
			return;
		}

		this.partyRoles.add(partyRole);
//		if (!partyRole.getInterchangeAgreement().contains(this)) {
//			partyRole.getInterchangeAgreement().add(this);
//		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ciaLevel == null) ? 0 : ciaLevel.hashCode());
		result = prime * result
				+ ((partyRoles == null) ? 0 : partyRoles.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime
				* result
				+ ((validityStartDate == null) ? 0 : validityStartDate
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterchangeAgreement other = (InterchangeAgreement) obj;
		if (ciaLevel == null) {
			if (other.ciaLevel != null)
				return false;
		} else if (!ciaLevel.equals(other.ciaLevel))
			return false;
		if (partyRoles == null) {
			if (other.partyRoles != null)
				return false;
		} else if (!partyRoles.equals(other.partyRoles))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (validityStartDate == null) {
			if (other.validityStartDate != null)
				return false;
		} else if (!validityStartDate.equals(other.validityStartDate))
			return false;
		return true;
	}

}
