package ru.dz.openGardemarine.dev.mercury;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;







import ru.dz.binutil.ByteArray;
import ru.dz.openGardemarine.dev.mercury.data.MercuryActivePower;
import ru.dz.openGardemarine.dev.mercury.data.MercuryEnergy;
import ru.dz.openGardemarine.dev.mercury.data.MercuryFixed;
import ru.dz.openGardemarine.dev.mercury.data.MercuryFreq;
import ru.dz.openGardemarine.dev.mercury.data.MercuryIV;
import ru.dz.openGardemarine.dev.mercury.data.MercuryInfo;
import ru.dz.openGardemarine.dev.mercury.pkt.ChannelOpenPacket;
import ru.dz.openGardemarine.dev.mercury.pkt.ChannelTestPacket;
import ru.dz.openGardemarine.dev.mercury.pkt.Packet;
import ru.dz.openGardemarine.dev.mercury.pkt.ParameterReadRequestPacket;
import ru.dz.openGardemarine.dev.mercury.pkt.ParameterWriteRequestPacket;
import ru.dz.openGardemarine.exceptions.ProtocolTimeoutException;
import ru.dz.openGardemarine.exceptions.UnexpectedPacketException;
import ru.dz.openGardemarine.exceptions.PacketCRCException;
import ru.dz.openGardemarine.exceptions.ProtocolException;


/**
 * Connect to Mercury 230 AC power meter RS485 port through TCP/IP protocol converter.
 * <p>
 * <li>Read power measure results as displayed on meter indicator.
 * <li>Read instant I/V/Freq
 * <li>Read active/reactive/total power 
 * <p>
 * 
 * @author dz
 *
 */
public class Mercury230Connection 
{
	private boolean dumpPacketData = false;

	private static final int MAX_PKT_LEN = 512; // Why? max payload 256?

	/** Power metering data contain special info in hight bits. */
	private static final byte P_MASK = 0x3F;  

	//private ISimpleBytePipe pipe;
	
	private String hostName;
	private int port;

	private InputStream is;
	private OutputStream os;
	private Socket clientSocket;
	private byte netAddress = 0; // broadcast

	public String getHostName() {		return hostName;	}
	public void setHostName(String hostName) {		this.hostName = hostName;	}
	public int getPort() {		return port;	}
	public void setPort(int port) {		this.port = port;	}

	public void connect() throws UnknownHostException, IOException
	{

		clientSocket = new Socket(hostName, port);

		//clientSocket.setSoTimeout(getNextPktTimeout()); // Wait this long if no data
		clientSocket.setSoTimeout(500); // Wait this long if no data

		os = clientSocket.getOutputStream();
		is = clientSocket.getInputStream();
	}

	private void sendByte(byte c) throws IOException {
		os.write(c);
	}

	private void sendShort(int s) throws IOException {
		os.write((byte) (s & 0xFF));
		os.write((byte) ((s>>8) & 0xFF));
	}

	private int readByte() throws IOException {
		int b = is.read();

		if( b == -1 ) throw new IOException();

		return b;
	}

	private int readShort() throws IOException {

		int lo = readByte();
		int hi = readByte();

		int shortValue = (hi & 0xFF) << 8;
		shortValue |= (lo & 0xFF);

		return shortValue;
	}


	private void drainInput() throws IOException {
		while( is.available() > 0 )
			readByte();		
	}


	public void disconnect() throws IOException
	{
		clientSocket.close();
	}

	// ---------------------------------------------------------------------------
	//
	// ---------------------------------------------------------------------------

	/**
	 * Timeout at the end of packet
	 * @return time, msec
	 */
	private int getSendPktEndTimeout() {
		// For 9600
		return 5; 
	}

	/**
	 * Timeout before sending next packet
	 * @return time, msec
	 */
	private int getNextPktTimeout() {
		// For 9600
		return 150; 
	}


	public int getNetAddress() {
		return ((int)netAddress) & 0xFF;
	}
	public void setNetAddress(byte netAddress) {
		this.netAddress = netAddress;
	}
	public Packet readPacket() throws PacketCRCException, IOException, ProtocolTimeoutException
	{
		int pos = 0;
		byte[] ans = new byte[MAX_PKT_LEN];

		try{

			while( is.available() > 0 )
			{
				ans[pos++] = (byte) readByte();
			}

		} catch(java.net.SocketTimeoutException t)
		{
			// It's ok, timeout means we have a packet
		}

		if( pos == 0 )
			throw new ProtocolTimeoutException();
		//throw new Mercury230UnexpectedPacketException(ans, "zero length packet")

		byte [] reply = new byte[pos];
		System.arraycopy(ans, 0, reply, 0, pos);

		if(dumpPacketData) ByteArray.dumpBytes("got pklt", reply);

		Packet p = new Packet(reply); 

		return p;
	}


	public Packet readNonRcPacket() throws ProtocolException, IOException
	{
		Packet pkt = readPacket();
		
		if(pkt.isReturnCodePacket())
			throw new ProtocolException("Got rc="+pkt.getReturnCode());
		
		return pkt;
	}

