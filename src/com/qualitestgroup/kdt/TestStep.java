package com.qualitestgroup.kdt;

import static com.qualitestgroup.kdt.KDTConstants.GOTO_DELTA;
import static com.qualitestgroup.kdt.KDTConstants.GOTO_KEYWORD;
import static com.qualitestgroup.kdt.KDTConstants.GOTO_LABEL;
import static com.qualitestgroup.kdt.KDTConstants.GOTO_STEP;
import static com.qualitestgroup.kdt.KDTConstants.SAVE_VALUE;

import java.io.IOException;
import java.util.Map.Entry;

import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTParseException;
import com.qualitestgroup.util.logging.Logger.LogLevel;

/**
 * Representation of a test step.
 * 
 * Stores the keyword object and its arguments.
 * 
 * @author Matthew Swircenski
 *
 */
public class TestStep {

	private Keyword kw;
	private Keyword.Arguments arguments;
	private String testCase;
	private String id;
	public String kwName;
	public boolean warning = false;
	public boolean skip = false;
	public boolean gotoFlag = false;
	private String gotoType;
	private String gotoValue;

	static boolean haltOnError = false;

	/**
	 * Creates a Test Step. If the keyword is not found in the given
	 * application, the Universal class is searched.
	 * 
	 * @param keyword
	 *            The keyword to run.
	 * @param app
	 *            The application to find the keyword in.
	 * @param testCase
	 *            The test case ID.
	 * @param logLevel
	 *            The log level.
	 * @throws KDTParseException
	 *             If keyword parsing fails.
	 */
	public TestStep(String keyword, String app, String testCase, String ID) throws KDTParseException {
		TestContext.getContext().log(LogLevel.debug, "Loading keyword '" + keyword + "' in app '" + app + "'");
		this.testCase = testCase;
		this.arguments = new Keyword.Arguments();
		this.kw = Keyword.find2(keyword, app);
		this.kwName = keyword;
		this.id = ID;
	}

	/**
	 * Fetches the goto type, if any, of this TestStep
	 * 
	 * @return a string representing the goto type
	 */
	public String getGotoType() {
		processGotoArgs();
		return gotoType;
	}

	/**
	 * Fetches the goto value, if any, of this TestStep
	 * 
	 * @return a string representing the goto type
	 */
	public String getGotoValue() {
		processGotoArgs();
		return gotoValue;
	}

	/**
	 * Fetches the label of the underlying kw for use as a go to target
	 * 
	 * @return The label
	 */
	public String getLabel() {
		return kw.label;
	}

	/**
	 * Checks the keyword arguments to parse out information about the arguments
	 * for the goto functionality
	 */
	private void processGotoArgs() {
		if (arguments.containsKey(GOTO_STEP)) {
			gotoType = GOTO_STEP;
			gotoValue = arguments.get(GOTO_STEP);
		} else if (arguments.containsKey(GOTO_DELTA)) {
			gotoType = GOTO_DELTA;
			gotoValue = arguments.get(GOTO_DELTA);
		} else if (arguments.containsKey(GOTO_KEYWORD)) {
			gotoType = GOTO_KEYWORD;
			gotoValue = arguments.get(GOTO_KEYWORD);
		} else if (arguments.containsKey(GOTO_LABEL)) {
			gotoType = GOTO_LABEL;
			gotoValue = arguments.get(GOTO_LABEL);
		}
	}

	public boolean gotoFlag() {
		return kw.gotoFlag;
	}

	/**
	 * Adds an argument for the keyword. Values beginning with a '$' are
	 * substituted with a value from the current data set at runtime.
	 * 
	 * @param arg
	 *            The name of the argument.
	 * @param val
	 *            The value of the argument.
	 */
	public void addArgument(String arg, String val) {
		arguments.put(arg, val);
		if (arg.equals("Label")) {
			this.kw.label = val;
		}
	}

	/**
	 * Runs the keyword. Substitutes values from the data set into arguments
	 * with variable values.
	 * 
	 * @throws KDTException
	 *             If the keyword fails to execute and the keyword's
	 *             {@link Keyword.stopTestOnFail} attribute is set to true.
	 */
	public boolean run() throws KDTException {
		boolean ret = true;
		TestContext context = TestContext.getContext();
		// Load keyword arguments
		for (Entry<String, String> var : arguments.entrySet()) {
			// Replace variable names with their values.
			if (!var.getValue().isEmpty() && var.getValue().charAt(0) == '$' && !var.getKey().equals(SAVE_VALUE)) {
				TestContext.Data data = context.getData();
				String value = data.get(var.getValue());
				if (value == null) {
					KDTDriver.ev.warnTC(testCase, id, kw.getClass().getSimpleName(), "",
							"Missing data variable: " + var.getValue());
					throw new KDTException("Missing data variable: " + var.getValue());
				}
				kw.addArg(var.getKey(), value);
			} else {
				kw.addArg(var.getKey(), var.getValue());
			}
		}

		String argString = kw.args.toString();

		try {
			kw.run();
		} catch (KDTException e) {

			StringBuilder errMessage = new StringBuilder(e.getMessage());
			Throwable tmp = e;
			while (tmp.getCause() != null) {
				tmp = tmp.getCause();
				errMessage.append("<br>");
				errMessage.append(tmp.getMessage());
			}
			if (kw.stopTestOnFail) {
				KDTDriver.ev.failTC(testCase, id, kw.getClass().getSimpleName(), errMessage.toString(), kw.comment);
			} else {
				KDTDriver.ev.warnTC(testCase, id, kw.getClass().getSimpleName(), errMessage.toString(), kw.comment);
				warning = true;
			}

			ret = false;
			if (haltOnError) {
				try {
					System.in.read();
				} catch (IOException e1) {
				}
			}

			if (context.getLogLevel() == LogLevel.debug || context.getLogLevel() == LogLevel.trace) {
				KDTDriver.ev.debug("Arguments: " + argString);
			}
			if (context.getLogLevel() == LogLevel.trace) {
				KDTDriver.ev.debug("<a href=\"screenshots/" + KDTDriver.ev.screenshot() + "\">Screen Shot</a>");
			}

			if (kw.stopTestOnFail) {
				throw e;
			}
		}
		if (ret) {
			KDTDriver.ev.passTC(testCase, id, kw.getClass().getSimpleName(), "", kw.comment);

			if (kw.skipTestOnPass) {
				skip = true;
			}

			if (context.getLogLevel() == LogLevel.debug || context.getLogLevel() == LogLevel.trace) {
				KDTDriver.ev.debug("Arguments: " + argString);
			}
			if (context.getLogLevel() == LogLevel.trace) {
				KDTDriver.ev.debug("<a href=\"screenshots/" + KDTDriver.ev.screenshot() + "\">Screen Shot</a>");
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		return kw.toString();
	}

}
