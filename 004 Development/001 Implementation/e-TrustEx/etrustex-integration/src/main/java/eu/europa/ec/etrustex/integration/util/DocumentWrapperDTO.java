package eu.europa.ec.etrustex.integration.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import eu.europa.ec.etrustex.domain.Party;

public class DocumentWrapperDTO {
	
	private String documentId;
	private String documentTypeCode;
	
	public DocumentWrapperDTO (String documentId, String documentTypeCode) {
		this.documentId = documentId;
		this.documentTypeCode = documentTypeCode;
	}
	
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getDocumentId())
        		.append(getDocumentTypeCode())
        		.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof DocumentWrapperDTO)) {
            return false;
        }
        
        if (obj == this){
        	return true;
        }
        
        final DocumentWrapperDTO otherDTO = (DocumentWrapperDTO) obj;
        
        return new EqualsBuilder()
        		.append(getDocumentId(), otherDTO.getDocumentId())
        		.append(getDocumentTypeCode(), otherDTO.getDocumentTypeCode())
        		.isEquals();
	}
	
	

}
