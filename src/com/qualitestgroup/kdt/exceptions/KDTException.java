package com.qualitestgroup.kdt.exceptions;

/**
 * General exception class for KDT.
 * @author Matthew Swircenski
 *
 */
public class KDTException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4989192725855339934L;

	public KDTException(String message)
	{
		super(message);
	}
	
	public KDTException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public KDTException()
	{
		super();
	}
}
