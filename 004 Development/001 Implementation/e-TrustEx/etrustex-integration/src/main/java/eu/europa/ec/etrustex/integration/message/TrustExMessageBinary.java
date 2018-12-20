package eu.europa.ec.etrustex.integration.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import eu.europa.ec.etrustex.domain.Party;

public class TrustExMessageBinary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long binaryId;
	private String fileName;
	private Party issuerParty;
	private Party senderParty;
	private String messageDocumentId;
	private String submitterIdWithScheme;
	private String mimeType;
	private String binaryType;
	private byte[] binaryContent;
	private InputStream  binaryContentIS;
	private Boolean useStrorageEncryption;
	private Long expectedSize;
	
	public Boolean getUseStrorageEncryption() {
		return useStrorageEncryption;
	}
	public void setUseStrorageEncryption(Boolean useStrorageEncryption) {
		this.useStrorageEncryption = useStrorageEncryption;
	}
	public InputStream getBinaryContentIS() {
		return binaryContentIS;
	}
	public void setBinaryContentIS(InputStream binaryContentIS) {
		this.binaryContentIS = binaryContentIS;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public byte[] getBinaryContent() {
		return binaryContent;
	}
	public void setBinaryContent(byte[] binaryContent) {
		this.binaryContent = binaryContent;
	}
	public String getBinaryType() {
		return binaryType;
	}
	public void setBinaryType(String binaryType) {
		this.binaryType = binaryType;
	}
	public Long getBinaryId() {
		return binaryId;
	}
	public void setBinaryId(Long binaryId) {
		this.binaryId = binaryId;
	}
	public String getSubmitterIdWithScheme() {
		return submitterIdWithScheme;
	}
	public void setSubmitterIdWithScheme(String submitterIdWithScheme) {
		this.submitterIdWithScheme = submitterIdWithScheme;
	}
	public Party getIssuerParty() {
		return issuerParty;
	}
	public void setIssuerParty(Party issuerParty) {
		this.issuerParty = issuerParty;
	}
	public Party getSenderParty() {
		return senderParty;
	}
	public void setSenderParty(Party senderParty) {
		this.senderParty = senderParty;
	}
	public String getMessageDocumentId() {
		return messageDocumentId;
	}
	public void setMessageDocumentId(String messageDocumentId) {
		this.messageDocumentId = messageDocumentId;
	}
	public void closeInsputStream(){
		try {
			if (getBinaryContentIS()!=null)
				getBinaryContentIS().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public Long getExpectedSize() {
		return expectedSize;
	}
	public void setExpectedSize(Long expectedSize) {
		this.expectedSize = expectedSize;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(binaryContent);
		result = prime * result + ((binaryId == null) ? 0 : binaryId.hashCode());
		result = prime * result + ((binaryType == null) ? 0 : binaryType.hashCode());
		result = prime * result + ((expectedSize == null) ? 0 : expectedSize.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((issuerParty == null) ? 0 : issuerParty.hashCode());
		result = prime * result + ((messageDocumentId == null) ? 0 : messageDocumentId.hashCode());
		result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + ((senderParty == null) ? 0 : senderParty.hashCode());
		result = prime * result + ((submitterIdWithScheme == null) ? 0 : submitterIdWithScheme.hashCode());
		result = prime * result + ((useStrorageEncryption == null) ? 0 : useStrorageEncryption.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrustExMessageBinary other = (TrustExMessageBinary) obj;
		if (!Arrays.equals(binaryContent, other.binaryContent))
			return false;
		if (binaryId == null) {
			if (other.binaryId != null)
				return false;
		} else if (!binaryId.equals(other.binaryId))
			return false;
		if (binaryType == null) {
			if (other.binaryType != null)
				return false;
		} else if (!binaryType.equals(other.binaryType))
			return false;
		if (expectedSize == null) {
			if (other.expectedSize != null)
				return false;
		} else if (!expectedSize.equals(other.expectedSize))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (issuerParty == null) {
			if (other.issuerParty != null)
				return false;
		} else if (!issuerParty.equals(other.issuerParty))
			return false;
		if (messageDocumentId == null) {
			if (other.messageDocumentId != null)
				return false;
		} else if (!messageDocumentId.equals(other.messageDocumentId))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		if (senderParty == null) {
			if (other.senderParty != null)
				return false;
		} else if (!senderParty.equals(other.senderParty))
			return false;
		if (submitterIdWithScheme == null) {
			if (other.submitterIdWithScheme != null)
				return false;
		} else if (!submitterIdWithScheme.equals(other.submitterIdWithScheme))
			return false;
		if (useStrorageEncryption == null) {
			if (other.useStrorageEncryption != null)
				return false;
		} else if (!useStrorageEncryption.equals(other.useStrorageEncryption))
			return false;
		return true;
	}
	

}
