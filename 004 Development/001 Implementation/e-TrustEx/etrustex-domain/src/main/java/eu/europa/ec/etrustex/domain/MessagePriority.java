/**
 * 
 */
package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;

@Entity
@Table(name="ETR_TB_MESSAGE_PRIORITY")
public class MessagePriority {
	
	@Id
	@Column(name ="MP_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MP_SENDER_ID", unique=false, nullable=true,updatable=true)
	private Party sender;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MP_TRANS_ID", unique=false, nullable=true,updatable=true)	
	private Transaction transaction;
	
	@Column(name ="MP_PRIORITY", nullable = false)
	private Integer priority;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
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
	public Party getSender() {
		return sender;
	}
	public void setSender(Party sender) {
		this.sender = sender;
	}
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}
	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	

}
