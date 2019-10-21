package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CRED_TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name="ETR_TB_CREDENTIALS")
public abstract class Credentials implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="CRED_ID", nullable = false)
	@GeneratedValue
	private Long id;
	@Column(name="CRED_USER", nullable=false)
	private String user;
	@Column(name="CRED_PASSW", nullable=false)
	private String password;
	@Column(name="CRED_CERT", nullable=true,unique=true)
	private String certificate;
	@OneToOne(mappedBy="credentials",fetch=FetchType.EAGER)
	private Party party;

	@Column(name="CRED_SIG_REQUIRED", nullable=false)
	private Boolean signatureRequired;

	@Column(name="CRED_PASSW_ENCRYPTED", nullable=false)
	private Boolean passwordEncrypted;

	@Column(name="CRED_SALTED", nullable=false)
	private Boolean salt = false;

	@Embedded
	private EntityAccessInfo accessInfo;

	@Column(name="IV",nullable=true)
	private byte[] iv;

	@Transient
	public String getType(){
		DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
		return val == null ? null : val.value();
	}

	@Transient
	private transient String decryptedPassword;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public EntityAccessInfo getAccessInfo() {
		return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}
	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	public Boolean getSignatureRequired() {
		return signatureRequired;
	}
	public void setSignatureRequired(Boolean signatureRequired) {
		this.signatureRequired = signatureRequired;
	}
	public Boolean getPasswordEncrypted() {
		return passwordEncrypted;
	}
	public void setPasswordEncrypted(Boolean passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
		if (BooleanUtils.isFalse(passwordEncrypted)) {
			this.setSalt(Boolean.FALSE);
		}
	}
	public String getDecryptedPassword() { return decryptedPassword;}
	public void setDecryptedPassword(String decryptedPassword) {this.decryptedPassword = decryptedPassword;}

	public Boolean getSalt() {
		return salt;
	}

	public void setSalt(Boolean salt) {
		this.salt = salt;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof Credentials)) {
			return false;
		}

		if (obj == this){
			return true;
		}

		final Credentials otherCredentials = (Credentials) obj;
		return new EqualsBuilder().append(getUser(), otherCredentials.getUser())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getUser())
				.toHashCode();
	}
}
