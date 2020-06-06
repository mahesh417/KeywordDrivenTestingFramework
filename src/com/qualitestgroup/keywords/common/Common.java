package com.qualitestgroup.keywords.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.qualitestgroup.kdt.KDTDriver;
import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataRow;
import com.qualitestgroup.util.datadriver.DataSource;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.kdt.exceptions.KDTParseException;
import com.qualitestgroup.util.logging.Logger.LogLevel;
import com.testautomationguru.utility.PDFUtil;

public class Common extends KeywordGroup {

	/**
	 * Compares two values.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Value1:</b> The first value to compare</li>
	 * <li><b>Value2:</b> The second value to compare</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Validate extends Keyword {
		String val1, val2;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Value1", "Value2");
			val1 = args.get("Value1");
			val2 = args.get("Value2");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			this.comment = "Comparing '" + val1 + "' to '" + val2 + "'.";
			if (val1.equals(val2)) {
				context.log(LogLevel.info, "Validation success");
			} else {
				context.log(LogLevel.warn, "Validation fail: \nValue1: " + val1 + "\nValue2: " + val2);
				throw new KDTKeywordExecException("Validation failed");
			}
		}

	}

	/*
	 * Keyword in progress @author QualiTest public static class WaitForElement
	 * extends Keyword { public void init() throws KDTKeywordInitException {
	 * super.init();
	 * 
	 * }
	 * 
	 * @Override public void exec() throws KDTKeywordExecException { String
	 * xpath = ""; WebDriver driver = context.getWebDriver(); (new
	 * WebDriverWait(driver,
	 * 10)).until(ExpectedConditions.presenceOfElementLocated(By.className(xpath
	 * ))); }
	 * 
	 * }
	 */

