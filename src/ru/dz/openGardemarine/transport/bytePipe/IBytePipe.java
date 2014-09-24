package ru.dz.openGardemarine.transport.bytePipe;

import ru.dz.openGardemarine.events.BytePipeEventListener;
import ru.dz.openGardemarine.exceptions.CommunicationsException;
import ru.dz.openGardemarine.exceptions.NoHalfDuplexException;


public interface IBytePipe extends ISimpleBytePipe 
{


	// -----------------------------------------------------------
	// IO 
	// -----------------------------------------------------------

	

	
	/**
	 * Read data.length bytes of data. Block if not enough data present.
	 * @param data Buffer to put data to.
	 * 
	 * @throws CommunicationsException If unable to read.
	 */
	public void read(byte data[]) throws CommunicationsException;

	
	/**
	 * Write data.length bytes of data. Block if unable to write at once.
	 * @param data Data to send.
	 * 
	 * @throws CommunicationsException If unable to write.
	 */
	public void write(byte[] data) throws CommunicationsException;

	
	/**
	 * Write size bytes of data. Block if unable to write at once.
	 * @param data Data to send.
	 * @param size Size of data to send. Better if == data.length.
	 * 
	 * @throws CommunicationsException If unable to write.
	 */
	public void write(byte[] data, int size) throws CommunicationsException;
	
	/**
	 * Write one byte of data. Block if unable to write at once.
	 * @param data Data to send.
	 * 
	 * @throws CommunicationsException If unable to write.
	 */
	public void write(byte data) throws CommunicationsException;

	
	
	/**
	 * Read data.length bytes of data or less. Do not block if not enough data present. 
	 * <b>NB!</b> No way to tell about error here!
	 * @param data Buffer to put data to.
	 * @return Number of bytes read.
	 */
	public int readUnblocked(byte data[]);

	
	/**
	 * Write data.length bytes of data. Block if unable to write at once.
	 * <b>NB!</b> No way to tell about error here!
	 * @param data Data to send.
	 * @return Number of bytes written.
	 */
	public int writeUnblocked(byte data[]);
	
	public void flush();



	

	/**
	 * Read line up to [CR]LF. CR is optional.
	 * @return null if read buffer does not have a complete line, or line of text
	 * excluding final CR and/or LF.
	 */
	public String readLineUnblocked();

	
	/**
	 * @return Number of bytes available for unblocked read.
	 */
	public int available();

	/**
	 * Take a look at the next character, but not consume it.
	 * @return Next character in buffer to be returned on read or -1 if none.
	 */
	public int peek();

	
	// -----------------------------------------------------------
	// Half-duplex
	// -----------------------------------------------------------

	/**
	 * In half-duplex mode pipe will not attempt sending if reception is
	 * in progress.
	 * @param halfDuplexMode Set or reset half duplex mode.
	 * @throws NoHalfDuplexException If this implementation has no half-duplex mode.
	 */
	public void setHalfDuplexMode(boolean halfDuplexMode) throws NoHalfDuplexException; 

	/**
	 * @return True if pipe is in half-duplex mode
	 */
	boolean isHalfDuplexMode();

	
	// -----------------------------------------------------------
	// Events 
	// -----------------------------------------------------------

	
	/**
	 * Add reception listener.
	 * 
	 * @param l Listener to be informed about data reception.
	 */
	public void addReceiveListener(BytePipeEventListener l); 
	public void removeReceiveListener(BytePipeEventListener l);

	/**
	 * Add error listener. Event contains exception.
	 * 
	 * @param l Listener to be informed about problems.
	 */
	public void addErrorListener(BytePipeEventListener l);
	public void removeErrorListener(BytePipeEventListener l);


	
}
