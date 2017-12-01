package eu.europa.ec.cipa.etrustex.services.exception;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class UndefinedIdentifierException extends EncodedBusinessException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UndefinedIdentifierException(String message) {
		super(message,ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	public UndefinedIdentifierException(Throwable cause) {
		super(cause,ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	public UndefinedIdentifierException(Throwable cause, String message) {
		super(cause, message,ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	
	public UndefinedIdentifierException(String message,
			ErrorResponseCode responseCode) {
		super(message,responseCode);
	}
	public UndefinedIdentifierException(Throwable cause,
			ErrorResponseCode responseCode) {
		super(cause,responseCode);
	}

	public UndefinedIdentifierException(Throwable cause, String message,
			ErrorResponseCode responseCode) {
		super(cause, message,responseCode);
	}


	
	
}
