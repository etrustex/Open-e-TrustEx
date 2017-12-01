package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ETR_TB_TRANSACTION")
public class Transaction implements Serializable {

	@Id
	@Column(name ="TRA_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name= "TRA_NAME", nullable = false)
	private String name;
	
	@Column(name= "TRA_NAMESPACE", nullable = true)
	private String namespace;
	
	@Column(name= "TRA_REQ_LOCAL_NAME", nullable = true)
	private String requestLocalName;
	
	@Column(name= "TRA_RES_LOCAL_NAME", nullable = true)
	private String responseLocalName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRA_SEN_ROL_ID")
	private Role senderRole;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRA_REC_ROL_ID")
	private Role receiverRole;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRA_DOC_ID")
	private Document document;
	
	@Column(name= "TRA_VERSION", nullable = true)
	private String version;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRA_CIA_ID", nullable = true)
	private CIALevel ciaLevel;
	
	@Column(name ="TRA_TYPE_CD", nullable = true)
	private String transactionTypeCode;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@ManyToMany(mappedBy="transactions" ,fetch=FetchType.EAGER)
	private Set<Profile> profiles = new HashSet<Profile>();
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getSenderRole() {
		return senderRole;
	}

	public void setSenderRole(Role senderRole) {
		this.senderRole = senderRole;
	}

	public Role getReceiverRole() {
		return receiverRole;
	}

	public void setReceiverRole(Role receiverRole) {
		this.receiverRole = receiverRole;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Set<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	public String getRequestLocalName() {
		return requestLocalName;
	}

	public void setRequestLocalName(String requestLocalName) {
		this.requestLocalName = requestLocalName;
	}
	public String getResponseLocalName() {
		return responseLocalName;
	}

	public void setResponseLocalName(String responseLocalName) {
		this.responseLocalName = responseLocalName;
	}

	public CIALevel getCiaLevel() {
		return ciaLevel;
	}

	public void setCiaLevel(CIALevel ciaLevel) {
		this.ciaLevel = ciaLevel;
	}

	@Override
	public String toString() {
		return getId().toString();
	}
	
	@Override
    public boolean equals(final Object obj) {
        
		if ((obj == null) || !(obj instanceof Transaction)) {
            return false;
        }
        
        final Transaction otherTransaction = (Transaction) obj;
        return new EqualsBuilder().append(getName(), otherTransaction.getName())
        		.append(getId(), otherTransaction.getId())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName())
        		.append(getId())
        		.toHashCode();
    }

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}	

}
