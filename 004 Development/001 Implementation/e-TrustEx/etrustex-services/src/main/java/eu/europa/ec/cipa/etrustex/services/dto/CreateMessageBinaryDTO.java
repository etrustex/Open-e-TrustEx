package eu.europa.ec.cipa.etrustex.services.dto;

import java.io.InputStream;

import eu.europa.ec.cipa.etrustex.domain.Party;

public class CreateMessageBinaryDTO {
	
	private String binaryType;
	private String mimeType;
	private String creatorID; 
	private InputStream inputStream; 
	private Boolean encryptBinary;
	private Boolean useFileStore; 
	private String senderIdWithScheme;
	private Long expectedSize;
	private Party issuer;
	private String documentId;
	private byte[] iv;
	private String fullFilePath;
	
	private CreateMessageBinaryDTO(Builder builder) {
		this.binaryType = builder.binaryType;
		this.mimeType = builder.mimeType;
		this.creatorID = builder.creatorID;
		this.inputStream = builder.inputStream;
		this.encryptBinary = builder.encryptBinary;
		this.useFileStore = builder.useFileStore;
		this.senderIdWithScheme = builder.senderIdWithScheme;
		this.expectedSize = builder.expectedSize;
		this.issuer = builder.issuer;
		this.documentId = builder.documentId;
		this.iv = builder.iv;
	}
	
	
	public static class Builder {
		private String binaryType;
		private String mimeType;
		private String creatorID; 
		private InputStream inputStream; 
		private Boolean encryptBinary;
		private Boolean useFileStore; 
		private String senderIdWithScheme;
		private Long expectedSize;		
		private Party issuer;
		private String documentId;
		private byte[] iv;
		
		public Builder binaryType(String binaryType) {
			this.binaryType = binaryType;
			return this;
		}
		
		public Builder mimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}		
		
		public Builder creatorID(String creatorID) {
			this.creatorID = creatorID;
			return this;
		}	
		
		public Builder inputStream(InputStream inputStream) {
			this.inputStream = inputStream;
			return this;
		}
		
		public Builder encryptBinary(Boolean encryptBinary) {
			this.encryptBinary = encryptBinary;
			return this;
		}		
		
		public Builder useFileStore(Boolean useFileStore) {
			this.useFileStore = useFileStore;
			return this;
		}		
		
		public Builder senderIdWithScheme(String senderIdWithScheme) {
			this.senderIdWithScheme = senderIdWithScheme;
			return this;
		}
		
		public Builder expectedSize(Long expectedSize) {
			this.expectedSize = expectedSize;
			return this;
		}
		
		public Builder issuer(Party issuer) {
			this.issuer = issuer;
			return this;
		}		
		
		public Builder documentId(String documentId) {
			this.documentId = documentId;
			return this;
		}	
		
		public Builder iv(byte[] iv) {
			this.iv = iv;
			return this;
		}			
		
		public CreateMessageBinaryDTO build() {
			return new CreateMessageBinaryDTO(this);
		}
				
	}
	
	public String getBinaryType() {
		return binaryType;
	}
	public String getMimeType() {
		return mimeType;
	}
	public String getCreatorID() {
		return creatorID;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public Boolean getEncryptBinary() {
		return encryptBinary;
	}
	public Boolean getUseFileStore() {
		return useFileStore;
	}
	public String getSenderIdWithScheme() {
		return senderIdWithScheme;
	}
	public Long getExpectedSize() {
		return expectedSize;
	}
	public void setExpectedSize(Long expectedSize) {
		this.expectedSize = expectedSize;
	}
	public Party getIssuer() {
		return issuer;
	}
	public String getDocumentId() {
		return documentId;
	}
	
	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	public byte[] getIv() {
		return iv;
	}
	public String getFullFilePath() {
		return fullFilePath;
	}
	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}
	
	
}
