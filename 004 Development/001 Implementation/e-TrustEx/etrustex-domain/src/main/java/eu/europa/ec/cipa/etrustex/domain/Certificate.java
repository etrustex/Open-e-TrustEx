package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

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
}