	/**
	 * Pauses the test until user input is received. Doesn't work if there is no
	 * console window.
	 * <p>
	 * No arguments
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Pause extends Keyword {
		@Override
		public void exec() throws KDTKeywordExecException {
			System.out.println("Press Enter to continue...");

			if (System.console() != null) {
				System.console().readLine();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Delays execution.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Minutes:</b> The number of minutes to wait</li>
	 * <li><b>Seconds:</b> The number of seconds to wait</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Wait extends Keyword {
		private int s;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			if (!hasArgs("Seconds")) {
				verifyArgs("Minutes");
			}
			if (!hasArgs("Minutes")) {
				verifyArgs("Seconds");
			}

			s = 0;
			if (hasArgs("Seconds")) {
				s += Integer.parseInt(args.get("Seconds"));
			}

			if (hasArgs("Minutes")) {
				s += Integer.parseInt(args.get("Minutes")) * 60;
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			try {
				Thread.sleep(s * 1000);
			} catch (InterruptedException e) {
				context.log(LogLevel.warn, "Sleep interrupted");
			}
		}
	}

	/**
	 * Performs a goto, the nature of which depends on the arguments.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Sample</b> Sample description.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class GoTo extends Keyword {
		private static String ARG_MAX_JUMPS = "MaxJumps";
		private static String ARG_EXIT_CONDITION = "Until";
		private static String ARG_CONTINUE_CONDITION = "While";
		private int jumpsPerformed = 0;

		private int maxJumps = Integer.MAX_VALUE;
		private boolean exitCondition = false;
		private boolean continueCondition = true;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (hasArgs(ARG_MAX_JUMPS)) {
				maxJumps = Float.valueOf(args.get(ARG_MAX_JUMPS)).intValue();
			} else if (hasArgs(ARG_EXIT_CONDITION)) {
				String exitString = args.get(ARG_EXIT_CONDITION);
				exitCondition = "true".equalsIgnoreCase(exitString) || "yes".equalsIgnoreCase(exitString)
						|| "y".equalsIgnoreCase(exitString) ? true : false;
			} else if (hasArgs(ARG_CONTINUE_CONDITION)) {
				String continueString = args.get(ARG_EXIT_CONDITION);
				continueCondition = "true".equalsIgnoreCase(continueString) || "yes".equalsIgnoreCase(continueString)
						|| "y".equalsIgnoreCase(continueString) ? true : false;
			} else {
				throw new KDTKeywordInitException("GoTo keyword requires an exit condition in the form of either '"
						+ ARG_CONTINUE_CONDITION + "', '" + ARG_EXIT_CONDITION + "' or '" + ARG_MAX_JUMPS + ".");
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			this.gotoFlag = true;

			if (exitCondition || !continueCondition) {
				this.gotoFlag = false;
				return;
			} else if (jumpsPerformed >= maxJumps) {
				this.gotoFlag = false;
				jumpsPerformed = 0;
				return;
			} else {
				jumpsPerformed += 1;
			}
		}
	}

	/**
	 * Prints out a value to the console, and to the comment in the report.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Value</b> The string to print.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Print extends Keyword {
		String val;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Value");
			val = args.get("Value");
			if (val.startsWith("$") && context.getData().containsKey(val)) {
				val = context.getData().get(val);
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			System.out.println(val);
			this.comment = val;
		}
	}

	/**
	 * Concatenates two strings and saves the value in a data variable. If the
	 * arguments are "String1" and "String2", the resulting string is
	 * "String1String2".
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Value1:</b> The first string to concatenate.</li>
	 * <li><b>Value2:</b> The second string to concatenate.</li>
	 * <li><b>SaveTo:</b> The variable to save to.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Concatenate extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Value1", "Value2", "SaveTo");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			saveValue(args.get("Value1") + args.get("Value2"));
		}

	}

	/**
	 * Copies a source directory to a destination directory.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>SourcePath:</b> Source directory to copy</li>
	 * <li><b>DestinationPath:</b> Destination to copy the contents of source
	 * directory to</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class MoveDirectory extends Keyword {
		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("SourcePath", "DestinationPath");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			String src = args.get("SourcePath");
			String dest = args.get("DestinationPath");
			try {
				FileUtils.copyDirectory(new File(src), new File(dest));
			} catch (IOException e) {
				e.printStackTrace();
				throw new KDTKeywordExecException("Unable to move from " + src + " to " + dest + ".", e);
			}
		}
	}

	/**
	 * Stores a value in the TestContext's global storage. Values can later be
	 * retrieved using the RetrieveGlobal keyword.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Key:</b> The key to store the value under. If the key already
	 * exists in the storage, the old value will be overwritten</li>
	 * <li><b>Value:</b> The value to store.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class StoreGlobal extends Keyword {
		private String key, value;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Key", "Value");
			key = args.get("Key");
			value = args.get("Value");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			context.storeGlobal(key, value);
		}
	}

	/**
	 * Retrieves a value in the TestContext's global storage. Values can be
	 * stored using the StoreGlobal keyword.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Key:</b> The key to retrieve the value from.</li>
	 * <li><b>SaveTo:</b> The test variable to save the retrieved value to.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class RetrieveGlobal extends Keyword {
		private String key;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Key", "SaveTo");
			key = args.get("Key");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			String value = context.retrieveGlobal(key).toString();
			this.saveValue(value);
		}
	}

	/**
	 * Stores a value in the TestContext's local storage. Values can later be
	 * retrieved using the RetrieveLocal keyword.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Key:</b> The key to store the value under. If the key already
	 * exists in the storage, the old value will be overwritten</li>
	 * <li><b>Value:</b> The value to store.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class StoreLocal extends Keyword {
		private String key, value;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Key", "Value");
			key = args.get("Key");
			value = args.get("Value");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			context.store(key, value);
		}
	}

	/**
	 * Retrieves a value in the TestContext's local storage. Values can be
	 * stored using the StoreLocal keyword.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Key:</b> The key to retrieve the value from.</li>
	 * <li><b>SaveTo:</b> The test variable to save the retrieved value to.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class RetrieveLocal extends Keyword {
		private String key;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("Key", "SaveTo");
			key = args.get("Key");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			String value = context.retrieve(key).toString();
			saveValue(value);
		}
	}

	/**
	 * Takes a screenshot. The screenshot is linked in the report comment.
	 * <p>
	 * No Arguments
	 * 
	 * @author QualiTest
	 *
	 */
	public static class Screenshot extends Keyword {
		@Override
		public void exec() throws KDTKeywordExecException {
			comment = "<a href=\"screenshots/" + KDTDriver.ev.screenshot() + "\">Screen Shot</a>";
		}
	}

