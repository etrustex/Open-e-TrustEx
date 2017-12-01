package eu.europa.ec.cipa.etrustex.services.exception;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class AuthorisationFailedException extends EncodedBusinessException {
	private static final long serialVersionUID = 1L;
	
	public AuthorisationFailedException(String message) {
		super(message, ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	
	public AuthorisationFailedException(String message,Throwable cause) {
		super(message, ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	
	public AuthorisationFailedException(String message,
			ErrorResponseCode responseCode) {
		super(message, responseCode);
	}

	public AuthorisationFailedException(Throwable cause,
			ErrorResponseCode responseCode) {
		super(cause,responseCode);
	}

	public AuthorisationFailedException(Throwable cause, String message,
			ErrorResponseCode responseCode) {
		super(cause, message,responseCode);
	}
//	public AuthenticationFailedException(ErrorResponseCode responseCode) {
//		super(responseCode);
//	}
}
