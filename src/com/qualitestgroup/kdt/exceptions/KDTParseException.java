package com.qualitestgroup.kdt.exceptions;

/**
 * Exception occurred during parsing of an input file.
 * @author Matthew Swircenski
 *
 */
public class KDTParseException extends KDTException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1453644504566705086L;

	public KDTParseException(String message) {
		super(message);
	}
	
	public KDTParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
