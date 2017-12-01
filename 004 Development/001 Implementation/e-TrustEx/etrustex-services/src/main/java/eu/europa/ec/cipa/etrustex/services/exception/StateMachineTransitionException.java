package eu.europa.ec.cipa.etrustex.services.exception;

public class StateMachineTransitionException extends StateMachineException {

	private static final long serialVersionUID = 7002136046907993126L;

	public StateMachineTransitionException(String message) {
		super(message);
	}

	public StateMachineTransitionException(Throwable cause) {
		super(cause);
	}

	public StateMachineTransitionException(String message, Throwable cause) {
		super(message, cause);
	}

}
