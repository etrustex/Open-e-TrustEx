package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This entity models the certificates in the platform database.
 * 
 * @author orazisa
 *
 */
@Entity
@Table(name="ETR_TB_CERTIFICATE")
public class Certificate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CERT_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="CERT_TYPE", nullable = false)
	private String type;
	
	@Column(name ="CERT_USAGE", nullable = false)
	private String usage;
	
	@Column(name ="CERT_VERSION", nullable = true)
	private String version;
	
	@Column(name ="CERT_HOLDER", nullable = false)
	private String holder;
	
	@Column(name ="CERT_ISSUER", nullable = false)
	private String issuer;
	
	@Column(name ="CERT_SIGN_ALG", nullable = true)
	private String signatureAlgorithm;
	
	@Column(name ="CERT_SIGN_VAL", nullable = true)
	private String signatureValue;
	
	@Column(name ="CERT_SERIAL_NR", nullable = false)
	private String serialNumber;
	
	@Column(name = "CERT_VALID_FROM", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date validityStartDate;
	
	@Column(name = "CERT_VALID_TO", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date validityEndDate;
	
	@Column(name = "CERT_ENCODED_DATA", nullable = false, length=8000)
	private String encodedData;
	
	@Column(name ="CERT_ATTRIBUTES", nullable = true)
	private String attributes;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CERT_PTY_ID", nullable = true)
	private Party party;	

	@Column(name ="CERT_ACTIVE_FLAG", nullable = false)
	private Boolean isActive;

	@Column(name ="CERT_REVOKED_FLAG", nullable = false)
	private Boolean isRevoked;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public String getSignatureValue() {
		return signatureValue;
	}

	public void setSignatureValue(String signatureValue) {
		this.signatureValue = signatureValue;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getValidityStartDate() {
		return validityStartDate;
	}

	public void setValidityStartDate(Date validityStartDate) {
		this.validityStartDate = validityStartDate;
	}

	public Date getValidityEndDate() {
		return validityEndDate;
	}

	public void setValidityEndDate(Date validityEndDate) {
		this.validityEndDate = validityEndDate;
	}

	public String getEncodedData() {
		return encodedData;
	}

	public void setEncodedData(String encodedData) {
		this.encodedData = encodedData;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsRevoked() {
		return isRevoked;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}
	
	@Override
	public String toString() {
		return "Certificate [id=" + id 
				+ ", S/N=" + serialNumber 
				+ ", type=" + type 
				+ ", usage=" + usage 
				+ ", issuer=" + issuer 
				+ ", holder=" + holder  
				+ ", encoded data=" + encodedData  
				+ ", attributes=" + attributes 
				+ ", version=" + version  
				+ ", sign alg=" + signatureAlgorithm  
				+ ", sign val=" + signatureValue  
				+ ", validFrom=" + validityStartDate  
				+ ", validTo=" + validityEndDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessInfo == null) ? 0 : accessInfo.hashCode());
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((encodedData == null) ? 0 : encodedData.hashCode());
		result = prime * result + ((holder == null) ? 0 : holder.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((isRevoked == null) ? 0 : isRevoked.hashCode());
		result = prime * result + ((issuer == null) ? 0 : issuer.hashCode());
		result = prime * result + ((party == null) ? 0 : party.hashCode());
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + ((signatureAlgorithm == null) ? 0 : signatureAlgorithm.hashCode());
		result = prime * result + ((signatureValue == null) ? 0 : signatureValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
		result = prime * result + ((validityEndDate == null) ? 0 : validityEndDate.hashCode());
		result = prime * result + ((validityStartDate == null) ? 0 : validityStartDate.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		Certificate other = (Certificate) obj;
		if (accessInfo == null) {
			if (other.accessInfo != null)
				return false;
		} else if (!accessInfo.equals(other.accessInfo))
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (encodedData == null) {
			if (other.encodedData != null)
				return false;
		} else if (!encodedData.equals(other.encodedData))
			return false;
		if (holder == null) {
			if (other.holder != null)
				return false;
		} else if (!holder.equals(other.holder))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (isRevoked == null) {
			if (other.isRevoked != null)
				return false;
		} else if (!isRevoked.equals(other.isRevoked))
			return false;
		if (issuer == null) {
			if (other.issuer != null)
				return false;
		} else if (!issuer.equals(other.issuer))
			return false;
		if (party == null) {
			if (other.party != null)
				return false;
		} else if (!party.equals(other.party))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (signatureAlgorithm == null) {
			if (other.signatureAlgorithm != null)
				return false;
		} else if (!signatureAlgorithm.equals(other.signatureAlgorithm))
			return false;
		if (signatureValue == null) {
			if (other.signatureValue != null)
				return false;
		} else if (!signatureValue.equals(other.signatureValue))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (usage == null) {
			if (other.usage != null)
				return false;
		} else if (!usage.equals(other.usage))
			return false;
		if (validityEndDate == null) {
			if (other.validityEndDate != null)
				return false;
		} else if (!validityEndDate.equals(other.validityEndDate))
			return false;
		if (validityStartDate == null) {
			if (other.validityStartDate != null)
				return false;
		} else if (!validityStartDate.equals(other.validityStartDate))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
