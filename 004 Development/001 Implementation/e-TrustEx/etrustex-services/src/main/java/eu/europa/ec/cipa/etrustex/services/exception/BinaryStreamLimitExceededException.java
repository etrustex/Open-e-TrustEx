package eu.europa.ec.cipa.etrustex.services.exception;

public class BinaryStreamLimitExceededException extends Exception {
	private static final long serialVersionUID = 1L;

	public BinaryStreamLimitExceededException() {
		super();
	}

	public BinaryStreamLimitExceededException(String message, Throwable cause) {
		super(message, cause);
	}

	public BinaryStreamLimitExceededException(String message) {
		super(message);
	}

	public BinaryStreamLimitExceededException(Throwable cause) {
		super(cause);
	}
}
