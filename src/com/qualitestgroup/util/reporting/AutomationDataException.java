package com.qualitestgroup.util.reporting;

public class AutomationDataException extends AutomationException 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4212737151281996942L;

	public AutomationDataException(String message) 
    {
        super(message);
    }

    public AutomationDataException(String message, int error) 
    {
        super(message, error);
    }

    public AutomationDataException(String message, Throwable t) 
    {
        super(message, t);
    }

    public AutomationDataException(String message, Throwable t, int error) 
    {
        super(message, t, error);
    }
}