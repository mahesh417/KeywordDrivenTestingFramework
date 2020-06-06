package com.qualitestgroup.kdt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataRow;
import com.qualitestgroup.util.datadriver.DataSource;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.kdt.exceptions.*;
import com.qualitestgroup.util.logging.Logger.LogLevel;

import static com.qualitestgroup.kdt.KDTConstants.*;

/**
 * Representation of a test case
 * 
 * A test case is a collection of {@link TestStep}s, along with an optional
 * {@link DataSource}. The {@link DataSource} contains argument values for test
 * steps that have variable arguments.
 * 
 * @author Matthew Swircenski
 *
 */
public class TestCase {

	public final String id;
	int logLevel;
	private LinkedList<TestStep> testSteps;
	private DataSource dataSource = null;
	private String scenario;

	/**
	 * Creates a test case.
	 * 
	 * @param stepSource
	 *            The {@link DataSource} to read test steps from.
	 * @param scenId
	 *            The ID of this test case. Only test steps that have this ID
	 *            will be included in the test case.
	 * @param logLevel
	 *            The log level for this test case
	 * @throws KDTParseException
	 *             If source is unable to be parsed.
	 */
	public TestCase(DataSource stepSource, String testCaseId, String scenId, int logLevel) throws KDTParseException {
		this.id = testCaseId;
		this.logLevel = logLevel;
		this.scenario = scenId;
		TestContext context = TestContext.getContext();
		context.log(LogLevel.debug, "Creating test case " + this.id);

		DataDriver source = stepSource.getDataDriver();

		// Aggregate list of parse exceptions
		List<String> parseErrors = new LinkedList<>();

		// Validate file
		if (!source.hasColumns(scenColNames)) {
			String[] missingCols = source.getMissingColumns(scenColNames);
			context.log(LogLevel.error, "Test step sheet missing columns");
			throw new KDTParseException("Test step sheet is missing columns: " + Arrays.toString(missingCols));
		}

		// Extract test steps
		testSteps = new LinkedList<TestStep>();
		DataRow row = source.nextRow();
		boolean lookAhead = true;
		while (lookAhead || source.hasNext()) {
			// Get next line if not gotten already
			if (lookAhead)
				lookAhead = false;
			else
				row = source.nextRow();

			// Load test steps with the same ID
			if (row.getItem(SCEN_ID).equals(scenId)) {
				try {
					String kw = row.getItem(KEYWORD);
					String app = row.getItem(APP);
					TestStep ts;

					// Parse test step
					ts = new TestStep(kw, app, testCaseId, scenId);

					// Iterate through following rows to get arguments
					// until next test step or EOF
					while (source.hasNext()) {
						lookAhead = true;
						row = source.nextRow();
						if (row.getItem(SCEN_ID).isEmpty()) {
							// Only add arguments that have both an argument and
							// a value
							String arg = row.getItem(ARGS);
							String val = row.getItem(VALUES);
							if (!arg.isEmpty() /* && !val.isEmpty() */) {
								ts.addArgument(arg, val);
							}
						} else // End of argument listing
						{
							break;
						}
					}

					testSteps.add(ts);
				} catch (KDTParseException kpe) {
					parseErrors.add(kpe.getMessage() + "<BR>");
				}
			}

		}

		if (parseErrors.size() > 0) {
			throw new KDTParseException(Arrays.toString(parseErrors.toArray()));
		}
	}

	/**
	 * Creates a test case with a data source.
	 * 
	 * @param source
	 *            The {@link DataSource} to read test steps from.
	 * @param id
	 *            The ID of this test case. Only test steps that have this ID
	 *            will be included in the test case.
	 * @param logLevel
	 *            The log level for this test case
	 * @param dataSrc
	 *            The data source to read variable data form.
	 * @throws KDTParseException
	 *             If the data source is unable to be parsed.
	 */
	public TestCase(DataSource source, String testCaseId, String scenId, int logLevel, DataSource dataSrc)
			throws KDTParseException {
		this(source, testCaseId, scenId, logLevel);
		dataSource = dataSrc;
	}

