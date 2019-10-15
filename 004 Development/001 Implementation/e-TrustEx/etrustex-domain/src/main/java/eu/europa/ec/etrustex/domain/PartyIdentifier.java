package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ETR_TB_PARTY_ID")
public class PartyIdentifier implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="PID_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name = "PID_IDENTIFIER_SCHEME")
	@Enumerated(EnumType.STRING)
	private IdentifierIssuingAgency schemeId;
	
	@Column(name = "PID_IDENTIFIER_VALUE")
	private String value;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@ManyToOne(fetch=FetchType.EAGER/*, cascade = {CascadeType.PERSIST,CascadeType.MERGE}*/)
	@JoinColumn(name = "PID_PTY_ID")
	private Party party;
	
	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
		if (party!=null && !party.getIdentifiers().contains(this)) {
			party.getIdentifiers().add(this);
        }
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public IdentifierIssuingAgency getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(IdentifierIssuingAgency schemeId) {
		this.schemeId = schemeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	@Override
	public String toString() {
		return "PartyIdentifier [id=" + id + ", schemeId=" + schemeId + ", value="
				+ value + ", party=" + party + "]";
	}
	
	@Override
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof PartyIdentifier)) {
            return false;
        }
        
        if (obj == this){
        	return true;
        }
        
        final PartyIdentifier otherPartyIdentifier = (PartyIdentifier) obj;
//        boolean result = new EqualsBuilder().append(getSchemeId(), otherPartyIdentifier.getSchemeId())
//        		.append(getValue(), otherPartyIdentifier.getValue())
//        		.append(getParty(), otherPartyIdentifier.getParty())
//        		.isEquals();
        return new EqualsBuilder().append(getSchemeId(), otherPartyIdentifier.getSchemeId())
        		.append(getValue(), otherPartyIdentifier.getValue())
        		.append(getPartyName(), getPartyName(otherPartyIdentifier.getParty()))
        		.isEquals();
        
    }
	
	@Override
    public int hashCode() {
//        int res = new HashCodeBuilder().append(getSchemeId())
//        		.append(getValue())
//        		.append(getParty())
//        		.toHashCode();
        return new HashCodeBuilder().append(getSchemeId())
        		.append(getValue())
        		.append(getPartyName())
        		.toHashCode();
        
    }
	
	private String getPartyName(){
		
		return getPartyName(getParty());
		
	}
	
	private String getPartyName(Party p){
		
		if (p == null || p.getName() == null){ 
			return null;
		} else {
			return p.getName();
		}
		
	}
	
	

}
