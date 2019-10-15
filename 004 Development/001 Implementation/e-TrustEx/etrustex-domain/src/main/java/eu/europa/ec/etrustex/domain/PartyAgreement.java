package eu.europa.ec.etrustex.domain;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ETR_TB_PARTYAGREEMENT")
public class PartyAgreement {

	@Id
	@Column(name ="PAG_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="PAG_AUTH_PTY_ID", unique=false, nullable=false,updatable=true)
	private Party authorizingParty;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="PAG_DEL_PTY_ID", unique=false, nullable=false,updatable=true)
	private Party delegateParty;
	
    @ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "ETR_TB_PARTYAGREEMENT_TRAN", 
		joinColumns = { @JoinColumn(name = "PAT_PAG_ID", referencedColumnName = "PAG_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "PAT_TRA_ID", referencedColumnName = "TRA_ID") })
	private Set<Transaction> transactions = new HashSet<Transaction>();

	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Party getAuthorizingParty() {
		return authorizingParty;
	}


	public void setAuthorizingParty(Party authorizingParty) {
		this.authorizingParty = authorizingParty;
	}


	public Party getDelegateParty() {
		return delegateParty;
	}


	public void setDelegateParty(Party delegateParty) {
		this.delegateParty = delegateParty;
	}


	public Set<Transaction> getTransactions() {
		return transactions;
	}


	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}


	public EntityAccessInfo getAccessInfo() {
		return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}


	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
}
