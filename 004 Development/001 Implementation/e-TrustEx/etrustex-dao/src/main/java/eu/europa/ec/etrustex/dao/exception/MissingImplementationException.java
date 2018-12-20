/**
 * 
 */
package eu.europa.ec.etrustex.dao.exception;


/**
 * @author batrian
 * 
 */
public class MissingImplementationException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Missing implementation.";

	public MissingImplementationException() {
		super(MESSAGE);
	}

	public MissingImplementationException(String message, Throwable cause) {
		super(MESSAGE + message, cause);
	}

	public MissingImplementationException(String message) {
		super(MESSAGE + message);
	}

	public MissingImplementationException(Throwable cause) {
		super(MESSAGE, cause);
	}

	public MissingImplementationException(String objectName, String methodName) {
		super(MESSAGE + " Object: " + objectName + ", Method: " + methodName);
	}
	
	public MissingImplementationException(String objectName, String methodName, String hint) {
		super(MESSAGE + " Object: " + objectName + ", Method: " + methodName + (hint == null ? "" : "(" + hint + ")"));
	}
}
