package eu.europa.ec.cipa.etrustex.services.dto;

import java.util.Date;
import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.MessageBinary;

public class CreateMessageDTO {
	
	private Long icaId;
	private String documentId;
	private String correlationId;
	private String statusCode;
	private Long issuerId; 
	private Long transactionTypeId;
	private String authenticatedUser;
	private Date receptionDate;
	private	Date issueDate;
	private Long receiverPartyId;
	private Long senderPartyId; 
	private Long parentMessageId;
	private String documentTypeCd;
	private String responseCode;
	private List<MessageBinary> binaries;
	
	private CreateMessageDTO (Builder builder) {
		this.icaId = builder.icaId;
		this.documentId = builder.documentId;
		this.correlationId = builder.correlationId;
		this.statusCode = builder.statusCode;
		this.issuerId = builder.issuerId; 
		this.transactionTypeId = builder.transactionTypeId;
		this.authenticatedUser = builder.authenticatedUser;
		this.receptionDate = builder.receptionDate;
		this.issueDate = builder.issueDate;
		this.receiverPartyId = builder.receiverPartyId;
		this.senderPartyId = builder.senderPartyId; 
		this.parentMessageId = builder.parentMessageId;
		this.documentTypeCd = builder.documentTypeCd;
		this.responseCode = builder.responseCode;
		this.binaries = builder.binaries;
	}
	
	public static class Builder {
		private Long icaId;
		private String documentId;
		private String correlationId;
		private String statusCode;
		private Long issuerId; 
		private Long transactionTypeId;
		private String authenticatedUser;
		private Date receptionDate;
		private	Date issueDate;
		private Long receiverPartyId;
		private Long senderPartyId; 
		private Long parentMessageId;
		private String documentTypeCd;
		private String responseCode;
		private List<MessageBinary> binaries;
		
		public Builder icaId(Long icaId) {
			this.icaId = icaId;
			return this;
		}
		
		public Builder documentId(String documentId) {
			this.documentId = documentId;
			return this;
		}		
		
		public Builder correlationId(String correlationId) {
			this.correlationId = correlationId;
			return this;
		}	
		
		public Builder statusCode(String statusCode) {
			this.statusCode = statusCode;
			return this;
		}
		
		public Builder issuerId(Long issuerId) {
			this.issuerId = issuerId;
			return this;
		}		
		
		public Builder transactionTypeId(Long transactionTypeId) {
			this.transactionTypeId = transactionTypeId;
			return this;
		}		
		
		public Builder authenticatedUser(String authenticatedUser) {
			this.authenticatedUser = authenticatedUser;
			return this;
		}
		
		public Builder receptionDate(Date receptionDate) {
			this.receptionDate = receptionDate;
			return this;
		}
		
		public Builder issueDate(Date issueDate) {
			this.issueDate = issueDate;
			return this;
		}
		
		public Builder receiverPartyId(Long receiverPartyId) {
			this.receiverPartyId = receiverPartyId;
			return this;
		}
		
		public Builder senderPartyId(Long senderPartyId) {
			this.senderPartyId = senderPartyId;
			return this;
		}
		
		public Builder parentMessageId(Long parentMessageId) {
			this.parentMessageId = parentMessageId;
			return this;
		}
		
		public Builder documentTypeCd(String documentTypeCd) {
			this.documentTypeCd = documentTypeCd;
			return this;
		}
		
		public Builder responseCode(String responseCode) {
			this.responseCode = responseCode;
			return this;
		}
		
		public Builder binaries(List<MessageBinary> binaries) {
			this.binaries = binaries;
			return this;
		}						
		
		public CreateMessageDTO build() {
			return new CreateMessageDTO(this);
		}
	}
	
	public Long getIcaId() {
		return icaId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public Long getIssuerId() {
		return issuerId;
	}
	public Long getTransactionTypeId() {
		return transactionTypeId;
	}
	public String getAuthenticatedUser() {
		return authenticatedUser;
	}
	public Date getReceptionDate() {
		return receptionDate;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public Long getReceiverPartyId() {
		return receiverPartyId;
	}
	public Long getSenderPartyId() {
		return senderPartyId;
	}
	public Long getParentMessageId() {
		return parentMessageId;
	}
	public String getDocumentTypeCd() {
		return documentTypeCd;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public List<MessageBinary> getBinaries() {
		return binaries;
	}
	
}
