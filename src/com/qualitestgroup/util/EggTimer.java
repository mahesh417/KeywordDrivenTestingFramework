package com.qualitestgroup.util;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
/**
 * An EggTimer instance allows you to start keeping
 * track of time and discover when the requested duration
 * has ended. Also provides a wrapper for Thread.Sleep to sleep
 * for a desired interval
 * 
 * @author Brian Van Stone
 *
 */
public class EggTimer {
	private boolean running = false;
	private int duration;
	private int interval;
	private long runTime;
	private Date start;
	private Date end;
	private boolean expired;
	
	/**
	 * Constructs a new EggTimer for timing over this.duration
	 * seconds and wrapping a sleep for this.interval seconds
	 * 
	 * @param duration
	 * @param interval
	 */
	public EggTimer(int duration, int interval) {
		this(duration, interval, Unit.SECOND, Unit.SECOND);
	}
	
	public EggTimer(int duration, int interval, Unit durationUnit) {
		this(duration, interval, durationUnit, Unit.SECOND);
	}
	
	public EggTimer(int duration, int interval, Unit durationUnit, Unit intervalUnit) {
		switch(durationUnit) {
		case MILLISECOND:
			this.duration = duration / 1000;
			break;
		case SECOND:
			this.duration = duration;
			break;
		case MINUTE:
			this.duration = duration * 60;
			break;
		}
		
		switch(intervalUnit) {
		case MILLISECOND:
			this.interval = interval;
			break;
		case SECOND:
			this.interval = interval * 1000;
			break;
		case MINUTE:
			this.interval = interval * 60 * 1000;
			break;
		}
		
		this.runTime = 0;
		this.expired = false;
	}
	
	public enum Unit {
		MILLISECOND, SECOND, MINUTE;
		
		public static Unit unitFromString(String arg) {
			switch(arg.toLowerCase()) {
			case "minute":
			case "minutes":
			case "min":
			case "mins":
				return MINUTE;
			case "second":
			case "seconds":
			case "secs":
			case "sec":
			case "s":
				return SECOND;
			case "milliseconds":
			case "millisecond":
			case "millis":
			case "milli":
			case "ms":
				return MILLISECOND;
			default:
				return null;
			}
		}
		
		public static String valuesToString() {
			StringBuilder sb = new StringBuilder();
			for (Unit u : Unit.values()) {
				sb.append(u.toString());
			}
			return sb.toString();
		}
		
		@Override
		public String toString() {
			switch(this) {
			case MILLISECOND:
				return "milliseconds";
			case MINUTE:
				return "minutes";
			case SECOND:
				return "seconds";
			default:
				return super.toString();
			}
		}
	}
	
	/**
	 * Starts the egg timer.
	 * 
	 * @return  Date object representing the time
	 * the timer was started
	 */
	public Date start() {
		if(!running) {
			running = true;
			runTime = 0;
			expired = false;
			start = new Date();
			end = DateUtils.addSeconds(start, duration);
		}
		return start;
	}
	
	/**
	 * Determines whether or not the timer is finished
	 * 
	 * @return true if the timer is finished and false otherwise
	 */
	public boolean done() {
		Date now = new Date();
		if (running == false) {
			return true;
		} else if (now.compareTo(end) >= 0) {
			running = false;
			runTime = (now.getTime() - start.getTime()) / 1000L;
			expired = true;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines whether or not a finished timer naturally expired
	 * due to a timeout. Only accurate if the timeout was discovered
	 * during an invocation of the done method.
	 * @return true if it expired, false otherwise
	 */
	public boolean didExpire() {
		return expired;
	}
	
	/**
	 * Fetches the run time (in seconds) for this EggTimer if it is finished running. Calling
	 * this method on a running EggTimer will raise an IllegalStateException
	 * 
	 * @return the final run time of this EggTimer
	 * @throws IllegalStateException when called on a running EggTimer instance
	 */
	public long getRunTime() throws IllegalStateException {
		if(running == false) {
			return runTime;
		} else {
			throw new IllegalStateException("Attempted to fetch the run time for a running EggTimer instance.");
		}
	}
	
	/**
	 * Delays execution for this.interval seconds
	 */
	public void delay() {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the timer for early exit
	 */
	public void cancel() {
		if(running) {
			Date now = new Date();
			runTime = (now.getTime() - start.getTime()) / 1000L;
			running = false;
		}
	}
	
	/**
	 * unit test
	 * @param args
	 */
	public static void main(String[] args) {
		//without cancel
		EggTimer x = new EggTimer(5,1);
		x.start();
		while(!x.done()) {
			x.delay();
			System.out.println("tick");
		}
		System.out.println("BUZZZZZZZZZ!");
		System.out.println("\tRan for: " + x.getRunTime());
		System.out.println("\tExpired: " + x.didExpire());
		
		//with cancel
		int i = 0;
		x.start();
		while(!x.done()) {
			x.delay();
			System.out.println("tick");
			if(++i > 2)
				x.cancel();
		}
		System.out.println("BUZZZZZZZZZ!");
		System.out.println("\tRan for: " + x.getRunTime());
		System.out.println("\tExpired: " + x.didExpire());
	}
}
