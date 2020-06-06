package com.qualitestgroup.kdt.exceptions;

/**
 * Keyword execution exception. 
 * @author Matthew Swircenski
 *
 */
public class KDTKeywordExecException extends KDTException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7222674187043337611L;
	
	public KDTKeywordExecException(String message) {
		super(message);
	}
	
	public KDTKeywordExecException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
