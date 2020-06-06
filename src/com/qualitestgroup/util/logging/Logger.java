/**
 * @author Cevon C. Carver
 *
 */

package com.qualitestgroup.util.logging;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class Logger{
  /* Get actual class name to be printed on */
 private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Logger.class);
 
 	private LogLevel level;
 
	public static enum LogLevel {
		off(0),
		fatal(1),
		error(2),
		warn(3),
		info(4),
		debug(5),
		trace(6);
		
		private static LogLevel[] lvls = {off, fatal, error, warn, info, debug, trace};
		
		public final int level;
		
		private LogLevel(int lvl)
		{
			this.level = lvl;
		}
		
		public static LogLevel get(int lvl)
		{
			return lvls[lvl];
		}		
	}
	
	public LogLevel getLogLevel()
	{
		return this.level;
	}
  
  // allows the user to change log level
  public void changeLogLevel(LogLevel log_level){
	  
	  // off,fatal,error,warn,info,debug,trace
	  
	  Level newLog;

	  switch(log_level){
	  
		case off:
			newLog = Level.OFF;
			break;
			  
		case fatal:
		  	newLog = Level.FATAL;
		  	break;
		  	
		case error:
		  	newLog = Level.ERROR;
			  break;
		
		case warn:
			  newLog = Level.WARN;
			  break;
		
		case info:
			  newLog = Level.INFO;
			  break;
		
		case debug:
			  newLog = Level.DEBUG;
			  break;
			  
		case trace:
			  newLog = Level.TRACE;
			  break;
					
		// if all cases fail 
		//default will be error
		default :
			newLog = Level.ERROR;
			break;
				  

	  }
	  
	  LogManager.getRootLogger().setLevel(newLog);
	  
	  Level setLog = LogManager.getRootLogger().getLevel();
	  if(!setLog.equals(newLog))
	  {
		  log.warn("Log level not changed. Log level is " + setLog.toString());
	  }
	  
	  log.info("Changing log level");

	  log.info("Log Level has successfuly changed to: " +newLog);
	  
	  level = log_level;
  }
  
  
  //log pointer 
  public static void trace(String pointer){
	  
	  log.trace(pointer);
	  
  }
  
  // log info 
  public static void info(String pointer){
	  
	  log.info(pointer);
  }
  
  
  
    
  // log  debug 
  public static void debug (String pointer){

		 log.debug(pointer);  
	  }
  
  
  
  //log fatal errors
  public static void fatal (String pointer){
	  
		 log.fatal(pointer);  
	  }
  
  
  // log warning errors
  public static void warning(String pointer){
	
	 log.warn(pointer);  
  }
  
  
  //log errors
  public static void error (String pointer){
	  
	 log.error(pointer);
  }
  
  

  
}
  