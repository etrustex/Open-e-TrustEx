package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ETR_TB_PARTY_ROLE",
		uniqueConstraints={ @UniqueConstraint(columnNames={"PAR_PTY_ID", "PAR_ROL_ID"})})
public class PartyRole implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@Column(name ="PAR_ID", nullable = false)
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PAR_PTY_ID")
	private Party party;
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PAR_ROL_ID")
	private Role role;

	@ManyToMany(mappedBy = "partyRoles", cascade = CascadeType.REMOVE)
	private List<InterchangeAgreement> icas = new ArrayList<InterchangeAgreement>();

	@Embedded
	private EntityAccessInfo accessInfo;

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

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<InterchangeAgreement> getIcas() { return icas; }

	public void setIcas(List<InterchangeAgreement> icas) { this.icas = icas; }

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof PartyRole)) {
			return false;
		}

		final PartyRole other = (PartyRole) obj;
		return new EqualsBuilder().append(getParty().getId(), other.getParty().getId())
				.append(getRole().getId(), other.getRole().getId())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getParty())
				.append(getRole())
				.toHashCode();
	}
}
