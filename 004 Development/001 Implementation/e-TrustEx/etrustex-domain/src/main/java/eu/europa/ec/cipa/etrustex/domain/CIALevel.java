package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity modelling the Confifentiality Integrity Availability Level in the etrustex platform These level can be linked to a specific ICA, To a profiles or to a transactions.
 * 
 * @author orazisa
 *
 */
@Entity
@Table(name="ETR_TB_CIA_LEVEL")
public class CIALevel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CIA_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;

	@Column(name ="CIA_AVAILABILITY_LVL", nullable = false)
	private Integer availabilityLevel;
	
	@Column(name ="CIA_CONFIDENTIALITY_LVL", nullable = false)
	private Integer confidentialityLevel;
	
	@Column(name ="CIA_INTEGRITY_LVL", nullable = false)
	private Integer integrityLevel;
	
	public Integer getAvailabilityLevel() {
		return availabilityLevel;
	}
	
	public void setAvailabilityLevel(Integer availabilityLevel) {
		this.availabilityLevel = availabilityLevel;
	}
	
	public Integer getConfidentialityLevel() {
		return confidentialityLevel;
	}
	
	public void setConfidentialityLevel(Integer confidentialityLevel) {
		this.confidentialityLevel = confidentialityLevel;
	}
	
	public Integer getIntegrityLevel() {
		return integrityLevel;
	}
	
	public void setIntegrityLevel(Integer integrityLevel) {
		this.integrityLevel = integrityLevel;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	@Override
	public String toString() {
		return "CIALevel [id=" + id + ", availabilityLevel="
				+ availabilityLevel + ", confidentialityLevel="
				+ confidentialityLevel + ", integrityLevel=" + integrityLevel
				+ "]";
	}
	
	@Override
    public boolean equals(final Object obj) {
		
		if ((obj == null) || !(obj instanceof CIALevel)) {
            return false;
        }
        
        if (obj == this){
        	return true;
        }
        
        final CIALevel otherCIALevel = (CIALevel) obj;
        
        return new EqualsBuilder().append(getConfidentialityLevel(), otherCIALevel.getConfidentialityLevel())
        		.append(getIntegrityLevel(), otherCIALevel.getIntegrityLevel())
        		.append(getAvailabilityLevel(), otherCIALevel.getAvailabilityLevel())
        		.isEquals();
         
    }
	
	@Override
    public int hashCode() {
		
		return new HashCodeBuilder().append(getConfidentialityLevel())
        		.append(getIntegrityLevel())
        		.append(getAvailabilityLevel())
        		.toHashCode();
        
    }
}
