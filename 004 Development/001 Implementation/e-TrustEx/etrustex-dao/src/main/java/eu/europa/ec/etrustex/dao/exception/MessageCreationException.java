package eu.europa.ec.etrustex.dao.exception;

import eu.europa.ec.etrustex.types.ErrorResponseCode;

public class MessageCreationException extends EncodedBusinessException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageCreationException(String message,
			ErrorResponseCode responseCode) {
		super(message, responseCode);
	}

	public MessageCreationException(Throwable cause,
			ErrorResponseCode responseCode) {
		super(cause, responseCode);
	}

	public MessageCreationException(Throwable cause, String message,
			ErrorResponseCode responseCode) {
		super(cause, message, responseCode);
	}
	
	public MessageCreationException(String message,
			ErrorResponseCode responseCode, String detailedDescription) {
		super(message, responseCode, detailedDescription);
	}	
}
