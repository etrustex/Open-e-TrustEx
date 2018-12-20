package eu.europa.ec.etrustex.dao.exception;

public class StateMachineConfigException extends StateMachineException {

	private static final long serialVersionUID = 3432668197058532645L;

	public StateMachineConfigException(String message) {
		super(message);
	}

	public StateMachineConfigException(Throwable cause) {
		super(cause);
	}

	public StateMachineConfigException(String message, Throwable cause) {
		super(message, cause);
	}
}
