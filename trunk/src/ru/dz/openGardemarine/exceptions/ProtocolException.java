package ru.dz.openGardemarine.exceptions;

public class ProtocolException extends CommunicationsException {

	public ProtocolException() {
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
