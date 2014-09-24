package ru.dz.openGardemarine.exceptions;



public class UnexpectedPacketException extends ProtocolPacketException {

	private static final long serialVersionUID = 84317507414523850L;


	public UnexpectedPacketException(byte[] packet, String string) {
		super(string);
		this.pkt = packet;
	}



}
