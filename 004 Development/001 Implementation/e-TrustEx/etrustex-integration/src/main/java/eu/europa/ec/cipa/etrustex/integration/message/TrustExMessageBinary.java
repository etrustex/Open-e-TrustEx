package eu.europa.ec.cipa.etrustex.integration.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import eu.europa.ec.cipa.etrustex.domain.Party;

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
	

}
