package ru.dz.openGardemarine.dev.mercury.pkt;



public class ParameterReadRequestPacket extends Packet {

	public ParameterReadRequestPacket(int nParam) {
		super(PKT_TYPE_READ_PARAMETER,makeOneBytePayload(nParam));
	}

	public ParameterReadRequestPacket(int nParam1, int nParam2) {
		super(PKT_TYPE_READ_PARAMETER,makeTwoBytesPayload(nParam1,nParam2));
	}

	
}