	/**
	 * Loads data from the test case's data source.
	 * 
	 * @return List of {@link TestContext.Data}. Contains one item per row in
	 *         the data source.
	 * @throws KDTException
	 */
	private ArrayList<TestContext.Data> loadData() throws KDTException {
		ArrayList<TestContext.Data> dataSet = new ArrayList<TestContext.Data>();

		if (dataSource == null) {
			dataSet.add(new TestContext.Data());
		} else {
			DataDriver dd;
			try {
				dd = dataSource.getDataDriver();
			} catch (Exception e) {
				TestContext.getContext().log(LogLevel.error, "Error loading data sheet");
				throw new KDTException("Error loading data sheet", e);
			}

			try {
				// Load variables
				for (DataRow row = dd.nextRow(); row != null; row = dd.nextRow()) {
					boolean allEmpty = true;
					// row = dd.nextRow();
					TestContext.Data varSet = new TestContext.Data();
					// Get variable names and values
					for (String column : row.getHeaders()) {
						String varName = column;
						String varValue = row.getItem(column);
						varSet.put(varName, varValue);
						if (!varValue.isEmpty()) {
							allEmpty = false;
						}
					}

					varSet.setRowIndex(row.getRowNum());

					// Check if data set is disabled
					if (varSet.containsKey("Enabled") && varSet.get("Enabled").equals("N")) {
						continue;
					}

					// Check if data set is empty
					if (varSet.isEmpty() || allEmpty) {
						continue;
					}

					dataSet.add(varSet);
				}

			} catch (java.util.NoSuchElementException e) {
				dataSet.add(new TestContext.Data());
			}
		}
		return dataSet;
	}

	/**
	 * Returns the new loop index for the test step loop in run
	 * 
	 * @param current
	 *            the current index of the test step loop in run
	 * @param testSteps
	 *            the list of testSteps
	 * @param gotoArg
	 *            the goto argument passed to the kw that just finished
	 *            executing
	 * @param gotoVal
	 *            the value of the goto argument passed to the kw that just
	 *            finished executing
	 * @return
	 * @throws KDTException
	 */
	private int getNewTestStepIndex(int current, List<TestStep> testSteps, String gotoArg, String gotoVal)
			throws KDTException {
		int t = -1;
		switch (gotoArg) {
		case GOTO_STEP:
			t = Double.valueOf(gotoVal).intValue() - 1;
			if (t < 0 || t > testSteps.size()) {
				throw new IllegalArgumentException("Invalid goto target index of " + (t + 1));
			}
			return t;

		case GOTO_DELTA:
			t = current + Double.valueOf(gotoVal).intValue() - 1;
			if (t < 0 || t > testSteps.size()) {
				throw new IllegalArgumentException("Invalid goto target index of " + (t + 1));
			}
			return t;

		case GOTO_KEYWORD:
			String[] kwArgs = gotoVal.split("\\[");
			String kwName = kwArgs[0];
			int kwOccurence = kwArgs.length == 2 ? Integer.parseInt(kwArgs[1].replace("]", "")) : 1;
			int count = 0;
			for (int i = 1; i <= testSteps.size(); i++) {
				TestStep ts = testSteps.get(i - 1);
				if (ts.kwName.equalsIgnoreCase(kwName)) {
					count++;
					if (count == kwOccurence) {
						return i - 1;
					}
				}
			}
			throw new IllegalArgumentException("Could not locate a keyword called " + kwName + " for goto target.");

		case GOTO_LABEL:
			for (int i = 0; i < testSteps.size(); i++) {
				TestStep ts = testSteps.get(i);
				if (ts.getLabel().equalsIgnoreCase(gotoVal)) {
					return i;
				}
			}
			throw new IllegalArgumentException(
					"Could not locate a keyword with the label '" + gotoVal + "' for goto target.");

		default:
			throw new IllegalArgumentException("Received arg of " + gotoArg + " for goto.");
		}
	}

