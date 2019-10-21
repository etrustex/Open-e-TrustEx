package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.StateMachine;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ETR_TB_DOCUMENT")
public class Document implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6220084842898644619L;
	
	@Id
	@Column(name ="DOC_ID", nullable = false)
	@GeneratedValue
	private Long id;
	@Column(name ="DOC_NAME", nullable = false)
	private String name;
	@Column(name ="DOC_LOCAL_NAME", nullable = false)
	private String localName;
	
	@Column(name ="DOC_NAMESPACE", nullable = true)
	private String namespace;
	@Column(name ="DOC_TYPE_CD", nullable = true)
	private String documentTypeCode;
	
	@Column(name= "DOC_VERSION", nullable = true)
	private String version;
	
	
	@OneToMany(mappedBy="document")
	private Set<Transaction> transactions = new HashSet<Transaction>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "DOC_SM_ID", nullable = true)
	private StateMachine stateMachine;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
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

	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	@Override
	public String toString() {
		return "Document [id=" + id 
				+ ", name=" + name 
				+ ", localName=" + localName 
				+ ", namespace=" + namespace
				+ ", documentTypeCode=" + documentTypeCode
				+ ", version=" + version
				+ ", transactions=" + transactions
				+ ", stateMachine=" + stateMachine
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((documentTypeCode == null) ? 0 : documentTypeCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((localName == null) ? 0 : localName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
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
		Document other = (Document) obj;
		if (documentTypeCode == null) {
			if (other.documentTypeCode != null)
				return false;
		} else if (!documentTypeCode.equals(other.documentTypeCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (localName == null) {
			if (other.localName != null)
				return false;
		} else if (!localName.equals(other.localName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}	
}
