package com.qualitestgroup.util.reporting;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggingEvent;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qualitestgroup.kdt.KDTDriver;
import com.qualitestgroup.util.fileio.FileTools;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.qualitestgroup.keywords.common.Browser.Launch;

/**
 * Transfers data from data sheet to HTML table and logs success/failure, color
 * codes them, and returns test case comments
 * 
 * @author Neo
 */
public class EventCase {
	// Static variable for colors
	final private static String SUCCESS = "#00FF00";
	final private static String FAIL = "#FF0000";
	final private static String WARN = "#FFFF00";
	final private static String START = "#CCCCCC";
	final private static String STOP="#CCCCCC";
	final private static String WAIT="#C2C2F0";
	final private static String DEBUG_CL="#4169E1";
	final private static String SUMMARY_CL="#336699";
	final private static String SUMMARY_TXTCL="#FFFFFF";
	final private static String TABLE_BORDER="224466";
	public static List<String> failScrList= new ArrayList<String>();

	private Logger logger = Logger.getRootLogger();
	private Logger testLogger = Logger.getLogger("testLog");
	private Logger exceptionLogger = Logger.getLogger("exceptionLog");

	private FileAppender mainApd;
	private FileAppender exceptionApd;
	private String exceptionBaseFileName;
	private int exceptionCount = 0;
	private String exceptionFileDate;
	public FileAppender testApd;
	private String appName;
	private String loggingDir;
	private boolean debugToMainLog = true;
	private String reportFile = "";
	private String logFile = "";
	public static String scenario = "";
	private String fileDate = "";

	public boolean screenshot = true;

	public static final String TEST_PATH = System.getProperty("user.dir");
	public static boolean preconditionInfoMessage = false; // Make it true if
															// preconditions
															// message is
															// required.
	public static ExtentHtmlReporter extentReporter;
	public static ExtentReports report;
	public static ExtentTest extentlogger;
	public static String graphReport = "";
	
	/**
	 * 
	 * 
	 * @param appName
	 * @param loggingDir
	 */
	public EventCase(String appName, String loggingDir) {
		this.appName = appName;
		this.loggingDir = loggingDir;

		// Set up logging configuration from property file.
		PropertyConfigurator.configure(
				EventCase.class.getResourceAsStream("/com/qualitestgroup/util/reporting/logging.properties"));

		// Get the current date and time.
		fileDate = getDateTime();

		// Get the main log appender and override the log file name w/ the
		// detailed name.
		mainApd = (FileAppender) logger.getAppender("main");
		logFile = loggingDir + "debug//" + fileDate + "_" + appName + "_" + mainApd.getFile();
		mainApd.setFile(logFile);
		mainApd.activateOptions();

		// Get the exception log appender base file name for use when logging
		// exceptions.
		exceptionApd = (FileAppender) exceptionLogger.getAppender("exception");
		exceptionBaseFileName = exceptionApd.getFile();

		// Disable exception log additivity.
		exceptionLogger.setAdditivity(false);

		// Disable selenium's crap-ton of debug messages
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");

		// Get the test log appender and override the log file name w/ the
		// detailed name.
		testApd = (FileAppender) testLogger.getAppender("test");
		reportFile = loggingDir + appName + "_" + fileDate + "_" + testApd.getFile();
		testApd.setFile(reportFile);
		testApd.activateOptions();
		// Disable test log additivity.
		testLogger.setAdditivity(false);
		//Extent Report
				graphReport = loggingDir + appName + "_" + fileDate + "_" + "graphReport.html";
				report = new ExtentReports(System.getProperty("user.dir")+"/"+graphReport);
	}

	public void closeLoggers() {
		mainApd.close();
		exceptionApd.close();
		testApd.close();

		// delete extraneous files
		try {
			File f = new File("TestLog.html");
			f.delete();
			f = new File("ExceptionLog.log");
			f.delete();
			f = new File("Debug.log");
			f.delete();
		} catch (Exception e) {
		}
	}

