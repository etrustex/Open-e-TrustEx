package eu.europa.ec.cipa.etrustex.services.dto;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.types.SlaType;

/**
 * DTO for transporting SLA policy search criteria from the integration layer to the DAO layer
 * @author chiricr
 *
 */
public class SlaPolicySearchDTO {
	
	private Party sender;
	private SlaType slaType;
	private Transaction transaction;

	
	public Party getSender() {
		return sender;
	}
	public void setSender(Party sender) {
		this.sender = sender;
	}
	
	public SlaType getSlaType() {
		return slaType;
	}
	public void setSlaType(SlaType slaType) {
		this.slaType = slaType;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
