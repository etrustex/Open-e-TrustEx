package eu.europa.ec.etrustex.dao.exception;

import eu.europa.ec.etrustex.types.ErrorResponseCode;

public class PlatformConfigurationException extends EncodedBusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PlatformConfigurationException(String message,
			ErrorResponseCode responseCode) {
		super(message, responseCode);
		// TODO Auto-generated constructor stub
	}


}
