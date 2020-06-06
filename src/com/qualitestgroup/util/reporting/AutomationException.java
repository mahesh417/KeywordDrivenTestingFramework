package com.qualitestgroup.util.reporting;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AutomationException extends Exception 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5864029571496131333L;
	private int errorCondition = 0;

    public AutomationException(String message) 
    {
        super(message);
    }

    public AutomationException(String message, int error) 
    {
        super(message);
        errorCondition = error;
    }

    public AutomationException(String message, Throwable t) 
    {
        super(message, t);
    }

    public AutomationException(String message, Throwable t, int error) 
    {
        super(message, t);
        errorCondition = error;
    }

    public int getErrorCondition() 
    {
        return errorCondition;
    }

    public String getStackTraceAsString() 
    {
        PrintWriter pw = null;
        
        try 
        {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);

            this.printStackTrace(pw);
            return sw.toString();
        } 
        finally 
        {
            if (pw != null)
                pw.close();
        }
    }
}
