/**
 * 
 */
package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;

@Entity
@Table(name="ETR_TB_MESSAGE_PRIORITY")
public class MessagePriority {
	
	@Id
	@Column(name ="MP_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MP_CRED_ID", unique=false, nullable=true,updatable=true)
	private Credentials credential;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MP_TRANS_ID", unique=false, nullable=true,updatable=true)	
	private Transaction transaction;
	
	@Column(name ="MP_PRIORITY", nullable = false)
	private Integer priority;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Credentials getCredential() {
		return credential;
	}
	public void setCredential(Credentials credential) {
		this.credential = credential;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
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
	
	

}
