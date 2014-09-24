package ru.dz.openGardemarine.exceptions;

public class OpenGardemarineException extends Exception {

	public OpenGardemarineException() {
	}

	public OpenGardemarineException(String message) {
		super(message);
	}

	public OpenGardemarineException(Throwable cause) {
		super(cause);
	}

	public OpenGardemarineException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenGardemarineException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