	public String convertStackToString(Throwable t) {
		StringWriter stringWritter = new StringWriter();
		PrintWriter printWritter = new PrintWriter(stringWritter, true);

		t.printStackTrace(printWritter);
		printWritter.flush();
		stringWritter.flush();

		return stringWritter.toString();
	}

	public void debug(String message) {
		logger.debug(message);

		if (debugToMainLog) {
			//String msg = "<td></td><td>DEBUG</td><td>DEBUG MESSAGE</td><td>DEBUG</td><td>" + message + "</td>";
			//String msg ="<td></td><td><font color=DEBUG1>DEBUG</font></td><td>DEBUG MESSAGE</td><td>DEBUG</td><td>" + message + "</td>";
			//String msg ="<td></td><td><font color=" + "/" + "DEBUG_CL" + "/"  + ">DEBUG</font></td><td>DEBUG MESSAGE</td><td>DEBUG</td><td>" + "<font color=DEBUG_CL>"+message +"</font></td>";
			//String msg ="<td></td><td><font color=" + "\"" + "DEBUG_CL" + "\""  + ">DEBUG</font></td><td>DEBUG MESSAGE</td><td>DEBUG</td><td>" + "<font color="+ "\"" + "DEBUG_CL" + "\""  +">"+ message +"</font></td>";
			//String msg ="<td></td><td><font color=" + "\"" + DEBUG_CL + "\""  + ">DEBUG</font></td><td>DEBUG MESSAGE</td><td>DEBUG</td><td>" + "<font color="+ "\"" + DEBUG_CL + "\""  +">"+ message +"</font></td>";
			String msg ="<td colspan=\"3\"></td><td>" + "<font color="+ "\"" + DEBUG_CL + "\">DEBUG</font></td><td colspan=\"3\">" + "<font color="+ "\"" + DEBUG_CL + "\">"+ message +"</font></td>";
            LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
			log.setProperty("event", "debug");
			testLogger.callAppenders(log);
		}
	}

	/**
	 * This will create a new time stamped filename for each exception thrown.
	 * Each exception file will be closed off when the logging is finished.
	 * These individual files can be linked into the test log.
	 * 
	 * @param message
	 * @param exception
	 * @return
	 */
	public String error(String message, String exception) {
		// Reset the exception log appender w/ the detailed date-time name.
		String fileDate = getDateTime();
		String exceptionApdFileName;

		// Check if this specific filename has already been used... if so,
		// update the
		// Exception count and add it to the file name. If not, reset the
		// exception count,
		// do not use it, and update the file date.
		if (fileDate.equals(exceptionFileDate)) {
			exceptionCount++;
			exceptionApdFileName = fileDate + "_" + exceptionCount + "_" + appName + "_" + exceptionBaseFileName;
		} else {
			exceptionApdFileName = fileDate + "_" + appName + "_" + exceptionBaseFileName;
			exceptionFileDate = fileDate;
			exceptionCount = 0;
		}

		exceptionApd = (FileAppender) exceptionLogger.getAppender("exception");

		exceptionApd.setFile(loggingDir + "exceptions//" + exceptionApdFileName);
		exceptionApd.activateOptions();

		// Log exception
		exceptionLogger.error(message + " - " + exception);

		return exceptionApdFileName;
	}

