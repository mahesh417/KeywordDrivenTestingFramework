package com.qualitestgroup.util.reporting;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;

import com.qualitestgroup.kdt.KDTDriver;
import com.sun.security.auth.module.NTSystem;

/**
 * Generates HTML layout to be used with test cases
 * 
 * @author Neo
 */
public class HTMLLayout extends Layout {
	protected final int BUF_SIZE = 256;
	protected final int MAX_CAPACITY = 1024;
	static String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;"; // Indenter
	String title = "Automation Test Log";

	private static Date lastTime;
	private static long totalTime = 0;
	private static int tfi = 0; // test file index
	private static int tdi = 0; // test data index
	private static int tsi = 0; // test step index

	// Output buffer appended to when format() is invoked
	private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

	/**
	 * A string constant used in naming the option for setting the the location
	 * information flag. Current value of this string constant is
	 * <b>LocationInfo</b>.
	 * 
	 * <p>
	 * Note that all option keys are case sensitive.
	 */
	public static final String LOCATION_INFO_OPTION = "LocationInfo";

	/**
	 * A string constant used in naming the option for setting the the HTML
	 * document title. Current value of this string constant is <b>Title</b>.
	 */
	public static final String TITLE_OPTION = "Title";

	/**
	 * Show Time column by default
	 */
	private boolean showTime = true;

	/**
	 * Default header for Time column in HTML table
	 */
	private String timeHeader = "<th>Time</th>";

	/**
	 * Default header for Message column in HTML table
	 */
	private String messageHeader = "<th>Open <a onclick='openAll()' href='#'>all</a><br />"
+ "Close <a onclick='closeAll()' href='#'>all</a></th>" + "<th colspan=\"2\">Test Scenario</th>"
+"<th>Test Case </th>" + "<th>Keywords / Test Steps</th>" + "<th>Error Message</th>" + "<th>Result</th>"
+ "<th>Comments</th>";

	private String version = KDTDriver.REVISION;