	int readRetCodePacket() throws UnexpectedPacketException, PacketCRCException, IOException, ProtocolTimeoutException
	{
		Packet p = readPacket();
		if(!p.isReturnCodePacket())
			throw new UnexpectedPacketException(p.getPacketBytes(),"Expected result code packet");

		return p.getReturnCode();
	}


	
	public void sendPacked(Packet p) throws IOException
	{
		p.setAddress(netAddress);
		byte[] toSend = p.getPacketBytes();

		if(dumpPacketData) ByteArray.dumpBytes("send pkt", toSend);

		for( byte b : toSend )
			sendByte(b);

		sleep(getSendPktEndTimeout());
		sleep(getNextPktTimeout());
	}










	public void sendParameterReadRequestPacket(int nParam) throws IOException
	{
		sendPacked(new ParameterReadRequestPacket(nParam));		
	}

	public void sendParameterReadRequestPacket(int nParam, int subParam) throws IOException
	{
		sendPacked(new ParameterReadRequestPacket(nParam, subParam));		
	}

	// ---------------------------------------------------------------------------
	// Read meter general parameters
	// ---------------------------------------------------------------------------

	public int readDeviceAddress() throws IOException, ProtocolException
	{
		sendParameterReadRequestPacket(5);
		byte[] payload = readNonRcPacket().getPayload();
		return payload[1];
	}

	public void programDeviceAddress(int address) throws IOException, ProtocolException
	{
		byte [] a = new byte[1];
		sendPacked(new ParameterWriteRequestPacket(Packet.PKT_W_PARAM_ADDDRESS, a));
	}
	

	// ---------------------------------------------------------------------------
	//
	// ---------------------------------------------------------------------------

	private void ping()
	{
		try {
			System.out.print("Ping... ");

			sendPacked(new ChannelTestPacket());
			int retCode = readRetCodePacket();

			System.out.println("rc = "+retCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnexpectedPacketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PacketCRCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	int openChannel(int level, String passwd) throws ProtocolException
	{
		try {
			sendPacked(new ChannelOpenPacket(level, passwd));
			return readRetCodePacket();
		} catch (IOException e) {
			throw new ProtocolException(e);		
		}
	}

	void sleep(int mSec)
	{
		synchronized (this) {


			try {
				this.wait(mSec);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnknownHostException, IOException, ProtocolException {
		Mercury230Connection c = new Mercury230Connection();

		if(false)
		{
			c.setHostName("etherwan.");
			c.setPort(604);
		} else {
			c.setHostName("moxa.");
			c.setPort(4002);
		}
		c.connect();

		c.ping();
		c.openChannel(1, "\1\1\1\1\1\1");

		int addr = c.readDeviceAddress();
		System.out.println("Device address = "+addr);

		(new MercuryFreq(c)).dump();
		(new MercuryInfo(c)).dump();
		
		
		while(true)
			getInstantPowerValues(c);

		//while(true)			c.ping();

	}

	private static void getInstantPowerValues(Mercury230Connection c) throws PacketCRCException, ProtocolTimeoutException 
	{

		try {
			
			MercuryIV iv = new MercuryIV(c);			
			//MercuryPower power = new MercuryPower(this);
			MercuryActivePower power = new MercuryActivePower(c);
			MercuryEnergy energy = new MercuryEnergy(c);

			iv.dump();
			power.dump();			
			energy.dump();



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Current
		catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * Read and decode typical I/V/etc reply with 3 fixed numbers 3 bytes each.
	 * <p>
	 * 
	 * 
	 * @throws IOException
	 * @throws ProtocolException 
	 */
	public double[] read3dPacket() throws IOException, ProtocolException {
		double[] v = new double[3];
		byte[] packet = readNonRcPacket().getPayload();
		MercuryFixed.decode3x3(packet, v);
		return v;
	}

	/**
	 * Read and decode typical power reply with 4 fixed numbers 3 bytes each.
	 * <p>
	 * NB! We do clear power specific high bits in 1st bytes of 4 numbers 
	 * 
	 * 
	 * @throws IOException
	 * @throws ProtocolException 
	 */
	public double[] read4dPacket() throws IOException, ProtocolException {
		double[] v = new double[4];
		byte[] packet = readNonRcPacket().getPayload();


		packet[0] &= P_MASK;
		packet[3] &= P_MASK;
		packet[6] &= P_MASK;
		packet[9] &= P_MASK;

		v[0] = MercuryFixed.decode3b(packet,0);
		v[1] = MercuryFixed.decode3b(packet,3);
		v[2] = MercuryFixed.decode3b(packet,6);
		v[3] = MercuryFixed.decode3b(packet,9);

		return v;
	}


	
	/* ��������������� �������. ��, ��� ���.

	sendparameterReadRequestPacket(0x14, 0xF0);
	packet = readPacket().getPayload();
	CCU825Test.dumpBytes("Energy", packet);

	sendparameterReadRequestPacket(0x14, 0xF1);
	packet = readPacket().getPayload();
	CCU825Test.dumpBytes("Energy T1", packet);

	sendparameterReadRequestPacket(0x14, 0xF2);
	packet = readPacket().getPayload();
	CCU825Test.dumpBytes("Energy T2", packet);

	sendparameterReadRequestPacket(0x14, 0xF3);
	packet = readPacket().getPayload();
	CCU825Test.dumpBytes("Energy T3", packet);

	sendparameterReadRequestPacket(0x14, 0xF4);
	packet = readPacket().getPayload();
	CCU825Test.dumpBytes("Energy T4", packet);
	 */			
	

}
