package com.qualitestgroup.kdt.exceptions;

/**
 * Keyword initialization exception.
 * @author Matthew Swircenski
 *
 */
public class KDTKeywordInitException extends KDTException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -834448688136235218L;

	public KDTKeywordInitException(String message) {
		super(message);
	}
	
	public KDTKeywordInitException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
