package eu.europa.ec.etrustex.dao.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.types.SortFieldTypeCode;

public class RetrieveMessagesForQueryRequestDTO {
	
	private Set<Long> receiverPartyIds = null;
	private Set<Long> senderPartyIds = null; 
	private Set<Transaction> transactions = null;
	private Set<String> documentTypeCodes = null;
	private Boolean retrievedInd = null;
	private Date startDate = null;
	private Date endDate = null; 
	private String correlationId;
	private Integer maxResult				 = null; 
	private Boolean fetchParents			 = null; 
	private Boolean fetchBinaries			 = null; 	
	private Boolean isSenderAlsoReceiver = null;
	private Boolean filterOutMessagesInError = null;
	private String userId;
	private Set<String> businessDocumentTypes;
	private BigDecimal paginationFrom;
	private BigDecimal paginationTo;
	private SortFieldTypeCode sortField; 	
	
	private RetrieveMessagesForQueryRequestDTO (Builder builder) {
		this.senderPartyIds = builder.senderPartyIds;
		this.receiverPartyIds = builder.receiverPartyIds;
		this.transactions = builder.transactions;
		this.documentTypeCodes = builder.documentTypeCodes;
		this.retrievedInd = builder.retrievedInd;
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.correlationId = builder.correlationId;
		this.maxResult = builder.maxResult;
		this.fetchParents = builder.fetchParents;
		this.fetchBinaries = builder.fetchBinaries;
		this.isSenderAlsoReceiver = builder.isSenderAlsoReceiver;
		this.filterOutMessagesInError = builder.filterOutMessagesInError;
		this.userId = builder.userId;
		this.businessDocumentTypes = builder.businessDocumentTypes;
		this.paginationFrom = builder.paginationFrom;
		this.paginationTo = builder.paginationTo;
		this.sortField = builder.sortField;				
	}	
	
	public static class Builder {
		
		private Set<Long> receiverPartyIds = null;
		private Set<Long> senderPartyIds = null; 
		private Set<Transaction> transactions = null;
		private Set<String> documentTypeCodes = null;
		private Boolean retrievedInd = null;
		private Date startDate = null;
		private Date endDate = null; 
		private String correlationId;
		private Integer maxResult				 = null; 
		private Boolean fetchParents			 = null; 
		private Boolean fetchBinaries			 = null; 	
		private Boolean isSenderAlsoReceiver = null;
		private Boolean filterOutMessagesInError = null;
		private String userId;
		private Set<String> businessDocumentTypes;
		private BigDecimal paginationFrom;
		private BigDecimal paginationTo;
		private SortFieldTypeCode sortField; 		
		
		public Builder setReceiverPartyIds(Set<Long> receiverPartyIds) {
			this.receiverPartyIds = receiverPartyIds;
			return this;
		}
		public Builder setSenderPartyIds(Set<Long> senderPartyIds) {
			this.senderPartyIds = senderPartyIds;
			return this;
		}		
		public Builder setTransactions(Set<Transaction> transactions) {
			this.transactions = transactions;
			return this;
		}
		public Builder setDocumentTypeCodes(Set<String> documentTypeCodes) {
			this.documentTypeCodes = documentTypeCodes;
			return this;
		}
		public Builder setRetrievedInd(Boolean retrievedInd) {
			this.retrievedInd = retrievedInd;
			return this;
		}
		public Builder setStartDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}
		public Builder setEndDate(Date endDate) {
			this.endDate = endDate;
			return this;
		}
		public Builder setCorrelationId(String correlationId) {
			this.correlationId = correlationId;
			return this;
		}
		public Builder setMaxResult(Integer maxResult) {
			this.maxResult = maxResult;
			return this;
		}
		public Builder setFetchParents(Boolean fetchParents) {
			this.fetchParents = fetchParents;
			return this;
		}
		public Builder setFetchBinaries(Boolean fetchBinaries) {
			this.fetchBinaries = fetchBinaries;
			return this;
		}
		public Builder setIsSenderAlsoReceiver(Boolean isSenderAlsoReceiver) {
			this.isSenderAlsoReceiver = isSenderAlsoReceiver;
			return this;
		}
		public Builder setFilterOutMessagesInError(Boolean filterOutMessagesInError) {
			this.filterOutMessagesInError = filterOutMessagesInError;
			return this;
		}
		
		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public Builder setBusinessDocumentTypes(Set<String> businessDocumentTypes) {
			this.businessDocumentTypes = businessDocumentTypes;
			return this;
		}
		public Builder setPaginationFrom(BigDecimal paginationFrom) {
			this.paginationFrom = paginationFrom;
			return this;
		}
		public Builder setPaginationTo(BigDecimal paginationTo) {
			this.paginationTo = paginationTo;
			return this;
		}
		public Builder setSortField(SortFieldTypeCode sortField) {
			this.sortField = sortField;
			return this;
		}
		
		public RetrieveMessagesForQueryRequestDTO build() {
			return new RetrieveMessagesForQueryRequestDTO(this);
		}
		
		
	}

	public Set<Long> getReceiverPartyIds() {
		return receiverPartyIds;
	}
	public String getUserId() {
		return userId;
	}

	public Set<String> getBusinessDocumentTypes() {
		return businessDocumentTypes;
	}

	public BigDecimal getPaginationFrom() {
		return paginationFrom;
	}

	public BigDecimal getPaginationTo() {
		return paginationTo;
	}

	public SortFieldTypeCode getSortField() {
		return sortField;
	}

	public Set<Long> getSenderPartyIds() {
		return senderPartyIds;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public Set<String> getDocumentTypeCodes() {
		return documentTypeCodes;
	}

	public Boolean getRetrievedInd() {
		return retrievedInd;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public Integer getMaxResult() {
		return maxResult;
	}

	public Boolean getFetchParents() {
		return fetchParents;
	}

	public Boolean getFetchBinaries() {
		return fetchBinaries;
	}

	public Boolean getIsSenderAlsoReceiver() {
		return isSenderAlsoReceiver;
	}

	public Boolean getFilterOutMessagesInError() {
		return filterOutMessagesInError;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
	
}
