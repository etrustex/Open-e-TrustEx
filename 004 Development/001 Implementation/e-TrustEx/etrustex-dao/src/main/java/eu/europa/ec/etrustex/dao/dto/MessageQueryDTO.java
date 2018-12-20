package eu.europa.ec.etrustex.dao.dto;

import java.util.Set;

import eu.europa.ec.etrustex.domain.Transaction;

public class MessageQueryDTO {
	
	private Long receiverPartyId 			 = null;
	private Long senderPartyId 				 = null; 
	private Long issuerPartyId 				 = null;
	private Long icaId						 = null;
	private Set<Transaction> transactions	 = null;
	private Integer maxResult				 = null; 
	private String messageDocumentId		 = null;
	private String documentTypeCode			 = null;
	private Boolean retrievedInd			 = null; 
	private Boolean fetchParents			 = Boolean.FALSE; 	
	private Boolean filterOutMessagesInError = Boolean.TRUE;
	private Boolean filterReadServices		 = Boolean.FALSE;
	private Boolean inboxServiceFilter		 = Boolean.FALSE;
	private Long parentIssuerId				 = null;
	
	public MessageQueryDTO(){}
	
	public MessageQueryDTO(Long receiverPartyId, Long senderPartyId, Long issuerPartyId, Long icaId,
			Set<Transaction> transactions, Integer maxResult, String messageDocumentId, String documentTypeCode,
			Boolean retrievedInd, Boolean fetchParents, Boolean fetchBinaries, Boolean filterOutMessagesInError,
			Boolean filterReadServices, Long parentIssuerId) {
		super();
		this.receiverPartyId = receiverPartyId;
		this.senderPartyId = senderPartyId;
		this.issuerPartyId = issuerPartyId;
		this.icaId = icaId;
		this.transactions = transactions;
		this.maxResult = maxResult;
		this.messageDocumentId = messageDocumentId;
		this.documentTypeCode = documentTypeCode;
		this.retrievedInd = retrievedInd;
		this.fetchParents = fetchParents;
		this.filterOutMessagesInError = filterOutMessagesInError;
		this.filterReadServices = filterReadServices;
		this.parentIssuerId = parentIssuerId;
	}
	
	public Long getReceiverPartyId() {
		return receiverPartyId;
	}
	public void setReceiverPartyId(Long receiverPartyId) {
		this.receiverPartyId = receiverPartyId;
	}
	public Long getSenderPartyId() {
		return senderPartyId;
	}
	public void setSenderPartyId(Long senderPartyId) {
		this.senderPartyId = senderPartyId;
	}
	public Long getIssuerPartyId() {
		return issuerPartyId;
	}
	public void setIssuerPartyId(Long issuerPartyId) {
		this.issuerPartyId = issuerPartyId;
	}
	public Long getIcaId() {
		return icaId;
	}
	public void setIcaId(Long icaId) {
		this.icaId = icaId;
	}
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	public String getMessageDocumentId() {
		return messageDocumentId;
	}
	public void setMessageDocumentId(String messageDocumentId) {
		this.messageDocumentId = messageDocumentId;
	}
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public Boolean getRetrievedInd() {
		return retrievedInd;
	}
	public void setRetrievedInd(Boolean retrievedInd) {
		this.retrievedInd = retrievedInd;
	}
	public Boolean getFetchParents() {
		return fetchParents;
	}
	public void setFetchParents(Boolean fetchParents) {
		this.fetchParents = fetchParents;
	}
	public Boolean getFilterOutMessagesInError() {
		return filterOutMessagesInError;
	}
	public void setFilterOutMessagesInError(Boolean filterOutMessagesInError) {
		this.filterOutMessagesInError = filterOutMessagesInError;
	}
	public Boolean getFilterReadServices() {
		return filterReadServices;
	}
	public void setFilterReadServices(Boolean filterReadServices) {
		this.filterReadServices = filterReadServices;
	}
	public Long getParentIssuerId() {
		return parentIssuerId;
	}
	public void setParentIssuerId(Long parentIssuerId) {
		this.parentIssuerId = parentIssuerId;
	}

	public Boolean getInboxServiceFilter() {
		return inboxServiceFilter;
	}

	public void setInboxServiceFilter(Boolean inboxServiceFilter) {
		this.inboxServiceFilter = inboxServiceFilter;
	}
}