	/**
	 * Fail event for test case
	 * 
	 * @param tcID
	 *            - test case ID
	 * @param tcDesc
	 *            - test case description
	 * @param tcSteps
	 *            - test case steps
	 * @param tcResults
	 *            - test case results
	 * @param comments
	 */
	public void failTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments) {
		String shot = screenshot ? " - <a href=\"screenshots/" + screenshot() + "\">Screen Shot</a>" : "";

		String msg ="<td colspan=\"2\">" + tcID + "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults
				+ "</td><td bgcolor=" + FAIL + ">FAIL</td><td>" + comments + shot + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "failTC");
		testLogger.callAppenders(log);
		String scrName = screenshot();
		failScrList.add(scrName);
		extentlogger.log(LogStatus.FAIL, tcSteps+" is Failed<td><td>"+"<a href=\"screenshots/"+scrName+"\">Screen Shot</a>");
	}
	
	
	

	/**
	 * Fail event for test case
	 * 
	 * @param tcID
	 *            - test case ID
	 * @param tcDesc
	 *            - test case description
	 * @param tcSteps
	 *            - test case steps
	 * @param tcResults
	 *            - test case results
	 * @param comments
	 * @param fileName
	 *            - name of file
	 */
	public void failTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments, String fileName) {
		String shot = screenshot ? " - <a href=\"screenshots/" + screenshot() + "\">Screen Shot</a>" : "";

		String msg = "<td colspan=\"2\">" + tcID + "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults
				+ "</td><td bgcolor=" + FAIL + ">FAIL</td><td><a href=\"" + logFile + "\">" + comments + shot + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "failTC");
		testLogger.callAppenders(log);
		String scrName = screenshot();
		failScrList.add(scrName);
		extentlogger.log(LogStatus.FAIL, tcSteps+" is Failed<td><td>"+"<a href=\"screenshots/"+scrName+"\">Screen Shot</a>");
	}

	/**
	 * Fail event for scenario
	 * 
	 * @param scenarioID
	 * @param scenarioDesc
	 * @param scenarioSteps
	 * @param scenarioResults
	 * @param comments
	 */
	public void failScenario(String scenarioID, String scenarioDesc, String scenarioSteps, String scenarioResults,
			String comments) {

		String msg ="<td colspan=\"2\" bgcolor=" + START + ">" + scenarioID + "</td><td bgcolor="
				+ STOP + ">" + scenarioDesc + "</td><td bgcolor=" + STOP + ">" + scenarioSteps + "</td><td bgcolor="
				+ STOP + ">" + scenarioResults + "</td><td bgcolor=" + STOP + ">STOPPED</td><td bgcolor=" + STOP + ">"
				+ comments + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "failScenario");
		testLogger.callAppenders(log);
	}

	/**
	 * Fail event for use case
	 * 
	 * @param ucID
	 *            - use case ID
	 * @param ucDesc
	 *            - use case description
	 * @param comments
	 */
	public void failUC(String ucID, String ucDesc, String comments) {

		String msg = "<td colspan=\"2\">" + ucID + "</td><td colspan=\"3\">" + ucDesc + "<td bgcolor=" + FAIL
				+ ">FAIL</td><td>" + comments + "</td>";
		
		String summary="<html>" +"<head>" +"<style>" +"table,th,tr, td {" +"  border: 1px solid black;" +"text-align: center;"+"}" +
				"</style>" +"</head>" +"<body>" +"" +"<table align=\"center\" border= \"3\">"+"<tr>" +"<th colspan ='5'<text-align: center;><font size=\" \"3\" \">SUMMARY</th>" +
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Total Test Scenarios</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseRan+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Success </td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSuccesses+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Failed </td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseFails+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Skipped </td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSkips+"</td>"+"</tr>"+
				"</table>" +"</body>" +"</html>";
		
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		LoggingEvent summarylog = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO,summary, null);
		log.setProperty("event", "failUC");
		testLogger.callAppenders(log);
		testLogger.callAppenders(summarylog);
	}

	public String getFileName() {
		return reportFile;
	}

	public void info(String infoType, String message) {
		if (preconditionInfoMessage) {
			testLogger
					.info("<td colspan=\"2\">INFO</td><td colspan=\"4\">" + infoType + "</td><td>" + message + "</td>");
		}
	}

	public void otherException(String message, String exception) {
		
		String exceptionFile = error(message, exception);

		
			String msg = "<td>.</td><td>NA</td><td>Non TC/UC Exception Occurred</td><td>.</td><td>.</td>" + "<td bgcolor="
				+ FAIL + ">FAIL</td><td><a href=\"exceptions/" + exceptionFile + "\">" + message + "</a></td>";
			String summary="<html>" +"<head>" +"<style>" +"table,th,tr, td {" +"  border: 1px solid black;" +"text-align: center;"+"}" +
					"</style>" +"</head>" +"<body>" +"" +"<table align=\"center\" border= \"3\">"+"<tr>" +"<th colspan ='5'<text-align: center;><font size=\" \"3\" \">SUMMARY</th>" +
					"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Total Test Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseRan+"</td>"+"</tr>"+
					"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Success Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSuccesses+"</td>"+"</tr>"+
					"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Failed Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseFails+"</td>"+"</tr>"+
					"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Skipped Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSkips+"</td>"+"</tr>"+
					"</table>" +"</body>" +"</html>";
		   LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		   LoggingEvent summarylog = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, summary, null);
		   log.setProperty("event", "otherException");
		   testLogger.callAppenders(log);
		   testLogger.callAppenders(summarylog);
		
	}
	/**
	 * Pass event for test case
	 * 
	 * @param tcID
	 *            - test case ID
	 * @param tcDesc
	 *            - test case description
	 * @param tcSteps
	 *            - test case steps
	 * @param tcResults
	 *            - test case results
	 * @param comments
	 */
	/*
	public void passTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments) {

		String msg = "<td>.</td><td>" + tcID + "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults
				+ "</td><td bgcolor=" + SUCCESS + ">PASS</td><td>" + comments + "</td>";

		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);

		log.setProperty("event", "passTC");

		testLogger.callAppenders(log);
	}

	*/
	public void passTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments) {

		
		 if(!tcSteps.contains("Wait"))
		 { String msg = "<td colspan=\"2\">" + tcID +
		 "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults +
		 "</td><td bgcolor=" + SUCCESS + ">PASS</td><td>" + comments + "</td>";
		 LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger,
				 Level.INFO, msg, null); log.setProperty("event", "passTC");
				 testLogger.callAppenders(log);
				 extentlogger.log(LogStatus.PASS, tcSteps+" is Passed<td><td>"+"<a href=\"screenshots/"+screenshot()+"\">Screen Shot</a>");
		 
		  }else {
			  String msg = "<td colspan=\"2\" bgcolor=" + WAIT + ">" + tcID + "</td><td bgcolor="
						+ WAIT + ">" + tcDesc + "</td><td bgcolor=" + WAIT + ">" + tcSteps
						+ "</td><td bgcolor=" + WAIT + ">" + tcResults + "</td><td bgcolor=" + WAIT
						+ "></td><td bgcolor=" + WAIT + ">" + comments + "</td>";
						 
						 
						 LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger,
						 Level.INFO, msg, null); log.setProperty("event", "passTC");
						 testLogger.callAppenders(log);
						
						 extentlogger.log(LogStatus.PASS, tcSteps+" is Passed<td><td>"+"<a href=\"screenshots/"+screenshot()+"\">Screen Shot</a>");
		  }
		 
		
	  }
	  
	 
		
		
	//}

	/**
	 * Pass event for scenario
	 * 
	 * @param scenarioID
	 * @param scenarioDesc
	 * @param scenarioSteps
	 * @param scenarioResults
	 * @param comments
	 */
	public void passScenario(String scenarioID, String scenarioDesc, String scenarioSteps, String scenarioResults,
			String comments) {
		String msg = "<td colspan=\"2\" bgcolor=" + START + ">" + scenarioID + "</td><td bgcolor="
				+ STOP + ">" + scenarioDesc + "</td><td bgcolor=" + STOP + ">" + scenarioSteps
				+ "</td><td bgcolor=" + STOP + ">" + scenarioResults + "</td><td bgcolor=" + STOP
				+ ">STOPPED</td><td bgcolor=" + STOP + ">" + comments + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "passScenario");
		testLogger.callAppenders(log);
	}

	/**
	 * Pass event for use case
	 * 
	 * @param ucID
	 *            - use case ID
	 * @param ucDesc
	 *            - use case description
	 * @param comments
	 */
	public void passUC(String ucID, String ucDesc, String comments) {
		
		String msg = "<td colspan=\"2\">" + ucID + "</td><td colspan=\"3\">" + ucDesc + "<td bgcolor=" + SUCCESS
				+ ">PASS</td><td>" + comments + "</td>";
		
		String summary="<html>" +"<head>" +"<style>" +"table,th,tr, td {" +"  border: 1px solid black;" +"text-align: center;"+"}" +
				"</style>" +"</head>" +"<body>" +"" +"<table align=\"center\" border= \"3\">"+"<tr>" +"<th colspan ='5'<text-align: center;><font size=\" \"3\" \">SUMMARY</th>" +
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Total Test Scenarios</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseRan+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Successful Scenarios</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSuccesses+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Failed Scenarios</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseFails+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Skipped Scenarios</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSkips+"</td>"+"</tr>"+
				"</table>" +"</body>" +"</html>";
		
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		LoggingEvent summarylog = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, summary, null);
		//LoggingEvent emptylog = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, empty, null);
		log.setProperty("event", "passUC");
		testLogger.callAppenders(log);
		//testLogger.callAppenders(emptylog);
		testLogger.callAppenders(summarylog);
	}

	/**
	 * Captures and saves a screen shot at return
	 * 
	 * @return a screenshot
	 */
	public String screenshot() {
		WebDriver screenshotdriver = Launch.driver;
		String screenshotName = "shot_" + getDateTime() + ".jpg";
		File fileLocation = new File(loggingDir + "screenshots//" + screenshotName);
		if (!(screenshotdriver == null)) {
			File src = ((TakesScreenshot) screenshotdriver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(src, fileLocation);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} 
		return screenshotName;
	}

	public void setDebugToMainLog(boolean set) {
		debugToMainLog = set;
	}

	/**
	 * Skip event for test case
	 * 
	 * @param tcID
	 *            - test case ID
	 * @param tcDesc
	 *            - test case description
	 * @param tcSteps
	 *            - test case steps
	 * @param tcResults
	 *            - test case results
	 * @param comments
	 */
	public void warnTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments) {
		String msg = "<td colspan=\"2\" bgcolor=" + WARN + ">" + tcID + "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults
				+ "</td><td bgcolor=" + WARN + ">WARNING</td><td>" + comments + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "warnTC");
		testLogger.callAppenders(log);
		extentlogger.log(LogStatus.WARNING, tcSteps+" is showing Warning<td><td>"+"<a href=\"screenshots/"+screenshot()+"\">Screen Shot</a>");
	}

	public void startTC(String ucID, String ucDesc) {

	}

	/**
	 * Start event for use case
	 * 
	 * @param ucID
	 *            - use case ID
	 * @param ucDesc
	 *            - use case description
	 */
	public void startUC(String ucID, String ucDesc) {
		String msg = "<td colspan=\"2\">Test File : " + ucID + "</td><td colspan=\"3\">" + ucDesc
				+ "</td><td></td><td>Test started.</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "startUC");
		testLogger.callAppenders(log);
	}

	/**
	 * Start event for scenario
	 * 
	 * @param scenarioID
	 * @param scenarioDesc
	 * @param scenarioSteps
	 * @param scenarioResults
	 * @param comments
	 */
	public void startScenario(String scenarioID, String scenarioDesc, String scenarioSteps, String scenarioResults,
			String comments) {

		String msg = "<td colspan=\"2\" bgcolor=" + START + ">" + scenarioID + "</td><td bgcolor="
				+ START + ">" + scenarioDesc + "</td><td bgcolor=" + START + ">" + scenarioSteps + "</td><td bgcolor="
				+ START + ">" + scenarioResults + "</td><td bgcolor=" + START + ">STARTED</td><td bgcolor=" + START
				+ ">" + comments + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "startScenario");
		testLogger.callAppenders(log);

		scenario = scenarioID;
		extentlogger = report.startTest("TestScenario :<td>"+scenarioID);
	}

	/**
	 * Skipped event for scenario
	 * 
	 * @param scenarioID
	 * @param scenarioDesc
	 * @param scenarioSteps
	 * @param scenarioResults
	 * @param comments
	 */
	public void warnScenario(String scenarioID, String scenarioDesc, String scenarioSteps, String scenarioResults,
			String comments) {
		String msg = "<td bgcolor=" + WARN + ">.</td><td bgcolor=" + WARN + ">" + scenarioID + "</td><td bgcolor="
				+ WARN + ">" + scenarioDesc + "</td><td bgcolor=" + WARN + ">" + scenarioSteps + "</td><td bgcolor="
				+ WARN + ">" + scenarioResults + "</td><td bgcolor=" + WARN + ">WARNING</td><td bgcolor=" + WARN + ">"
				+ comments + "</td>";
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		log.setProperty("event", "warnScenario");
		testLogger.callAppenders(log);

		scenario = scenarioID;
	}

	/**
	 * Untestable event for test case
	 * 
	 * @param tcID
	 *            - test case ID
	 * @param tcDesc
	 *            - test case description
	 * @param tcSteps
	 *            - test case steps
	 * @param tcResults
	 *            - test case results
	 * @param comments
	 */
	public void untestableTC(String tcID, String tcDesc, String tcSteps, String tcResults, String comments) {

		String msg = "<td>.</td><td>" + tcID + "</td><td>" + tcDesc + "</td><td>" + tcSteps + "</td><td>" + tcResults
				+ "</td><td bgcolor=" + WARN + ">SKIPPED</td><td>" + comments + "</td>";
		
		String summary="<html>" +"<head>" +"<style>" +"table,th,tr, td {" +"  border: 1px solid black;" +"text-align: center;"+"}" +
				"</style>" +"</head>" +"<body>" +"" +"<table align=\"center\" border= \"3\">"+"<tr>" +"<th colspan ='5'<text-align: center;><font size=\" \"3\" \">SUMMARY</th>" +
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Total Test Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseRan+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Success Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSuccesses+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Failed Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseFails+"</td>"+"</tr>"+
				"</tr>" +"<tr>"+"<td style=width:50%><font size=\" \"3\" \">Skipped Cases</td>"+"<td><font size=\" \"3\" \">"+KDTDriver.caseSkips+"</td>"+"</tr>"+
				"</table>" +"</body>" +"</html>";
		
		LoggingEvent log = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, msg, null);
		LoggingEvent summarylog = new LoggingEvent("org.apache.log4j.Logger", testLogger, Level.INFO, summary, null);
		log.setProperty("event", "passUC");
		testLogger.callAppenders(log);
		testLogger.callAppenders(summarylog);
	}

	public static String getDateTime() {
		// Set up detailed log file name.
		Calendar now = Calendar.getInstance();
		String fileDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE)
				+ "_" + now.get(Calendar.HOUR_OF_DAY) + "h" + now.get(Calendar.MINUTE) + "m" + now.get(Calendar.SECOND)
				+ "s" + now.get(Calendar.MILLISECOND) + "ms";

		return fileDate;
	}

	public boolean copyReport(String location) {
		try {
			FileTools.copyFile(reportFile, location);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean copyLog(String location) {
		try {
			FileTools.copyFile(logFile, location);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getFileDate() {
		return fileDate;
	}

	public String getLoggingDir() {
		return loggingDir;
	}

	public void writeTestData(KDTDriver.TestRunData trd) {
		trd.LastReport = new File(reportFile).toURI().normalize().toString();
		trd.LastLog = new File(logFile).toURI().normalize().toString();

		System.out.println(trd.LastReport);
		System.out.println(trd.LastLog);
	}
}