	/**
	 * Runs the test case. The sequence of test steps is run once per data set.
	 * 
	 * @return True if the test case ran successfully.
	 * @throws KDTException
	 */
	public boolean run() throws KDTException {
		boolean isSuccessful = true;
		TestContext runContext = TestContext.getContext();
		runContext.setLog(logLevel);
		boolean notFirst = false;

		KDTDriver.ev.startScenario(id, "", testSteps.toString(), "", "");
		// check if keywords exist before running.
		if (testSteps.size() == 0) {
			KDTDriver.ev.warnScenario(id, "", "", "No keywords found for scenario \"" + scenario + "\".", "");
			return false;
		}

		runContext.log(LogLevel.info, "Loading test data for " + id);

		// Load the data
		ArrayList<TestContext.Data> testData;
		try {
			testData = loadData();
		} catch (KDTException e) {
			// Could not find data sheet.
			KDTDriver.ev.failScenario(id, "", "", e.getMessage(), "");
			return false;
		}

		int i = 0;
		for (TestContext.Data dataSet : testData) {
			runContext.setLog(logLevel);
			int totalKeywords = testSteps.size();
			int kwRan = 0;
			int kwSuccesses = 0;
			int kwWarn = 0;
			int kwFails = 0;
			boolean runSuccess = true;

			// Data row number
			i++;

			if (notFirst) {
				KDTDriver.ev.startScenario(id, "", testSteps.toString(), "", "");
			}

			runContext.log(LogLevel.info, id + " run #" + i);
			// Store the data set for this run.
			runContext.setData(dataSet);

			for (int t = 1; t <= testSteps.size(); t++) {
				TestStep ts = testSteps.get(t - 1);
				try {
					if (ts.run()) {
						kwSuccesses++;
						if (ts.skip) {
							break;
						} else if (ts.gotoFlag()) {
							try {
								int newIndex = getNewTestStepIndex(t, testSteps, ts.getGotoType(), ts.getGotoValue());
								totalKeywords += t - newIndex;
								t = newIndex;
							} catch (IllegalArgumentException e) {
								kwSuccesses--;
								// KDTDriver.ev.failScenario(id, "GOTO ERROR",
								// "", e.getMessage(), "");
								runSuccess = false;
								isSuccessful = false;
								kwFails++;
								break;
							}
						}
					} else {
						if (ts.warning)
							kwWarn++;
						else
							kwFails++;
						isSuccessful = false;
						runSuccess = false;
					}
					kwRan++;
				} catch (KDTException e) {
					// Abort current run if an exception is thrown.
					TestContext.getContext().log(LogLevel.error, id + " run #" + i + " failed.");
					isSuccessful = false;
					runSuccess = false;
					kwFails++;
					kwRan++;
					break;
				}
			}

		//	String totals = "Success: " + kwSuccesses + "/" + kwRan + "<br>" + "Warnings: " + kwWarn + "/" + kwRan
//					+ "<br>" + "Failures: " + kwFails + "/" + kwRan + "<br>" + "Skipped: " + (totalKeywords - kwRan);
        int pass_perc=(kwSuccesses*100)/kwRan;
		int fail_perc=(kwFails*100)/kwRan;
		int warn_perc=(kwWarn*100)/kwRan;		
		
		String totals = "Success: " + kwSuccesses + "/" + kwRan + " ("+  pass_perc +  "% ) <br>" ;
		totals = totals + "Warnings: " + kwWarn + "/" + kwRan	+ "<br>" ; //" (" + warn_perc +  "% ) <br>" ;
		totals = totals + "Failures: " + kwFails + "/" + kwRan + " (" + fail_perc +  "% ) <br>" ;
		totals = totals + "Skipped: " + (totalKeywords - kwRan);

			if (runSuccess) {
				KDTDriver.ev.passScenario(id, "", "", "", totals);
				writeTestResult(dataSet.getRowIndex(), "Pass");
			} else if (kwFails == 0) {
				KDTDriver.ev.warnScenario(id, "", "", "", totals);
				writeTestResult(dataSet.getRowIndex(), "Warn");
			} else {
				KDTDriver.ev.failScenario(id, "", "", "", totals);
				writeTestResult(dataSet.getRowIndex(), "Fail");
			}
			// runContext.setLog(4);
			runContext.log(LogLevel.info, id + " completed.");

			// Cleanup context
			runContext.cleanup();

			notFirst = true;
		}
		return isSuccessful;
	}

	private void writeTestResult(int dataRowIndex, String message) {
		// Check if data sheet is used.
		if (dataSource == null)
			return;

		// Open the sheet
		Sheet sheet;
		try {
			sheet = dataSource.getDataSheet();
		} catch (IOException e) {
			TestContext.getContext().log(LogLevel.error, "Could not open data sheet for pass/fail writeback");
			return;
		}

		// Check for "Result" column
		final String RESULT = "Result";
		Row header = sheet.getRow(0);
		int resColumn = -1;
		for (Cell c : header) {
			if (c.toString().equals(RESULT)) {
				resColumn = c.getColumnIndex();
				break;
			}
		}

		// Result column doesn't exist, exit
		if (resColumn < 0) {
			return;
		}

		// Write result
		Row dataRow = sheet.getRow(dataRowIndex);
		Cell resultCell = dataRow.getCell(resColumn, Row.CREATE_NULL_AS_BLANK);
		resultCell.setCellValue(message);

		// Save file
		Workbook w = sheet.getWorkbook();
		try {
			FileTools.saveExcel(w, TestContext.getContext().getTestFilePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
