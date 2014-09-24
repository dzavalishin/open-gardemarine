package ru.dz.openGardemarine.exceptions;

public class ProtocolPacketException extends ProtocolException {
	protected byte[] pkt;

	public byte[] getPacket() {
		return pkt;
	}
	
	
	
	public ProtocolPacketException() {
	}

	public ProtocolPacketException(String message) {
		super(message);
	}

	public ProtocolPacketException(Throwable cause) {
		super(cause);
	}

	public ProtocolPacketException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolPacketException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
