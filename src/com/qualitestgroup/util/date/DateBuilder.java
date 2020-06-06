package com.qualitestgroup.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

import com.qualitestgroup.util.DateUtil;

/**
 * Utility class for building a Date in either String, Date, or Calendar format
 * Based on the concrete Calendar implementation, java.util.GregorianCalendar
 * 
 * @author Brian Van Stone
 */
public class DateBuilder {
	
	/**
	 * These three should always be kept current
	 */
	private SimpleDateFormat desiredFormat;
	private Calendar calendar;
	
	/**
	 * Construct a new DateBuilder initially containing the
	 * current date and targeting a desiredFormat of
	 * "yyyy-MM-dd HH:mm:ss"
	 */
	public DateBuilder() {
		this.calendar = new GregorianCalendar();
		this.desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	private void add(int field, int value) {
		this.calendar.set(field, this.calendar.get(field) + value);
		long millis = this.calendar.getTimeInMillis();
		this.calendar.clear();
		this.calendar.setTimeInMillis(millis);
	}
	
	private void set(int field, int value) {
		this.calendar.set(field, value);
		long millis = this.calendar.getTimeInMillis();
		this.calendar.clear();
		this.calendar.setTimeInMillis(millis);
	}
	
	public DateBuilder fromString(String dateString) throws ParseException {
		return fromString(dateString, false);
	}
	
	public DateBuilder fromString(String dateString, boolean preserveFormat) throws ParseException {
		SimpleDateFormat originFormat = new SimpleDateFormat(DateUtil.determineDateFormat(dateString));
		desiredFormat = preserveFormat == true ? originFormat : desiredFormat;
		this.calendar.setTime(originFormat.parse(dateString));
		return this;
	}
	
	public DateBuilder fromDate(Date date) {
		this.calendar.setTime(date);
		return this;
	}
	
	public DateBuilder fromCalendar(Calendar calendar) {
		this.calendar = calendar;
		return this;
	}
	
	public DateBuilder toFormat(String format) {
		this.desiredFormat = new SimpleDateFormat(format);
		return this;
	}
	
	public DateBuilder toFormat(SimpleDateFormat format) {
		this.desiredFormat = format;
		return this;
	}
	
	public DateBuilder addMillis(int millis) {
		add(Calendar.MILLISECOND, millis);
		return this;
	}
	
	public DateBuilder addSeconds(int seconds) {
		add(Calendar.SECOND, seconds);
		return this;
	}
	
	public DateBuilder addMinutes(int minutes) {
		add(Calendar.MINUTE, minutes);
		return this;
	}
	
	public DateBuilder addHours(int hours) {
		add(Calendar.HOUR, hours);
		return this;
	}
	
	public DateBuilder addDays(int days) {
		add(Calendar.DATE, days);
		return this;
	}
	
	public DateBuilder addMonths(int months) {
		add(Calendar.MONTH, months);
		return this;
	}
	
	public DateBuilder addYears(int years) {
		add(Calendar.YEAR, years);
		return this;
	}
	
	public DateBuilder setMilli(int milli) {
		set(Calendar.MILLISECOND, milli);
		return this;
	}
	
	public DateBuilder setSecond(int second) {
		set(Calendar.SECOND, second);
		return this;
	}
	
	public DateBuilder setMinute(int minute) {
		set(Calendar.MINUTE, minute);
		return this;
	}
	
	public DateBuilder setHour(int hour) {
		set(Calendar.HOUR, hour);
		return this;
	}
	
	public DateBuilder setDay(int day) {
		set(Calendar.DATE, day);
		return this;
	}
	
	public DateBuilder setMonths(int months) {
		set(Calendar.MONTH, months);
		return this;
	}
	
	public DateBuilder setYear(int year) {
		set(Calendar.YEAR, year);
		return this;
	}
	
	public DateBuilder minusDate(Date date) {
		this.calendar.setTimeInMillis(this.calendar.getTimeInMillis() - date.getTime());
		return this;
	}
	
	public DateBuilder minusDate(String dateString) {
		Date tempDate = DateUtil.parseDate(dateString);
		this.calendar.setTimeInMillis(this.calendar.getTimeInMillis() - tempDate.getTime());
		return this;
	}
	
	public DateBuilder plusDate(Date date) {
		this.calendar.setTimeInMillis(this.calendar.getTimeInMillis() + date.getTime());
		return this;
	}
	
	public DateBuilder plusDate(String dateString) {
		Date tempDate = DateUtil.parseDate(dateString);
		this.calendar.setTimeInMillis(this.calendar.getTimeInMillis() + tempDate.getTime());
		return this;
	}
	
	public Date toDate() {
		return this.calendar.getTime();
	}
	
	public Calendar toCalendar() {
		return this.calendar;
	}
	
	@Override
	public String toString() {
		return desiredFormat.format(this.calendar.getTime());
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println((new DateBuilder()).fromDate(DateUtil.parseDate("08072011")).toFormat("yyyy:MM:dd-hh:mm:ss.SSSS").addDays(5).addDays(30).setDay(5).toString());
	}
}
