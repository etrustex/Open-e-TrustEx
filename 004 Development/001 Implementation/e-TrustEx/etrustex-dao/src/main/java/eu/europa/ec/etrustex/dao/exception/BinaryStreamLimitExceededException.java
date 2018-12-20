package eu.europa.ec.etrustex.dao.exception;

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
