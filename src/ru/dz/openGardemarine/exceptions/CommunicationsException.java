package ru.dz.openGardemarine.exceptions;

public class CommunicationsException extends OpenGardemarineException {

	public CommunicationsException() {
	}

	public CommunicationsException(String message) {
		super(message);
	}

	public CommunicationsException(Throwable cause) {
		super(cause);
	}

	public CommunicationsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunicationsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
