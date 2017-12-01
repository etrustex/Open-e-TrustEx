package eu.europa.ec.cipa.etrustex.services.exception;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class EncodedBusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorResponseCode responseCode = null;
	
//	public EncodedBusinessException(ErrorResponseCode responseCode) {
//		super();
//		this.responseCode = responseCode;
//	}

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

	public ErrorResponseCode getResponseCode() {
		return responseCode;
	}
}