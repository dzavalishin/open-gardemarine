package ru.dz.openGardemarine.transport.bytePipe;

import ru.dz.openGardemarine.exceptions.CommunicationsException;

public interface ISimpleBytePipe 
{

	// -----------------------------------------------------------
	// General control 
	// -----------------------------------------------------------

	/**
	 * Go to operational state, do everything necessary to communicate.
	 * <p>
	 * Connect to endpoint or throw an exception.
	 * 
	 * @throws CommunicationsException If unable to connect.
	 */
	public void connect() throws CommunicationsException;
	
	/**
	 * Leave operational state, release devices and other resources. 
	 * <p>
	 * Must unblock all blocked calls.
	 * 
	 * @throws CommunicationsException If there was some problem. Must disconnect anyway.
	 */
	public void disconnect() throws CommunicationsException;

	/**
	 * Connected to endpoint?
	 * <p>
	 * 
	 * @return true if pipe is connected now.
	 */
	public boolean isConected();



	// -----------------------------------------------------------
	// Info 
	// -----------------------------------------------------------

	/**
	 * Get printable instance name.
	 * <p>
	 * Name includes type name and name of end point within type.
	 * <br>
	 * Synax is not guaranteed, but name parts supposed to be separated by ':',
	 * like "TCP:localhost:502".
	 * 
	 * @return Name of connection (like "TCP hostname", or "Port COM4").
	 */
	public String getName();

	/**
	 * 
	 * Type of connection.
	 * <p>
	 * 
	 * @return Type of connection (TCP, port, whatever).
	 */
	public String getTypeName();
	
	
	/**
	 * Name of object we are connected to.
	 * <p>
	 * @return End point name. Like host for TCP connection, etc 
	 */
	public String getEndPointName();
	
	
	// -----------------------------------------------------------
	// IO 
	// -----------------------------------------------------------
	
	
	/**
	 * Read blocking one byte with timeout.
	 * <p>
	 * Return -1 if timed out with 0 bytes read.
	 *  
	 * @param timeoutMsec Time to wait for byte, msec
	 * @return byte read (positive) or -1 if timed out.
	 */
	public int readTimed(int timeoutMsec);
	
	
	/**
	 * Write blocking one byte with timeout.
	 * <p>
	 * Return -1 if timed out with 0 bytes written.
	 *  
	 * @param timeoutMsec Time to wait for write, msec
	 * @param b byte to write
	 * @return 0 if ok or -1 if timed out.
	 */
	public int writeTimed(byte b, int timeoutMsec);
	
	
}