	// In development
	/**
	 * Executes a test case. Arguments are the same as they would be in a test
	 * case definition.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>ScenarioID</b>: The ID of the test scenario to run.</li>
	 * <li><b>LogLevel</b>: The ID of the test scenario to run.</li>
	 * <li><b>DataSheet</b>: The ID of the test scenario to run.</li>
	 * <li><b>ScenarioSheet</b>: The ID of the test scenario to run.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	/*
	 * public static class RunTestCase extends Keyword { TestCase tc;
	 * 
	 * @Override public void init() throws KDTKeywordInitException { // TODO
	 * Auto-generated method stub verifyArgs("ScenarioID", "LogLevel",
	 * "DataSheet", "ScenarioSheet"); context.pushContext(); try { String seqId
	 * = args.get("ScenarioID"); int logLevel =
	 * KDTDriver.parseLogLevel(args.get("LogLevel")); String dataSrc =
	 * args.get("DataSheet"); String stepSrc = args.get("ScenarioSheet");
	 * DataSource testSteps = new DataSource(context.getTestFilePath(),
	 * TEST_STEPS, stepSrc); // Create test case if(dataSrc == null ||
	 * dataSrc.equals(NA)) { tc = new TestCase(testSteps, "TC", seqId,
	 * logLevel); } else { DataSource data = new
	 * DataSource(context.getTestFilePath(), TEST_DATA, dataSrc);
	 * 
	 * tc = new TestCase(testSteps, "TC", seqId, logLevel, data); } } catch
	 * (KDTParseException e) { throw new KDTKeywordInitException(
	 * "Unable to create test case.", e); }
	 * 
	 * }
	 * 
	 * @Override public void exec() throws KDTKeywordExecException { boolean
	 * result; try { result = tc.run(); } catch (Exception e) { throw new
	 * KDTKeywordExecException("Error running test case.", e); }
	 * validate(result, "Test case failed."); }
	 * 
	 * @Override public void cleanup() { super.cleanup(); context.popContext();
	 * } }//
	 */

