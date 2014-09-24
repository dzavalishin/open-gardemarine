package ru.dz.openGardemarine.exceptions;


public class PacketCRCException extends ProtocolPacketException {


	public PacketCRCException() {
	}

	public PacketCRCException(String message) {
		super(message);
	}

	public PacketCRCException(Throwable cause) {
		super(cause);
	}

	public PacketCRCException(String message, Throwable cause) {
		super(message, cause);
	}

	public PacketCRCException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PacketCRCException(byte[] pkt) {
		this.pkt = pkt;
	}

}
