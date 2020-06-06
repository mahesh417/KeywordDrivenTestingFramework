package com.qualitestgroup.kdt;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.security.auth.module.NTSystem;
import com.qualitestgroup.kdt.exceptions.*;
import com.qualitestgroup.util.TemplateCreator;
import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataDriverFactory;
import com.qualitestgroup.util.datadriver.DataRow;
import com.qualitestgroup.util.datadriver.DataSource;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.util.logging.Logger.LogLevel;
import com.qualitestgroup.util.reporting.EventCase;
import com.qualitestgroup.util.robot.RobotThread;
import com.qualitestgroup.keywords.common.Browser.*;

import static com.qualitestgroup.kdt.KDTConstants.*;

/**
 * Main keyword-driven testing class. 
 * 
 * 
 * @author QualiTest
 *
 */
public class KDTDriver {

	private LinkedList<TestCase> testCases;

	private HashMap<String, Boolean> testResults;
	private HashMap<String, String[]> dependencies;
	private String testFilePath;
	public static final String REVISION;
	public static EventCase ev;
	private static RobotThread RT;
	private static boolean robot = true;
	private static String applicationUnderTestUrl = "";

	static {
		String temp = "0";
		try {		
			java.io.InputStream is = KDTDriver.class.getResourceAsStream("/REVISION");
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(is));
			temp = FileTools.readerToString(in);			
			in.close();
		} catch (Exception e) {
			try {
				java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader("./REVISION"));
				temp = FileTools.readerToString(in);			
				in.close();
			} catch (Exception e2) {}
		}
		finally {
			REVISION = temp;
		}
	}

	public static int totalCases, caseRan, caseSuccesses, caseFails, caseSkips;
	//private int totalCases, caseRan, caseSuccesses, caseFails, caseSkips;

	private static int forceLog = -1;

	/**
	 * Creates a KDTDriver from the given test file. 
	 * 
	 * The test file is read, and each test case marked to execute is parsed.
	 * 
	 * @param testFilePath The path of the test file to open.
	 * @throws KDTParseException If the test file is unable to be parsed.
	 */
	public KDTDriver(String testFilePath) throws KDTParseException
	{
		TestContext context = TestContext.getContext();
		NTSystem x = new NTSystem();
		context.log(LogLevel.info, "Run by: " + x.getDomain() + "\\" + x.getName());
		context.log(LogLevel.info, "Opening " + testFilePath);
		this.testFilePath = testFilePath;
		//senderDIFilePath=testFilePath;
		DataDriver testfile = DataDriverFactory.create(testFilePath, TEST_CASES);
		if(testfile == null)
		{
			context.log(LogLevel.error, "Error opening file " + testFilePath);
			throw new KDTParseException("");
		}
		testCases = new LinkedList<TestCase>();
		testResults = new HashMap<String, Boolean>();
		dependencies = new HashMap<String, String[]>();

		// Validate columns
		if(!testfile.hasColumns(caseReqColumns))
		{
			String[] missingCols = testfile.getMissingColumns(caseReqColumns);
			context.log(LogLevel.error, "Test file mising columns");
			throw new KDTParseException("Test file is missin columns: " + Arrays.toString(missingCols));
		}

		LinkedList<String> parseErrors = new LinkedList<String>();

		// Load test cases
		DataRow testCaseRow;
		while(testfile.hasNext())
		{
			testCaseRow = testfile.nextRow();			
			String testCase = testCaseRow.getItem(TEST_CASE);



			// Add if marked as execute
			if(testCaseRow.getItem(EXECUTE).equals(Y))
			{
				try { // try for KDTParseException
					String seqId = testCaseRow.getItem(SCEN_ID);

					// Throw error if sequence ID is blank.
					if(seqId.isEmpty())
					{
						throw new KDTParseException("Secenario ID for Test Case '" + testCase + "' is empty.");
					}

					// Parse log level
					int logLevel = parseLogLevel(testCaseRow.getItem(LOG_LEVEL));

					// Force log level if needed.
					logLevel = forceLog >= 0 ? forceLog : logLevel;

					context.setLog(logLevel);
					context.log(LogLevel.debug, "Loading test case '" + testCase + "'");

					// Load dependencies
					String deps = testCaseRow.getItem(DEPEND);
					if(deps == null || deps.equals(""))					
					{
						dependencies.put(testCase, new String[0]);
					}
					else
					{
						dependencies.put(testCase, deps.split(","));
					}


					String dataSrc = testCaseRow.getItem(DATA_SHEET);
					String stepSrc = testCaseRow.getItem(SCEN_SHEET);				
					DataSource testSteps = new DataSource(testFilePath, TEST_STEPS, stepSrc);


					// Create test case
					if(dataSrc == null || dataSrc.equals(NA))
					{
						testCases.add(new TestCase(testSteps, testCase, seqId, logLevel));
					}
					else
					{
						DataSource data = new DataSource(testFilePath, TEST_DATA, dataSrc);

						testCases.add(new TestCase(testSteps, testCase, seqId, logLevel, data));
					}
				} catch(KDTParseException kpe)
				{
					// Error parsing test case, log failure and move on
					testResults.put(testCase, false);
					parseErrors.add(testCase + ": " + kpe.getMessage() + "<BR>");					
				}
			}
			else
			{
				// Test not executed, so mark as not passed for dependent tests.
				testResults.put(testCase, false);
			}
		}		

		if(parseErrors.size() > 0)
		{
			throw new KDTParseException(Arrays.toString(parseErrors.toArray()));
		}

	} // End constructor

	public static int parseLogLevel(String lvl)
	{
		int ret = FileTools.getSafeInt(lvl);
		// Normalize value
		if(ret < 0)
			ret = 0;
		else if(ret > 6)
			ret = 6;

		return ret;
	}

	public static void pauseRobot() {
		if(robot) {
			RT.kill();
		}
	}

	public static void unpauseRobot() {
		if(robot) {
			RT = new RobotThread();
			RT.start();
		}
	}

	/**
	 * Runs all of the test cases and reports the results.
	 */
	public boolean run(LinkedList<TestRunData> trds)
	{
		boolean pass = true;
		TestRunData trd;
		TestContext context = TestContext.getContext();
		context.setTestFilePath(testFilePath);

		totalCases = testCases.size();
		caseRan = 0;
		caseSuccesses = 0;
		caseFails = 0;
		caseSkips = 0;

		for(TestCase tc : testCases)
		{

			trd = new TestRunData();
			trd.TestCaseID = tc.id;
			context.log(LogLevel.info, "Checking " + tc.id + "'s dependencies.");
			boolean runTest = true;
			for(String dep : dependencies.get(tc.id))
			{
				// Don't run the test if any of it's dependencies failed.
				if(!testResults.get(dep).booleanValue())
				{
					context.log(LogLevel.warn, "Dependency " + dep + " not met.");
					runTest = false;
					caseSkips++;
				}
			}
			if(runTest)
			{
				context.log(LogLevel.info, "Dependencies OK, running test.");
				context.setLog(tc.logLevel);
				try
				{
					caseRan++;
					boolean result = tc.run();
					testResults.put(tc.id, result);
					if(!result)
					{
						pass = false;
						trd.LastResult = "Failed";
						caseFails++;
					}
					else
					{
						trd.LastResult = "Passed";
						caseSuccesses++;
					}
				}
				catch(KDTException e)
				{
					// Errors should be caught inside TestCase, but just in case.
					context.log(LogLevel.error, "Error running Test case " + tc.id);
					testResults.put(tc.id, false);
					trd.LastResult = "Failed";
					caseFails++;
				}
			}
			else
			{
				// Test didn't run due to missing dependencies
				context.log(LogLevel.error, tc.id + " not run due to missing dependencies.");
				testResults.put(tc.id, false);
				ev.warnScenario(tc.id, "", "", "", "Dependencies not met");
				trd.LastResult = "Not run";
			}
			ev.writeTestData(trd);
			trds.add(trd);			
		}

		return pass;
	} // End run

	private static File[] getTestFiles()
	{
		JFileChooser FC = new JFileChooser(System.getProperty("user.dir"));
		FC.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel & CSV files", "xls", "xlsx", "csv");
		FC.setFileFilter(filter);
		int result = FC.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			return FC.getSelectedFiles();
		}
		else
		{
			return new File[0];
		}
	}




	/**
	 * Runs the KDTDriver. If no files are passed in, a dialog box will open 
	 * so that files can be selected.
	 * <p>
	 * Command-line arguments:
	 * <ul> 
	 * <li>-http://Url/Of/The/Application/Under/Test : Sets the URLl to the url passed by the user via batch file, this will be further used to launch the tests.</li>
	 * <li>-firefox : Sets the browser to Firefox browser.</li>
	 * <li>-chrome : Sets the browser to Chrome browser.</li>
	 * <li>-ie : Sets the browser to Internet Explorer browser.</li>
	 * <li>-safari : Sets the browser to Safari browser.</li>
	 * <li>-noreport : Prevents the report from being automatically opened when a test is finished.</li>
	 * <li>-copyreport {filepath} : Copies the report to another location. File path can be relative or absolute.</li>
	 * <li>-copylog {filepath} : Copies the log to another location. File path can be relative or absolute.</li>
	 * <li>-basepath [filepath] : Base folder to copy the log/report to. With this option on, -copylog and -copyreport 
	 * must either be relative paths or be absent.</li>
	 * <li>-timestampcopy : Appends the log file timestamp to the base path folder name. (requires -basepath)</li>
	 * <li>-manualmode : Runs KDT in manual mode. Keywords are entered manually and executed one at a time.<li>
	 * <li>-validate : Validates the test file(s), without running them.</li>
	 * <li>-version : Prints out the revision number the jar was compiled with.</li>
	 * <li>-nowriteback : Don't write back test info to the test file.</li>
	 * <li>-forcelog [log level] : Forces the log level to a certain value (0-6), overrides the test file.</li>
	 * <li>-noscreenshot : Disable screenshots.</li>
	 * <li>-help : Prints this message</li>
	 * </ul>
	 * 
	 * @param args Paths of test files to open.
	 */
	public static void main(String[] args)
	{
		TestContext context = TestContext.getContext();
		// Initialize reporting;
		ev = new EventCase("KDT", "./reports/");
		// Separate arguments and parameters.
		LinkedList<String[]> argList = new LinkedList<String[]>();
		for(int i = 0; i < args.length; i++)
		{
			String arg = args[i];
			if(arg.startsWith("-"))
			{
				int j = i;
				do {
					j++;
				} while(j < args.length && !args[j].startsWith("-"));
				j = Math.min(j, args.length);
				argList.add(Arrays.copyOfRange(args, i, j));
				i = j - 1;
			}
			else
			{
				argList.add(new String[] {arg});
			}
		}

		// Parse arguments

		boolean openReport = true;		
		LinkedList<String> paths = new LinkedList<String>();		
		String copyReport = null;
		String copyLog = null;
		String copyPath = null;
		boolean timestamp = false;		
		boolean doRun = true;
		boolean writeback = true;


		for(String[] params : argList)
		{
			System.out.println(Arrays.toString(params));
			String arg = params[0];
			if(arg.startsWith("-"))
			{
				if(arg.contains("http"))
				{
					applicationUnderTestUrl = arg.substring(1);
					System.out.println(applicationUnderTestUrl);
				}
				if(arg.equals("-firefox"))
				{
					Launch.brow = BrowserType.FIREFOX;
				}
				if(arg.equals("-ie"))
				{
					Launch.brow = BrowserType.IE;
				}
				if(arg.equals("-chrome"))
				{
					Launch.brow = BrowserType.CHROME;
				}
				if(arg.equals("-macchrome"))
				{
					Launch.brow = BrowserType.MACCHROME;
				}
				if(arg.equals("-safari"))
				{
					Launch.brow = BrowserType.SAFARI;
				}
				if(arg.equals("-noreport"))
				{
					openReport = false;
				}
				else if(arg.equals("-copyreport"))
				{
					if(params.length > 1)
						copyReport = params[1];
					else
						copyReport = "";
				}
				else if(arg.equals("-copylog"))
				{
					if(params.length > 1)
						copyLog = params[1];
					else
						copyLog = "";
				}
				else if(arg.equals("-basepath"))
				{
					if(params.length > 1)
						copyPath = params[1];
					else
						copyPath = "";
				}
				else if(arg.equals("-timestampcopy"))
				{
					timestamp = true;
				}
				else if(arg.equals("-manualmode"))
				{
					// Run manual mode
					KDTManual.run();
					return;
				}
				else if(arg.equals("-halterror"))
				{
					TestStep.haltOnError = true;
				}
				else if(arg.equals("-savetemplate"))
				{
					TemplateCreator.main(new String[0]);
					return;
				}
				else if(arg.equals("-validate"))
				{
					doRun = false;
				}
				else if(arg.equals("-version"))
				{
					System.out.println(REVISION);
					return;
				}
				else if(arg.equals("-nowriteback"))
				{
					writeback = false;
				}
				else if(arg.equals("-forcelog"))
				{
					if(params.length > 1)
					{
						forceLog = Integer.parseInt(params[1]);
						System.out.println("Forcing log to " + forceLog);
					}
				}
				else if(arg.equals("-noscreenshot"))
				{
					ev.screenshot = false;
				}
				else if(arg.equals("-norobot"))
				{
					robot = false;
				}
				else if(arg.equals("-help"))
				{
					System.out.println("Runs the KDTDriver. If no files are passed in, a dialog box will open so that files can be selected.");
					System.out.println("Command-line arguments:");
					System.out.println("-noreport : Prevents the report from being automatically opened when a test is finished. ");
					System.out.println("-copyreport {filepath} : Copies the report to another location. File path can be relative or absolute. ");
					System.out.println("-copylog {filepath} : Copies the log to another location. File path can be relative or absolute. ");
					System.out.println("-basepath [filepath] : Base folder to copy the log/report to. With this option on, -copylog and -copyreport must either be relative paths or be absent. ");
					System.out.println("-timestampcopy : Appends the log file timestamp to the base path folder name. (requires -basepath) ");
					System.out.println("-manualmode : Runs KDT in manual mode. Keywords are entered manually and executed one at a time. ");
					System.out.println("-validate : Validates the test file(s), without running them. ");
					System.out.println("-version : Prints out the revision number the jar was compiled with. ");
					System.out.println("-nowriteback : Don't write back test info to the test file. ");
					System.out.println("-forcelog [log level] : Forces the log level to a certain value (0-6), overrides the test file.");
					System.out.println("-noscreenshot : Disable screenshots.");
					System.out.println("-help : Prints this message");
					return;
				}
			}
			else
			{
				paths.add(arg);
			}			
		}


		// Validate args
		String reportPath = null;
		String logPath = null;
		try{
			if(copyPath == null)
			{
				if(timestamp)
				{
					System.out.println("Must specify a base path using \"-basepath <path>\" when using -timestamp.");
				}
				copyPath = "";
			}
			String finalPath = copyPath;
			if(timestamp)
			{
				finalPath += "_" + ev.getFileDate();
			}
			if(!finalPath.isEmpty())
			{
				finalPath += "/";
			}			
			if(copyReport != null && !(copyReport.isEmpty() && copyPath.isEmpty())) 
			{
				reportPath = finalPath + copyReport;
			}
			if(copyLog != null && !(copyLog.isEmpty() && copyPath.isEmpty())) 
			{
				logPath = finalPath + copyLog;
			}
		}
		catch(Exception e)
		{
			System.out.println("Unable to parse paths: " + e.getMessage());
			return;
		}

		// Execute test files
		KDTDriver kdt = null;

		// Open file selector if no paths passed.
		if(paths.size() == 0)
		{
			File[] files = getTestFiles();
			for(File f : files)
			{
				paths.add(f.getAbsolutePath());
			}
		}

		// Start up mouse robot
		if(robot)
		{
			RT = new RobotThread();
			RT.start();
		}
		boolean suitePassed = false;		
		for(String path : paths)
		{			
			LinkedList<TestRunData> runs = new LinkedList<TestRunData>();
			// Skip arguments
			if(path.startsWith("-"))
				continue;
			try {
				TestRunData.LastRun = new java.util.Date().toString();
				TestRunData.FilePath = path;
				kdt = new KDTDriver(path);
				ev.startUC(FileTools.getFileName(path), "");
				boolean result = doRun ? kdt.run(runs) : true;	
				suitePassed = result;
				if(suitePassed) {
					context.log(LogLevel.info, "Test Passed");
				}
				else {
					context.log(LogLevel.info, "Test Failed");
				}
				if(result)
				{
					ev.passUC(FileTools.getFileName(path), "", "Success: " + kdt.caseSuccesses + "/" + kdt.caseRan +
							"<br>" + "Failures: " + kdt.caseFails + "/" + kdt.caseRan + 
							"<br>" + "Skipped: " + kdt.caseSkips + "/" + kdt.totalCases);
				}
				else
				{
					ev.failUC(FileTools.getFileName(path), "", "Success: " + kdt.caseSuccesses + "/" + kdt.caseRan +
							"<br>" + "Failures: " + kdt.caseFails + "/" + kdt.caseRan + 
							"<br>" + "Skipped: " + kdt.caseSkips + "/" + kdt.totalCases);
				}


			} catch (Exception e)
			{		
				if(kdt != null)
				{
					ev.failUC(FileTools.getFileName(path), e.getMessage(), "Success: " + kdt.caseSuccesses + "/" + kdt.caseRan +
							"<br>" + "Failures: " + kdt.caseFails + "/" + kdt.caseRan + 
							"<br>" + "Skipped: " + kdt.caseSkips + "/" + kdt.totalCases);
				}
				else
				{
					ev.failUC(FileTools.getFileName(path), e.getMessage(), "");
				}
			}
			finally
			{	
				if(writeback)
				{
					FileTools.writeTestData(runs);
				}
			}
		}
	
		EventCase.report.endTest(EventCase.extentlogger);
		if(openReport)
		{
			// Open up report in web browser.
			try {
				String report = "file:///" + (new File(ev.getFileName())).getAbsolutePath().replace('\\', '/').replace("./", "").replace(" ", "%20");
				java.awt.Desktop.getDesktop().browse(new java.net.URI(report));
			} catch (Exception e) {
				System.out.println("Couldn't open browser to view report: " + e.getMessage());
			}
		}

		ev.closeLoggers();

		EventCase.report.flush();
		// Sending email once report is generated.
		// call that compress method pass global list 

		// Copy report and/or log
		if(reportPath != null)
		{
			System.out.println("Copying report to " + reportPath.toString());
			ev.copyReport(reportPath.toString());
		}
		if(logPath != null)
		{
			System.out.println("Copying log to " + logPath.toString());
			ev.copyLog(logPath.toString());
		}
		if(robot) RT.kill();
		
		System.out.println("Testing finished.");
		
		// Calling emailing method
		
		try {
			com.qualitestgroup.util.mail.ZipUtils.main(args);
			
			com.qualitestgroup.util.mail.Mailer.Send_ExtentReportMail();
		} catch (Exception e) {			
			e.printStackTrace();
			System.out.println("Unable to send email with Test Result in HTML format");
		}
		
		if(suitePassed) {
			System.out.println("Test Passed");
		}
		else {
			System.out.println("Test Failed");
			System.exit(1);
		}
		return;
	}	// End main

	public static class TestRunData {
		public String LastResult, LastLog, LastReport, TestCaseID;
		public static String LastRun, FilePath;

	}
	
	//returns the URL for application under test to the calling entity
	public static String getApplicationUrl() {
		return applicationUnderTestUrl;
	}

}
