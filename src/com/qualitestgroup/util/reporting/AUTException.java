package com.qualitestgroup.util.reporting;

public class AUTException extends AutomationException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8475696818955691573L;

	public AUTException(String message) 
	{
		super(message);
	}

	public AUTException(String message, int error) 
	{
		super(message, error);
	}

	public AUTException(String message, Throwable t)
	{
		super(message, t);
	}

	public AUTException(String message, Throwable t, int error) 
	{
		super(message, t, error);
	}
}