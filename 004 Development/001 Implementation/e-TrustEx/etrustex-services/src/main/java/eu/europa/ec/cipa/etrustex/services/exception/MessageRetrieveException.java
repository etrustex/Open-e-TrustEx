package eu.europa.ec.cipa.etrustex.services.exception;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class MessageRetrieveException extends EncodedBusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageRetrieveException(String message,
			ErrorResponseCode responseCode) {
		super(message, responseCode);
	}

	public MessageRetrieveException(Throwable cause,
			ErrorResponseCode responseCode) {
		super(cause, responseCode);
	}

	public MessageRetrieveException(Throwable cause, String message,
			ErrorResponseCode responseCode) {
		super(cause, message, responseCode);
	}

}
