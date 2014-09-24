package ru.dz.openGardemarine.exceptions;


/**
 * Thrown if no half duplex mode is implemented.
 * <p>
 * See byte pipe classes.
 * @author dz
 *
 */
public class NoHalfDuplexException extends CommunicationsException {

	public NoHalfDuplexException() {
	}

	public NoHalfDuplexException(String message) {
		super(message);
	}

	public NoHalfDuplexException(Throwable cause) {
		super(cause);
	}

	public NoHalfDuplexException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoHalfDuplexException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
