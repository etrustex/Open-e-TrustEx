package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
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

	@Embedded
	private EntityAccessInfo accessInfo;

    @Transient
    public String getType(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
	
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
