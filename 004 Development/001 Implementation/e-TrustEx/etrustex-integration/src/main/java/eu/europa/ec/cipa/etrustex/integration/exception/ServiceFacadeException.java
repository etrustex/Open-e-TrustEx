package eu.europa.ec.cipa.etrustex.integration.exception;

import java.io.Serializable;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class ServiceFacadeException extends Exception {

	public ServiceFacadeException() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ServiceFacadeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceFacadeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceFacadeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


}
