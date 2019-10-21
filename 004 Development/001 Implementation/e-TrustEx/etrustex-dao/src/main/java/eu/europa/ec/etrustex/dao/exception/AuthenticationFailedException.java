/**
 * 
 */
package eu.europa.ec.etrustex.dao.exception;

import eu.europa.ec.etrustex.types.ErrorResponseCode;

/**
 * @author chiricr
 *
 */
public class AuthenticationFailedException extends EncodedBusinessException {

	private static final long serialVersionUID = -8345948665268162946L;

	public AuthenticationFailedException(String message) {
		super(message, ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	
	public AuthenticationFailedException(String message,Throwable cause) {
		super(message, ErrorResponseCode.AUTHENTICATION_FAILED);
	}
	
	public AuthenticationFailedException(String message,
			ErrorResponseCode responseCode) {
		super(message, responseCode);
	}

	public AuthenticationFailedException(Throwable cause,
			ErrorResponseCode responseCode) {
		super(cause,responseCode);
	}

	public AuthenticationFailedException(Throwable cause, String message,
			ErrorResponseCode responseCode) {
		super(cause, message,responseCode);
	}	

}
