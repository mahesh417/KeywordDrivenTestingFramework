package com.qualitestgroup.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	{
	    put("^\\d{8}$", "MMddyyyy");
	    put("^\\d{7}$", "Mddyyyy");
	    put("^\\d{6}$", "MMddyy");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
	    put("^\\d{1,2}-[a-z]{3}-\\d{4}$", "dd-MMM-yyyy");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
	    put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
	    put("^\\d{14}$", "yyyyMMddHHmmss");
	    put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2} [ap]m$", "MM/dd/yyyy hh:mm:ss a");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2} [ap]m$", "MM-dd-yyyy hh:mm:ss a");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
	    put("^\\d{1,4}-\\d{1,2}-\\d{1,2}_\\d{1,2}h\\d{1,2}m\\d{1,2}s\\d{1,3}ms$", "y-M-d_H'h'm'm's's'S'ms'");
	    put("^\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2} [-+]\\d{2}:\\d{2}$", "yyyy-MM-dd hh:mm:ss X");
	}};

	/**
	 * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
	 * format is unknown. You can simply extend DateUtil with more formats if needed.
	 * @param dateString The date string to determine the SimpleDateFormat pattern for.
	 * @return The matching SimpleDateFormat pattern, or null if format is unknown.
	 * @see SimpleDateFormat
	 */
	public static String determineDateFormat(String dateString) {
		dateString = dateString.toLowerCase();
		if(dateString.matches("^\\d{12}$")) {
			String format1 = "yyyyMMddHHmm";
			String format2 = "MMddyyyyHHmm";
			Date d1 = dateFromFormatString(dateString, format1);
			Date d2 = dateFromFormatString(dateString, format2);
			return d1.after(d2) ? format1 : format2;
		}
	    for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
	        if (dateString.toLowerCase().matches(regexp)) {
	            return DATE_FORMAT_REGEXPS.get(regexp);
	        }
	    }
	    return null; // Unknown format.
	}
	
	/**
	 * Get a Date object from a date String
	 * 
	 * @param dateString The String date to attempt to parse
	 * @return The Date object representing this date
	 */
	public static Date parseDate(String dateString) {
		//attempt to determine the format
		String format = determineDateFormat(dateString);
		
		//unknown format, return null
		if(format == null)
			return null;
		
		//construct the SimpleDateFormat based on determined format
		SimpleDateFormat df = new SimpleDateFormat(format);
		
		//attempt to parse the dateString into a Date object based on the determined format
		try {
			Date d = df.parse(dateString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Date dateFromFormatString(String dateString, String origFormat) {
		SimpleDateFormat df = new SimpleDateFormat(origFormat);
		
		try {
			Date d = df.parse(dateString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Change a date from its existing format to one of your choice
	 * 
	 * @param dateString The String date to attempt to parse
	 * @param formatString The SimpleDateFormat String you want the date in
	 * @return The newly formatted date as a String
	 * @see SimpleDateFormat
	 */
	public static String reformatDate(String dateString, String formatString) {
		//attempt to determine the existing format
		String existingFormat = determineDateFormat(dateString);
		
		//unknown format, return null
		if(existingFormat == null)
			return null;
		
		//construct the existing format
		SimpleDateFormat edf = new SimpleDateFormat(existingFormat);
		//construct the new format
		SimpleDateFormat ndf = new SimpleDateFormat(formatString);
		
		//make Date object from existing format and input
		Date d;
		try {
			d = edf.parse(dateString);
			return ndf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//return null if we couldn't parse it
		return null;
	}
	
	/**
	 * Return a date object representing the current system date/time
	 * @return the current system date/time
	 */
	public static Date getCurrentDate() {
		return new java.util.Date();
	}
	
	/**
	 * Return the current system date/time formatted according
	 * to a SimpleDateFormat string of your choice
	 * @param format The desired format
	 * @return A String representing the current system date/time in
	 * the desired format.
	 */
	public static String getCurrentDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new java.util.Date());
	}
	
	/**
	 * Returns a string of the format "yyyy-MM-dd HH:mm:ss" representing
	 * the current system date/time
	 * @param asString
	 * @return
	 */
	public static String getCurrentDate(boolean asString) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new java.util.Date());
	}
	
	public static int compareDates(String date1, String date2) {
		Date d1 = parseDate(date1);
		Date d2 = parseDate(date2);
		return d1.compareTo(d2);
	}
	
	public static int compareDates(String date1, String date2, String format) {
		Date d1 = dateFromFormatString(date1, format);
		Date d2 = dateFromFormatString(date2, format);
		return d1.compareTo(d2);
	}
	
	public static int compareDates(String date1, String format1, String date2, String format2) {
		Date d1 = dateFromFormatString(date1, format1);
		Date d2 = dateFromFormatString(date2, format2);
		return d1.compareTo(d2);
	}
	
	public static void main(String[] args) {
		System.out.println(reformatDate("8/26/2014 04:07:58 AM", "MM/dd/yyyy HH:mm:ss"));
		System.out.println(reformatDate("2014-11-04 12:05:56 -05:00", "MM/dd/yyyy hh:mm:ss"));
	}
}
