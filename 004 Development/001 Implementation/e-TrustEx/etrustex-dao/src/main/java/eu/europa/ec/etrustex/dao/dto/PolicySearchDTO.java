package eu.europa.ec.etrustex.dao.dto;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.policy.Policy;

/**
 * DTO for transporting policies search criteria from the integration layer to the DAO layer
 * @author chiricr
 *
 */
public class PolicySearchDTO {
	
	private Party sender;
	private Class<? extends Policy> policyType;
	private Transaction transaction;
	
	public Party getSender() {
		return sender;
	}
	public void setSender(Party sender) {
		this.sender = sender;
	}
	
	public Class<? extends Policy> getPolicyType() {
		return policyType;
	}
	public void setPolicyType(Class<? extends Policy> policyType) {
		this.policyType = policyType;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
