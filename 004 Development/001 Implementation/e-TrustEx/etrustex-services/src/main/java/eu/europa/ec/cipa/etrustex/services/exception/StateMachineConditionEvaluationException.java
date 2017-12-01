package eu.europa.ec.cipa.etrustex.services.exception;

public class StateMachineConditionEvaluationException extends
		StateMachineTransitionException {

	private static final long serialVersionUID = -5285666565622608267L;

	public StateMachineConditionEvaluationException(String message) {
		super(message);
	}

	public StateMachineConditionEvaluationException(Throwable cause) {
		super(cause);
	}

	public StateMachineConditionEvaluationException(String message,
			Throwable cause) {
		super(message, cause);
	}

}
