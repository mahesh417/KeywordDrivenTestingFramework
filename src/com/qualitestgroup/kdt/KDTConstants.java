package com.qualitestgroup.kdt;


/**
 * String constants for use within KDT.
 * @author Matthew Swircenski
 *
 */
public final class KDTConstants {
	
	// Constants
	//		Column names	
	public static final String TEST_CASE = "Test Scenario ID";
	public static final String EXECUTE = "Execute?";		
	public static final String LAST_RUN = "LastRun";
	public static final String LAST_RESULT = "LastResult";
	public static final String LAST_LOG = "LastLog";
	public static final String LAST_REPORT = "LastReport";
	public static final String LOG_LEVEL = "LogLevel";
	public static final String TEST_STEPS = "Test Case Steps";
	public static final String SCEN_SHEET = "Scenario Sheet";
	public static final String TEST_CASES = "Test Scenarios";
	public static final String SCEN_ID = "Test Case ID";	
	public static final String DATA_SHEET = "Data Sheet";
	public static final String TEST_DATA = "Test Data";
	public static final String KEYWORD = "Keyword";
	public static final String APP = "Application";
	public static final String ARGS = "Arguments";
	public static final String VALUES = "Values";
	public static final String DEPEND = "Dependencies";
	
	public static final String[] scenColNames = {SCEN_ID, KEYWORD, APP, ARGS, VALUES};
	public static final String[] caseReqColumns = {TEST_CASE, EXECUTE, SCEN_ID};
	public static final String[] caseOptColumns = {LAST_RUN, LAST_RESULT, LAST_LOG, LAST_REPORT, LOG_LEVEL, DATA_SHEET, SCEN_SHEET, DEPEND};
	
	//		Cell values
	public static final String Y = "Y";
	public static final String N = "N";
	public static final String NA = "N/A";		
	
	//		Package names
	public static final String PKG = "com.qualitestgroup.keywords.";
	public static final String COM = "common.";
	public static final String COMCOM = "common.Common";

	public static final String SAVE_VALUE = "SaveTo";
	public static final String GOTO_STEP = "GoToStep";
	public static final String GOTO_DELTA = "GoToDelta";
	public static final String GOTO_KEYWORD = "GoToKeyword";
	public static final String GOTO_LABEL = "GoToLabel";
}
