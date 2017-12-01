package eu.europa.ec.cipa.etrustex.services.exception;

public abstract class StateMachineException extends Exception {

	private static final long serialVersionUID = 4179269681526825088L;

	public StateMachineException(String message) {
		super(message);
	}

	public StateMachineException(Throwable cause) {
		super(cause);
	}

	public StateMachineException(String message, Throwable cause) {
		super(message, cause);
	}

}
