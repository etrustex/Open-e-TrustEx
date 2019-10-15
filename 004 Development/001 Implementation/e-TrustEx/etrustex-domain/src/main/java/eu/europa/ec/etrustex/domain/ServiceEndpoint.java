package eu.europa.ec.etrustex.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="ETR_TB_SERVICE_ENDPOINT")
public class ServiceEndpoint {
	
	
	@Id
	@Column(name ="ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="NAME", nullable = false, unique = true)
	private String endpointName;
	
	@ManyToMany
	@JoinTable(name = "ETR_TB_SERVICE_EP_TRANSACTION", 
		joinColumns = { @JoinColumn(name = "ENDPOINT_ID", referencedColumnName = "ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "TRANSACTION_ID", referencedColumnName = "TRA_ID") })
	private Set<Transaction> transactions = new HashSet<Transaction>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

}
