package eu.europa.ec.etrustex.dao.exception;

import eu.europa.ec.etrustex.types.ErrorResponseCode;

public class EncodedBusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorResponseCode responseCode = null;
	private String detailedDescription;
	
	public EncodedBusinessException(Throwable cause, String message, ErrorResponseCode responseCode) {
		super(message, cause);
		this.responseCode = responseCode;
	}

	public EncodedBusinessException(String message, ErrorResponseCode responseCode) {
		super(message);
		this.responseCode = responseCode;
	}

	public EncodedBusinessException(Throwable cause, ErrorResponseCode responseCode) {
		super(cause);
		this.responseCode = responseCode;
	}
	
	public EncodedBusinessException(String message, ErrorResponseCode responseCode, String detailedDescription) {
		super(message);
		this.responseCode = responseCode;
		this.detailedDescription = detailedDescription;
	}	

	public ErrorResponseCode getResponseCode() {
		return responseCode;
	}
	
	public String getDetailedDescription() {
		return detailedDescription;
	}
}