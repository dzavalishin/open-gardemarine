package ru.dz.openGardemarine.transport.bytePipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import ru.dz.openGardemarine.exceptions.CommunicationsException;

public class SimpleTCPBytePipe implements ISimpleBytePipe 
{
	private String hostName;
	private int port;

	private InputStream is;
	private OutputStream os;
	private Socket clientSocket;

	// --------------------------------------------------------------
	// Class specific methods
	// --------------------------------------------------------------
	
	public String getHostName() 				{		return hostName;	}
	public void setHostName(String hostName) 	{		this.hostName = hostName;	}
	public int getPort() 						{		return port;	}
	public void setPort(int port) 				{		this.port = port;	}
	
	
	// --------------------------------------------------------------
	// Interface methods
	// --------------------------------------------------------------
	
	
	
	public SimpleTCPBytePipe() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connect() throws CommunicationsException {
		try {

			clientSocket = new Socket(hostName, port);

			clientSocket.setSoTimeout(500); // Wait this long if no data

			os = clientSocket.getOutputStream();
			is = clientSocket.getInputStream();

		} catch (UnknownHostException e) {
			throw new CommunicationsException(e); 
		} catch (IOException e) {
			throw new CommunicationsException(e); 
		}

	}

	@Override
	public void disconnect() throws CommunicationsException {
		try {
			clientSocket.close();
		} catch (IOException e) {
			throw new CommunicationsException(e); 
		}
	}

	@Override
	public boolean isConected() {
		return clientSocket.isConnected();
	}

	@Override
	public String getName() {
		return getTypeName()+":"+getEndPointName();
	}

	@Override
	public String getTypeName() {
		return "TCP";
	}

	@Override
	public String getEndPointName() {
		return hostName+":"+port;
	}

	@Override
	public int readTimed(int timeoutMsec) {

		try {
			clientSocket.setSoTimeout(timeoutMsec);
			return is.read();
		} catch (IOException e) {
			return -1;
		}


	}

	@Override
	public int writeTimed(byte b, int timeoutMsec) {
		try {
			clientSocket.setSoTimeout(timeoutMsec);
			os.write(b);
			return 0;
		} catch (IOException e) {
			return -1;
		}
	}

}
