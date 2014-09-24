package ru.dz.openGardemarine.exceptions;


public class ProtocolTimeoutException extends
		ProtocolException {

	public ProtocolTimeoutException() {
		super("Packet reception timeout");
	}

	public ProtocolTimeoutException(String message) {
		super(message);
	}

	public ProtocolTimeoutException(Throwable cause) {
		super("Packet reception timeout",cause);
	}

	public ProtocolTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolTimeoutException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