	/**
	 * Data-drives execution of a keyword. This will execute the keyword once
	 * for each row in the given data sheet. If any of the executions fail, the
	 * remaining keywords won't execute. The arguments to the keyword are passed
	 * as arguments to this keyword, with the value being the name of the column
	 * in the data sheet to get the value from. If the column name is not found,
	 * the value will be passed through as normal.
	 * <p>
	 * Arguments
	 * <ul>
	 * <li><b>RK_Keyword</b>: The name of the keyword to execute.</li>
	 * <li><b>RK_App</b>: The application of the keyword to execute.</li>
	 * <li><b>RK_DataSheet</b>: The data sheet to execute from.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class RepeatKeyword extends Keyword {
		DataSource ds;
		String keyword, app;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("RK_Keyword", "RK_App", "RK_DataSheet");
			try {
				ds = new DataSource(context.getTestFilePath(), "", args.get("RK_DataSheet"));
			} catch (KDTParseException e) {
				throw new KDTKeywordInitException("Error initializing: ", e);
			}
			keyword = args.get("RK_Keyword");
			app = args.get("RK_App");
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			DataDriver dd;
			try {
				dd = ds.getDataDriver();
				for (DataRow dr = dd.nextRow(); dr != null; dr = dd.nextRow()) {
					// Number of arguments to Keyword.run()
					// (# of arguments to RepeatKeyword - # of RK arguments) * 2
					// for arg name and value
					int numArgs = (args.size() - 3) * 2;
					ArrayList<String> arguments = new ArrayList<String>(numArgs);
					for (java.util.Map.Entry<String, String> rkArg : args.entrySet()) {
						// Skip args for this keyword.
						if (rkArg.getKey().equals("RK_Keyword") || rkArg.getKey().equals("RK_App")
								|| rkArg.getKey().equals("RK_DataSheet")) {
							continue;
						}

						String kwArg = rkArg.getKey();
						String col = rkArg.getValue();
						String kwVal = dr.getItem(col);
						if (kwVal == null) {
							kwVal = col;
						}

						arguments.add(kwArg);
						arguments.add(kwVal);
					}

					Keyword.run(app, keyword, arguments.toArray(new String[] {}));
				}
			} catch (Exception e) {
				throw new KDTKeywordExecException("Error: ", e);
			}
		}
	}

	/**
	 * Returns the current date and/or time.
	 * <p>
	 * Mandatory Arguments:
	 * <ul>
	 * <li><b>SaveTo</b>: The variable to store the date/time.</li>
	 * <ul>
	 * Optional Arguments:
	 * <ul>
	 * <li><i>FormatString:</i> Date format string. Default is "MM/dd/yyyy".
	 * </li>
	 * <li><i>AddDays:<i> Specify a number of days to add to the date. For
	 * example, if the current date is August 5th (08/05/2014), passing "5" will
	 * return 08/10/2014, and passing "-3" will return 08/02/2014.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class GetCurrentDateTime extends Keyword {
		private String dateFormatString = "MM/dd/yyyy";
		private int addDays = 0;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("SaveTo");
			if (hasArgs("FormatString")) {
				dateFormatString = args.get("FormatString");
			}
			if (hasArgs("AddDays")) {
				try {
					addDays = Integer.parseInt(args.get("AddDays"));
				} catch (NumberFormatException e) {
					throw new KDTKeywordInitException("Unable to parse number of days.", e);
				}
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			try {
				DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
				Calendar rightNow = Calendar.getInstance();
				rightNow.add(Calendar.DAY_OF_MONTH, addDays);
				saveValue(dateFormat.format(rightNow.getTime()));
			} catch (Exception e) {
				throw new KDTKeywordExecException("Unable to get date.", e);
			}
		}
	}

	/**
	 * Returns date with days added to current date exclude weekends
	 * <p>
	 * Mandatory Arguments:
	 * <ul>
	 * <li><b>SaveTo</b>: The variable to store the date/time.</li>
	 * <ul>
	 * Optional Arguments:
	 * <ul>
	 * <li><i>FormatString:</i> Date format string. Default is "MM/dd/yyyy".
	 * </li>
	 * <li><i>AddDays:<i> Specify a number of days to add to the date but does
	 * NOT count weekends(Sat & Sun). For example, if the current date is
	 * 08/05/2014 is a Monday, passing "3" will return 08/08/2014, but if
	 * 08/05/2014 is a Wednesday, passing "3" will return 08/10/2014.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 *
	 */
	public static class AddDaysToCurrentDateExcludeWeekends extends Keyword {
		private String dateFormatString = "MM/dd/yyyy";
		private int addDays = 0;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			verifyArgs("SaveTo");
			if (hasArgs("FormatString")) {
				dateFormatString = args.get("FormatString");
			}
			if (hasArgs("AddDays")) {
				try {
					addDays = Integer.parseInt(args.get("AddDays"));
				} catch (NumberFormatException e) {
					throw new KDTKeywordInitException("Unable to parse number of days.", e);
				}
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			try {
				DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
				Calendar rightNow = Calendar.getInstance();
				// if the day of the week is Wed, Thur or Friday this will add 2
				// to addDays
				if (rightNow.get(Calendar.DAY_OF_WEEK) > 3 && rightNow.get(Calendar.DAY_OF_WEEK) < 7) {
					addDays += 2;
				} else if (rightNow.get(Calendar.DAY_OF_WEEK) == 7) // this is
																	// Saturday
					addDays += 1;

				rightNow.add(Calendar.DAY_OF_MONTH, addDays);
				System.out.println(dateFormat.format(rightNow.getTime()));
				saveValue(dateFormat.format(rightNow.getTime()));
			} catch (Exception e) {
				throw new KDTKeywordExecException("Unable to add days to date.", e);
			}
		}
	}
	