	void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
		if (s != null) {
			int len = s.length;

			if (len == 0)
				return;

			sbuf.append(Transform.escapeTags(s[0]));
			sbuf.append(Layout.LINE_SEP);

			for (int i = 1; i < len; i++) {
				sbuf.append(TRACE_PREFIX);
				sbuf.append(Transform.escapeTags(s[i]));
				sbuf.append(Layout.LINE_SEP);
			}
		}
	}

	/**
	 * Returns format and time stamp
	 */
	@Override
	public String format(LoggingEvent event) {
		// use valueOf to prevent null pointer exceptions
		String eventMethod = String.valueOf(event.getProperty("event"));

		String toggle = "";

		// If exceeds MAX_CAPACITY, resize it
		if (sbuf.capacity() > MAX_CAPACITY)
			sbuf = new StringBuffer(BUF_SIZE);
		else
			sbuf.setLength(0);

		String tf = "tf" + tfi;
		String td = "td" + tdi;
		String ts = "ts" + tsi;

		if (eventMethod.equals("startUC")) {
			toggle = tf;
			sbuf.append("<tr id='" + tf + "'>" + Layout.LINE_SEP);
		} else if (eventMethod.equals("passUC") || eventMethod.equals("failUC")) {
			sbuf.append("<tr id='~" + tf + "'>>" + Layout.LINE_SEP);
			tfi++;
		} else if (eventMethod.equals("startScenario")) {
			toggle = td;
			sbuf.append("<tr class=\"" + tf + " close-hide-temp\" ");
			sbuf.append("data-thisclass='" + tf + "' ");
			sbuf.append("id='" + td + "' ");
			sbuf.append(">");
			sbuf.append(Layout.LINE_SEP);
		} else if (eventMethod.equals("passScenario") || eventMethod.equals("failScenario")
				|| eventMethod.equals("warnScenario")) {
			sbuf.append("<tr class=\"" + tf + " close-hide-temp\" ");
			sbuf.append("data-thisclass='" + tf + "' ");
			sbuf.append("id='~" + td + "' ");
			sbuf.append(">");
			sbuf.append(Layout.LINE_SEP);
			tdi++;
		} else if (eventMethod.equals("passTC") || eventMethod.equals("warnTC") || eventMethod.equals("failTC")
				|| eventMethod.equals("untestableTC")) {
			toggle = "ts" + ++tsi;
			sbuf.append("<tr class=\"" + tf + " " + td + " close-hide-temp\" ");
			sbuf.append("data-thisclass='" + td + "' ");
			sbuf.append("id='" + toggle + "' ");
			sbuf.append(">");
			sbuf.append(Layout.LINE_SEP);
		} else if (eventMethod.equals("debug")) {
			sbuf.append("<tr class=\"" + tf + " " + td + " " + ts + " close-hide-temp\" ");
			sbuf.append("data-thisclass='" + ts + "' ");
			sbuf.append(">");
			sbuf.append(Layout.LINE_SEP);

		}

		// Display's Time Stamp
		if (showTime) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

			Date curr = new Date(event.timeStamp);
			String timestamp;
			if (lastTime == null) {
				timestamp = "Started: " + formatter.format(curr);
				try {
					NTSystem x = new NTSystem();
					timestamp = timestamp + "<br>Run by: " + x.getDomain() + "\\" + x.getName();
				} catch (NoClassDefFoundError e) {
					timestamp = timestamp + "<br>Run by: Unknown user.";
				}
			} else {
				long totalms = curr.getTime() - lastTime.getTime();
				totalTime += totalms;
				timestamp = "Step: " + formatTime(totalms) + "<BR>" + "Total: " + formatTime(totalTime);
			}
			lastTime = curr;
			sbuf.append("<td>");
			sbuf.append(timestamp);
			sbuf.append("</td>" + Layout.LINE_SEP);

		} else {
			sbuf.append("<td></td>" + Layout.LINE_SEP);
		}

		if (toggle.isEmpty()) {
			sbuf.append("<td></td>" + Layout.LINE_SEP);
		} else {
			// toggle button
			sbuf.append("<td class='tog-off-temp' id='" + toggle + "'><a ");
			sbuf.append("onclick=\"tog('" + toggle + "');");
			sbuf.append("getClassList(this.parentNode).toggle('tog-on');");
			sbuf.append("getClassList(this.parentNode).toggle('tog-off');\" href='#" + toggle + "'></a></td>");
			sbuf.append(Layout.LINE_SEP);
		}
		sbuf.append(event.getRenderedMessage());
		sbuf.append("</tr>" + Layout.LINE_SEP);

		return sbuf.toString();
	}

	private String formatTime(long millis) {
		long ms = millis % 1000;
		long ss = (millis / 1000) % 60;
		long mm = (millis / (1000 * 60)) % 60;
		long hh = (millis / (1000 * 60 * 60));
		return String.format("%02d:%02d:%02d.%03d", hh, mm, ss, ms);
	}

	/**
	 * Returns the content type output by this layout, i.e "text/html".
	 */
	@Override
	public String getContentType() {
		return "text/html";
	}

	/**
	 * Returns the appropriate HTML footers.
	 */
	@Override
	public String getFooter() {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append("</table>" + Layout.LINE_SEP);
		sbuf.append("<br>" + Layout.LINE_SEP);
		sbuf.append("</body></html>");

		return sbuf.toString();
	}

	/**
	 * Returns appropriate HTML headers.
	 */
	@Override
	public String getHeader() {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append(
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
						+ Layout.LINE_SEP);
		sbuf.append("<html>" + Layout.LINE_SEP);
		sbuf.append("<head>" + Layout.LINE_SEP);
		sbuf.append("<title>" + title + "</title>" + Layout.LINE_SEP);
		// CSS
		sbuf.append("<style type='text/css'>" + Layout.LINE_SEP);
		sbuf.append("body, table {font-family: arial,sans-serif; font-size: x-small;}" + Layout.LINE_SEP);
		sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
		sbuf.append("th a {color: #FFFFFF;}" + Layout.LINE_SEP);
		sbuf.append(".open-show {display: table-row;}" + Layout.LINE_SEP);
		sbuf.append(".open-hide, .close-hide {display: none;}" + Layout.LINE_SEP);
		sbuf.append(".tog-off a:before {content:'[+]';}" + Layout.LINE_SEP);
		sbuf.append(".tog-on a:before {content:'[-]';}" + Layout.LINE_SEP);
		sbuf.append(".tog-off {font-size: x-large; text-align: center;}" + Layout.LINE_SEP);
		sbuf.append(".tog-on {font-size: x-large; text-align: center;}" + Layout.LINE_SEP);
		sbuf.append("</style>" + Layout.LINE_SEP);
		// JS
		sbuf.append("<script>" + Layout.LINE_SEP);
		// Get class lit of an element
		sbuf.append("function getClassList(elt){" + Layout.LINE_SEP);
		sbuf.append("var classes=elt.className;" + Layout.LINE_SEP);
		sbuf.append("var ret=new Object();" + Layout.LINE_SEP);
		sbuf.append("ret.elt=elt;" + Layout.LINE_SEP);
		sbuf.append("ret.classList=classes.split(' ');" + Layout.LINE_SEP);
		sbuf.append("ret.contains=function(cls){" + Layout.LINE_SEP);
		sbuf.append("return this.classList.indexOf(cls)>=0;};" + Layout.LINE_SEP);
		sbuf.append("ret.remove=function(cls){" + Layout.LINE_SEP);
		sbuf.append("var newClassList=[];" + Layout.LINE_SEP);
		sbuf.append("while(this.classList.length>0){" + Layout.LINE_SEP);
		sbuf.append("var tmp=this.classList.pop();" + Layout.LINE_SEP);
		sbuf.append("if(tmp!=cls)" + Layout.LINE_SEP);
		sbuf.append("newClassList.push(tmp);}" + Layout.LINE_SEP);
		sbuf.append("this.classList=newClassList;" + Layout.LINE_SEP);
		sbuf.append("this.elt.className=this.classList.join(' ');};" + Layout.LINE_SEP);
		sbuf.append("ret.add=function(cls){" + Layout.LINE_SEP);
		sbuf.append("this.classList.push(cls);" + Layout.LINE_SEP);
		sbuf.append("this.elt.className=this.classList.join(' ');};" + Layout.LINE_SEP);
		sbuf.append("ret.toggle=function(cls){" + Layout.LINE_SEP);
		sbuf.append("if(this.contains(cls))" + Layout.LINE_SEP);
		sbuf.append("this.remove(cls);" + Layout.LINE_SEP);
		sbuf.append("else" + Layout.LINE_SEP);
		sbuf.append("this.add(cls);};" + Layout.LINE_SEP);
		sbuf.append("ret.length=ret.classList.length;" + Layout.LINE_SEP);
		sbuf.append("ret.item=function(i) {" + Layout.LINE_SEP);
		sbuf.append("return this.classList[i];};" + Layout.LINE_SEP);
		sbuf.append("return ret;}" + Layout.LINE_SEP);
		// Toggle a row's subrows on/off
		sbuf.append("function tog(cls){" + Layout.LINE_SEP);
		sbuf.append("var rows=document.getElementsByClassName(cls);" + Layout.LINE_SEP);
		sbuf.append("for(var r=0;r<rows.length;++r){" + Layout.LINE_SEP);
		sbuf.append("var row=rows[r];" + Layout.LINE_SEP);
		sbuf.append("var clslist = getClassList(row);" + Layout.LINE_SEP);
		sbuf.append("if(row.getAttribute('data-thisclass')==cls){" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('open-show');" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('close-hide');}" + Layout.LINE_SEP);
		sbuf.append("else{" + Layout.LINE_SEP);
		sbuf.append("if(clslist.contains('open-show')){" + Layout.LINE_SEP);
		sbuf.append("clslist.remove('open-show');" + Layout.LINE_SEP);
		sbuf.append("clslist.add('open-hide');}" + Layout.LINE_SEP);
		sbuf.append("else if(clslist.contains('open-hide')){" + Layout.LINE_SEP);
		sbuf.append("var disp=true;" + Layout.LINE_SEP);
		sbuf.append("for(var i=0;i<clslist.length;i++){" + Layout.LINE_SEP);
		sbuf.append("var item=document.getElementById(clslist.item(i));" + Layout.LINE_SEP);
		sbuf.append("if(getClassList(item).contains('close-hide')){" + Layout.LINE_SEP);
		sbuf.append("disp=false;" + Layout.LINE_SEP);
		sbuf.append("break;}}" + Layout.LINE_SEP);
		sbuf.append("if(disp){" + Layout.LINE_SEP);
		sbuf.append("clslist.remove('open-hide');" + Layout.LINE_SEP);
		sbuf.append("clslist.add('open-show');}}}}}" + Layout.LINE_SEP);
		// Open all rows
		sbuf.append("function openAll(){" + Layout.LINE_SEP);
		sbuf.append("var closed=document.getElementsByClassName('tog-off');" + Layout.LINE_SEP);
		sbuf.append("while(closed.length>0){" + Layout.LINE_SEP);
		sbuf.append("closed[0].firstElementChild.click();}}" + Layout.LINE_SEP);
		// Close all rows
		sbuf.append("function closeAll(){" + Layout.LINE_SEP);
		sbuf.append("openAll();" + Layout.LINE_SEP);
		sbuf.append("var open=document.getElementsByClassName('tog-on');" + Layout.LINE_SEP);
		sbuf.append("while(open.length>0){" + Layout.LINE_SEP);
		sbuf.append("open[open.length-1].firstElementChild.click();}}" + Layout.LINE_SEP);
		// Hide all on load
		sbuf.append("window.onload = function(){" + Layout.LINE_SEP);
		sbuf.append("var close=document.getElementsByClassName('close-hide-temp');" + Layout.LINE_SEP);
		sbuf.append("while(close.length>0){" + Layout.LINE_SEP);
		sbuf.append("clslist=getClassList(close[0]);" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('close-hide-temp');" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('close-hide');}" + Layout.LINE_SEP);
		sbuf.append("var togs=document.getElementsByClassName('tog-off-temp');" + Layout.LINE_SEP);
		sbuf.append("while(togs.length>0){" + Layout.LINE_SEP);
		sbuf.append("var elts=document.getElementsByClassName(togs[0].id);" + Layout.LINE_SEP);
		sbuf.append("var clslist=getClassList(togs[0]);" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('tog-off-temp');" + Layout.LINE_SEP);
		sbuf.append("if(elts.length>0){" + Layout.LINE_SEP);
		sbuf.append("clslist.toggle('tog-off');}}};" + Layout.LINE_SEP);
		sbuf.append("</script>" + Layout.LINE_SEP);
		sbuf.append("</head>" + Layout.LINE_SEP);
		// HTML body
		sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);
		sbuf.append("<span id='open-hide'></span><span id='open-show'></span><span id='close-hide'></span>"
				+ Layout.LINE_SEP);
		sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
		sbuf.append("Log session start time " + new java.util.Date() + "<div style='float: right;'>v" + version
				+ "</div><br>" + Layout.LINE_SEP);
		sbuf.append("<br>" + Layout.LINE_SEP);
		sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">"
				+ Layout.LINE_SEP);
		sbuf.append("<tr>" + Layout.LINE_SEP);
		sbuf.append("<th>Time</th>" + Layout.LINE_SEP);
		sbuf.append(messageHeader + Layout.LINE_SEP);
		sbuf.append("</tr>" + Layout.LINE_SEP);
		return sbuf.toString();
	}

	/**
	 * Returns the current value of the <b>MessageHeader</b> option.
	 */
	public String getMessageHeader() {
		return messageHeader;
	}

	/**
	 * Returns the current value of the <b>ShowTime</b> option.
	 */
	public boolean getShowTime() {
		return showTime;
	}

	/**
	 * Returns the current value of the <b>TimeHeader</b> option.
	 */
	public String getTimeHeader() {
		return timeHeader;
	}

	/**
	 * Returns the current value of the <b>Title</b> option.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The HTML layout handles the throwable contained in logging events. Hence,
	 * this method return <code>false</code>.
	 */
	@Override
	public boolean ignoresThrowable() {
		return false;
	}

	/**
	 * The <b>MessageHeader</b> option takes a String value. This option sets
	 * the table header for the message column in the HTML table layout.
	 * <p/>
	 * <p>
	 * Defaults to '&lt;th&gt;Message&lt;/th&gt;'.
	 */
	public void setMessageHeader(String msgHeader) {
		this.messageHeader = msgHeader;
	}

	public void setShowTime(boolean flag) {
		showTime = flag;
	}

	/**
	 * The <b>TimeHeader</b> option takes a String value. This option sets the
	 * table header for the time column in the HTML table layout.
	 * <p/>
	 * <p>
	 * Defaults to '&lt;th&gt;Time&lt;/th&gt;'.
	 */
	public void setTimeHeader(String timeHeader) {
		this.timeHeader = timeHeader;
	}

	/**
	 * The <b>Title</b> option takes a String value. This option sets the
	 * document title of the generated HTML document.
	 * 
	 * <p>
	 * Defaults to 'Log4J Log Messages'.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * No options to activate.
	 */
	@Override
	public void activateOptions() {
	}
}
