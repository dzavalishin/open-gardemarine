package ru.dz.openGardemarine.dev.mercury.pkt;

import ru.dz.mercury.pkt.Packet;

public class ChannelClosePacket extends Packet {
	public ChannelClosePacket() {
		super(PKT_TYPE_CHANNEL_CLOSE,empty);
	}
	
}