	/**
	 * 
	 * @author vidaya.anand
	 * @Method name Compare_pdf_ByLine
	 * @param pdf_filewithpath
	 * @param check_String
	 * @param check_rest
	 * @return
	 */
	public static boolean Compare_pdf_ByLine(String pdf_filewithpath, String check_String, String check_rest) {
        PDFUtil pdfUtil = new PDFUtil();
        String fullpdfContent="";
        boolean found_flag=false;
        String pdf_line=null; 

		try {			
			PdfReader pdfReader = new PdfReader(pdf_filewithpath);	//Create PdfReader instance.
		 
			if (pdfReader.getNumberOfPages() > 0) {
				fullpdfContent = PdfTextExtractor.getTextFromPage(pdfReader, 1); 
				 
				System.out.println("PDF Page1:"+fullpdfContent);
				System.out.println("----");	 
				
				// check first search
				String lines[] = fullpdfContent.split("\\r?\\n"); 
				for (String line : lines) {            	
	            	if( line.contains(check_String)) {
	            		pdf_line=line;
	            		break;
	            	}
				}
			}
			// check other search strings
			if( pdf_line.contains(check_String)) {	
				String search_keys[] = check_rest.split("~");

				for (String skey : search_keys) {            	
	            	if( pdf_line.contains(skey)) {
	            		found_flag=true;
	            	}
	            	else {
	            		System.out.println("Not Found Search String: "+ skey);
	            		found_flag=false;
	            		break;
	            	}
				}
			}				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return found_flag; 
		
    } 
	////////////////////////////////////////////////////////////////////////////////////////

	
	/**
	 * @author vidaya.anand
	 * @name  GetlastModifiedFile
	 * @param projdir
	 * @return last Modified File name
	 * @throws IOException
	 */
	public static String GetlastModifiedFile(String projdir) throws IOException {
		Path dir1 = Paths.get(projdir);  // specify your directory
		String lm_file= null;

		Optional<Path> lastFilePath = Files.list(dir1)    // here we get the stream with full directory listing
		    .filter(f -> !Files.isDirectory(f))  // exclude subdirectories from listing
		    .max(Comparator.comparingLong(f -> f.toFile().lastModified()));  // finally get the last file using simple comparator by lastModified field

		if ( lastFilePath.isPresent() ) // your folder may be empty
		{
			lm_file = lastFilePath.toString().split("\\[")[1].split("\\]")[0];
		}
		return(lm_file) ; 
	}
		/////////////////////////////////////////////////////////////////////////////////
	/**
	 * @author vidaya.anand
	 * @desc - function to return start date delimited by colon
	 * @param startDate
	 * @param term
	 * @return
	 */
	public static String get_startDateByTerm(String startDate, int term)  {
		String start_date = "", startDate2;
		int itr=0;

		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		startDate2 = startDate;
		for (itr=0; itr < term; itr++) {
			Date date;
			try {
					date = formatter.parse(startDate2);

				start_date = start_date + formatter.format(date) ;
				if (term != itr+1) start_date = start_date + ":" ;
				Calendar cal = Calendar.getInstance();
				cal = formatter.getCalendar();
				cal.add(Calendar.MONTH, 1); // to get previous year add -1
				String nextMonth = cal.get(Calendar.MONTH) + 1 + "/"
						+ (cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR));
				startDate2 = nextMonth;
			} catch (ParseException e) { 
				e.printStackTrace();
			}		
		}
		return start_date;
	}
	/////////////////////////////////////////////////////////////////////////////////
	/**
	 * @author vidaya.anand
	 * @desc - function to return end date delimited by colon
	 * @param endDate
	 * @param term
	 * @return
	 */	
	public static String get_endDateByTerm(String endDate, int terms)  {		
		String end_date = "", next_enddate;

		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		next_enddate = endDate;
		for (int itr = 0; itr < terms; itr++) {			
			Calendar cal = Calendar.getInstance();
			cal = formatter.getCalendar();
			cal.add(Calendar.MONTH, itr+1); 
			cal.add(Calendar.DATE, -1);
			
			next_enddate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DATE) + "/"
						+ cal.get(Calendar.YEAR);
			
			end_date = end_date + next_enddate + ":" ;			
		}
		return end_date;
	}
}
