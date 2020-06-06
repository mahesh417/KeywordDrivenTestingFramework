package com.qualitestgroup.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
	
	private String pattern;
	private Pattern p;
	
	/**
	 * Constructs/compiles a PatternMatcher instance based on the pattern provided
	 * This supports two special characters [*,?]
	 * A * represents zero or more of any character (like a traditional regex .*)
	 * A ? represents any single character (like a traditional regex .)
	 * 
	 * @param pattern
	 */
	public PatternMatcher(String pattern) {
		this.pattern = pattern.replaceAll("([\\\\.\\[{(*+?^$|\\)])", "\\\\$1").replaceAll("\\\\\\*", ".*").replaceAll("\\\\\\?", ".");
		p = Pattern.compile(pattern);
	}
	
	/**
	 * Determine whether or not the entire inputString matches
	 * the pattern represented by this PatternMatcher
	 * 
	 * @param inputString The string to test
	 * @return true if the entire input string matches the pattern, false otherwise
	 */
	public boolean matches(String inputString) {
		return p.matcher(inputString).matches();
	}
	
	/**
	 * Determines whether or not any part of the inputString
	 * matches the pattern described by this PatternMatcher
	 * @param inputString The string to test
	 * @return true if some part of inputString matches the pattern, false otherwise
	 */
	public boolean containedIn(String inputString) {
		return p.matcher(inputString).find();
	}
	
	/**
	 * Return a Matcher yielded by calling Pattern.matcher(inputString) on
	 * the underlying Pattern object that represents the pattern string supplied
	 * at instantiation
	 * @param inputString The string to get a matcher for
	 * @return the corresponding Matcher instance
	 * @see java.util.regex.Matcher
	 */
	public Matcher matcher(String inputString) {
		return this.p.matcher(inputString);
	}
	
	/**
	 * Splits inputString into an array of component strings according to
	 * the behavior of Pattern.split
	 * @param inputString The string to split
	 * @return A String array containing the component strings
	 * @see java.util.regex.Pattern
	 */
	public String[] split(String inputString) {
		return p.split(inputString);
	}
	
	/**
	 * Splits inputString into an array of component strings according to
	 * the behavior of Pattern.split
	 * <p>
	 * The limit parameter controls the number of times the pattern is applied
	 * and therefore affects the length of the resulting array. If the limit n
	 * is greater than zero then the pattern will be applied at most n - 1 times,
	 * the array's length will be no greater than n, and the array's last entry
	 * will contain all input beyond the last matched delimiter. If n is non-positive
	 * then the pattern will be applied as many times as possible and the array can
	 * have any length. If n is zero then the pattern will be applied as many times
	 * as possible, the array can have any length, and trailing empty strings will be discarded.
	 * </p>
	 * @param inputString The string to split
	 * @param limit The number of times to apply the pattern
	 * @return A String array containing the component strings
	 * @see java.util.regex.Pattern
	 */
	public String[] split(String inputString, int limit) {
		return p.split(inputString, limit);
	}
	
	/**
	 * Returns the original pattern String from which this PatternMatcher was constructed
	 * @return the original pattern String
	 */
	public String pattern() {
		return pattern;
	}
}
