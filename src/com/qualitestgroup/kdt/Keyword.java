package com.qualitestgroup.kdt;

import static com.qualitestgroup.kdt.KDTConstants.COM;
import static com.qualitestgroup.kdt.KDTConstants.COMCOM;
import static com.qualitestgroup.kdt.KDTConstants.N;
import static com.qualitestgroup.kdt.KDTConstants.PKG;
import static com.qualitestgroup.kdt.KDTConstants.SAVE_VALUE;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.kdt.exceptions.KDTParseException;
import com.qualitestgroup.kdt.exceptions.KDTValidationException;
import com.qualitestgroup.util.logging.Logger.LogLevel;

/**
 * Abstract class for keywords. Implementing classes must provide override the
 * exec function. The init and cleanup methods can be overridden to provide more
 * functionality.
 * 
 * @author lenovo
 *
 */
public abstract class Keyword {

	protected Arguments args = new Arguments();
	protected static TestContext context;
	protected boolean gotoFlag = false;
	public boolean stopTestOnFail = true;
	public boolean skipTestOnPass = false;
	private boolean initialized = false;
	public String comment = "";
	public String label = "";
	public String failMessage = "";
	private static final boolean jar;

	private static final URL[] jarURLs;
	private static final ClassLoader jcl;

	static {
		File jarFolder;
		jarFolder = new File("./apps");
		jar = jarFolder.exists();
		if (jar) {
			File[] jars = jarFolder.listFiles();
			jarURLs = new URL[jars.length];
			for (int i = 0; i < jars.length; i++) {
				System.out.println(jars[i].toString());
				try {
					jarURLs[i] = jars[i].toURI().toURL();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			jarURLs = null;
		}
		jcl = jar ? new URLClassLoader(jarURLs) : ClassLoader.getSystemClassLoader();
	}

	/**
	 * Initializes the keyword. Implementing classes should override this method
	 * to validate arguments and check keyword preconditions. It is recommended
	 * that this superclass method be called in overriding implementations.
	 * 
	 * @param args
	 *            Arguments for this keyword
	 * @throws KDTKeywordInitException
	 */
	public void init() throws KDTKeywordInitException {
		context = TestContext.getContext();
		if (hasArgs("StopOnFail")) {
			if (args.get("StopOnFail").equals(N)) {
				stopTestOnFail = false;
			}
		}

		this.comment = "";

		initialized = true;
	}

	/**
	 * Executes the keyword.
	 * 
	 * @throws KDTKeywordExecException
	 * @throws  
	 */
	public abstract void exec() throws KDTKeywordExecException;

	/**
	 * Cleans up after the keyword. This is called even if init or exec fail.
	 */
	public void cleanup() {
		args.clear();
	}

	/**
	 * Cleans up after the keyword. This is called even if init or exec fail.
	 * 
	 * @param e
	 *            The exception raised during init or exec
	 */
	public void cleanup(Exception e) {
		this.cleanup();
	}

	protected boolean isKDTTrue(String value) {
		value = value == null ? null : value.toUpperCase();
		return "Y".equals(value) || "YES".equals(value) || "TRUE".equals(value);
	}

	protected boolean areKDTTrue(String... values) {
		boolean result = true;
		for (String v : values) {
			result = result && isKDTTrue(v);
		}
		return result;
	}

	/**
	 * Runs the keyword.
	 * 
	 * The init, exec, and cleanup methods
	 * 
	 * @throws KDTException
	 */
	public final void run() throws KDTException {
		context = TestContext.getContext();
		String name = this.getClass().getSimpleName();
		Exception initExecException = null;
		// Run method
		try {
			context.log(LogLevel.debug, "Args: " + args.toString());
			context.log(LogLevel.info, "Initializing keyword " + name);
			this.init();
			if (!initialized) {
				throw new KDTKeywordInitException("Error: Keyword init method must call super.init()");
			}
			context.log(LogLevel.info, "Executing keyword " + name);
			this.exec();
		} catch (KDTException e) {
			context.log(LogLevel.warn, "Keyword " + name
					+ ((e.getClass() == KDTKeywordInitException.class) ? " initialization fail: " : " execution fail: ")
					+ e.getMessage());
			if (e.getCause() != null) {
				context.log(LogLevel.warn, e.getCause().getMessage());
			}
			initExecException = e;
			throw e;

		} catch (NullPointerException npe) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			npe.printStackTrace(pw);
			sw.toString();
			KDTException e = new KDTException("Null pointer exception: " + sw.toString().replaceAll("\\n", "<BR>"));
			initExecException = e;
			throw e;
		} catch (Exception ee) {
			StringWriter sw = new StringWriter();
			ee.printStackTrace(new PrintWriter(sw));
			context.log(LogLevel.error, sw.toString());
			// KDTDriver.ev.otherException("Exception detail", sw.toString());
			KDTException e = new KDTException("Unknown exception", ee);
			initExecException = e;
			throw e;
		} finally {
			context.log(LogLevel.info, "Cleaning up keyword " + name);
			try {
				if (initExecException == null) {
					this.cleanup();
				} else {
					this.cleanup(initExecException);
				}
			} catch (Exception e) {
				context.log(LogLevel.error, "Error during cleanup: " + e.getMessage());
			}
			context.log(LogLevel.info, "Keyword " + name + " complete.");
		}
	}

	public final void addArg(String argName, String argValue) {
		args.put(argName, argValue);
	}

	/**
	 * Adds a comment to this keyword.
	 * 
	 * @param comment
	 *            The comment to add
	 * @author Brian Van Stone
	 */
	protected void addComment(String comment) {
		if (!this.comment.equals(""))
			this.comment += "</br>";
		this.comment += comment;
	}

	/**
	 * Adds a comment to this keyword.
	 * 
	 * @param comment
	 *            The comment to add
	 * @author Akshit Dwivedi
	 */
	protected void addFailMessage(String failMessage) {
		if (!this.failMessage.equals(""))
			this.failMessage += "</br>";
		this.failMessage += failMessage;
	}

	/**
	 * @Deprecated Use {@link Keyword.run(String application, String keyword,
	 *             String... args)} instead. {@see find2}
	 */
	@Deprecated
	public static final Keyword find(String keyword, String app) throws KDTParseException {
		// TestContext.getContext().log(LogLevel.warn, "Keyword.find(): method
		// deprecated. Use Keyword.run(String application, String keyword,
		// String... args) instead.");
		return find2(keyword, app);
	}

	/**
	 * Returns a Keyword object given the Keyword name and application.
	 * 
	 * @param keyword
	 *            Name of the keyword.
	 * @param app
	 *            Name of the application {@see KeywordGroup}
	 * @return The Keyword.
	 * @throws KDTParseException
	 *             If the keyword is not found.
	 */
	static final Keyword find2(String keyword, String app) throws KDTParseException {
		Keyword k;
		try {
			// Parse method name from keyword
			Class<? extends KeywordGroup> cl;
			try {
				String pack, kwgroup;
				if (app.contains(".")) {
					try {
						// Separate keyword group class name from package name
						String[] app2 = app.split("\\.", 2);
						pack = app2[0].toLowerCase();
						kwgroup = app2[1];

						// Check for class in the app's package.
						String kwClass = PKG + pack + "." + kwgroup;
						TestContext.getContext().log(LogLevel.debug, "Checking in " + kwClass);
						cl = Class.forName(kwClass, true, jcl).asSubclass(KeywordGroup.class);
					} catch (ClassNotFoundException e) {
						// Look for package with same name as the last part of
						// app.
						String[] app3 = app.split("\\.");
						pack = app3[0].toLowerCase();
						int last = app3.length - 1;
						String subpack = app3[last];

						kwgroup = "";
						for (int i = 1; i < last; i++) {
							kwgroup += app3[i] + ".";
						}

						// Check for class in the app's package.
						String kwClass = PKG + pack + "." + kwgroup + subpack.toLowerCase() + "." + subpack;
						TestContext.getContext().log(LogLevel.debug, "Checking in " + kwClass);
						cl = Class.forName(kwClass, true, jcl).asSubclass(KeywordGroup.class);
					}
				} else {
					// keyword group name = package name
					pack = app.toLowerCase();
					kwgroup = app;

					// Check for class in the app's package.
					String kwClass = PKG + pack + "." + kwgroup;
					TestContext.getContext().log(LogLevel.debug, "Checking in " + kwClass);
					cl = Class.forName(kwClass, true, jcl).asSubclass(KeywordGroup.class);
				}

			} catch (ClassNotFoundException e) {
				try {
					// If a class is not found above, check in common package
					String kwClass = PKG + COM + app;
					TestContext.getContext().log(LogLevel.debug, "Checking in " + kwClass);
					cl = Class.forName(kwClass, true, jcl).asSubclass(KeywordGroup.class);
				} catch (ClassNotFoundException e2) {
					// If a class is not found above, check for keyword in
					// common class
					String kwClass = PKG + COMCOM;
					TestContext.getContext().log(LogLevel.debug, "Checking in " + kwClass);
					cl = Class.forName(kwClass, true, jcl).asSubclass(KeywordGroup.class);
				}
			}

			// Get the keyword object.
			k = cl.newInstance().getK(keyword);

		} catch (Exception e) {
			// log.error("Keyword parse fail.");
			throw new KDTParseException("Unable to find keyword '" + keyword + "' in application '" + app + "'", e);
		}

		// Check if deprecated
		if (k.getClass().isAnnotationPresent(java.lang.Deprecated.class)) {
			throw new KDTParseException("Keyword " + keyword + " is deprecated.");
		}
		return k;
	}

	/**
	 * Runs a keyword.
	 * 
	 * @param application
	 *            The Application (KeywordGroup) containing the keyword.
	 * @param keyword
	 *            The name of the keyword.
	 * @param args
	 *            Arguments for the keyword. Alternates between Name and Value
	 *            (Name1, Value1, Name2, Value2, ...)
	 * @throws KDTException
	 *             If the keyword can't be found, the number of args is odd, or
	 *             the keyword throws an error.
	 */
	public static final Keyword run(String application, String keyword, String... args) throws KDTException {
		if (args.length % 2 != 0) {
			throw new KDTException("Odd number of argument name/values");
		}
		// System.out.println("App: " + application);
		// System.out.println("KW: " + keyword);
		Keyword KW = find2(keyword, application);

		for (int i = 0; i < args.length; i += 2) {
			String name = args[i];
			// System.out.println("Name: " + name);
			String value = args[i + 1];
			// System.out.println("Value: " + value);
			KW.addArg(name, value);
		}

		KW.run();
		return KW;
	}

	/**
	 * Checks whether the given arguments have been passed to this keyword.
	 * 
	 * @param argNames
	 *            The names of the arguments to check.
	 * @return True if all of the arguments are present.
	 */
	protected final boolean hasArgs(String... argNames) {
		for (String arg : argNames) {
			if (!args.containsKey(arg)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether at least one of the given arguments have been passed to
	 * this keyword.
	 * 
	 * @param argNames
	 *            The names of the arguments to check for
	 * @return True if at least one of the arguments is present
	 */
	protected final boolean hasAnyArgs(String... argNames) {
		for (String arg : argNames) {
			if (args.containsKey(arg)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Saves a value into a runtime variable. The variable name is specified by
	 * the SaveValue argument passed to the keyword.
	 * 
	 * @param value
	 *            The value to save.
	 * @return False if the SaveTo argument is not defined, true otherwise (and
	 *         the value is saved).
	 */
	protected final boolean saveValue(String value) {
		if (hasArgs(SAVE_VALUE)) {
			String var = args.get(SAVE_VALUE);
			if (var.charAt(0) != '$') {
				var = '$' + var;
			}

			context.getData().put(var, value);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Verifies whether the required arguments are passed to the keyword.
	 * 
	 * @param argNames
	 *            The names of the arguments to check for.
	 * @throws KDTKeywordInitException
	 *             If any of the arguments are not found.
	 */
	protected final void verifyArgs(String... argNames) throws KDTKeywordInitException {
		LinkedList<String> missing = new LinkedList<String>();
		for (String arg : argNames) {
			if (!args.containsKey(arg)) {
				missing.add(arg);
			}
		}
		if (missing.size() > 0) {

			throw new KDTKeywordInitException("Missing Arguments: " + missing.toString());
		}
	}

	/**
	 * Verifies that at least one of the args in argNames is passed to the
	 * keyword.
	 * 
	 * @param argNames
	 * @throws KDTKeywordInitException
	 *             If none of the arguments are found
	 */
	protected final void verifyOneOrMoreOfArgs(String... argNames) throws KDTKeywordInitException {
		if (!hasAnyArgs(argNames)) {
			throw new KDTKeywordInitException("Expected to find one or more of the arguments in "
					+ Arrays.toString(argNames) + " but found none.");
		}
	}

	/**
	 * 
	 * @param isSuccessful
	 * @param failMessage
	 * @throws KDTValidationException
	 */
	public final void validate(boolean isSuccessful, String failMessage) throws KDTValidationException {
		if (isSuccessful == false) {
			this.stopTestOnFail = true;
			this.comment += failMessage;
			throw new KDTValidationException(failMessage);
		}
	}

	/**
	 * 
	 * @param isSuccessful
	 * @param failMessage
	 * @param successMesssage
	 * @throws KDTValidationException
	 */
	public final void verify(boolean isSuccessful, String successMesssage, String failMessage)
			throws KDTValidationException {
		if (isSuccessful == false) {
			this.failMessage = this.failMessage
					+ "</br>------------------------------------------------------------------------------</br>"
					+ failMessage;
		} else {
			this.comment = this.comment + "<br>Validation: " + successMesssage + " <br>";
		}
	}

	/**
	 * 
	 * @throws KDTKeywordExecException
	 */
	public final void verifyAll() throws KDTKeywordExecException {
		if (this.failMessage.length() >= 1) {
			String failedMessage = this.failMessage;
			this.stopTestOnFail = true;
			throw new KDTKeywordExecException("Failed the Validation: \n " + failedMessage);
		} else {
			this.comment = this.comment + " Successfully verified all Validations.";
		}

	}

	public final void verifyAll(boolean b) throws KDTKeywordExecException {
		if (this.failMessage.length() >= 1) {
			String failedMessage = this.failMessage;
			// this.stopTestOnFail = true;
			this.stopTestOnFail = b;
			throw new KDTKeywordExecException("Failed the Validation: \n " + failedMessage);
		} else {
			this.comment = this.comment + " Successfully verified all Validations.";
		}
	}

	@Override
	public final String toString() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Class for storing argument name to value mappings.
	 * 
	 * @author Matt Swircenski
	 *
	 */
	public static class Arguments extends HashMap<String, String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3922549197222961149L;

		public String get(String key, String defaultValue) {
			return this.containsKey(key) ? this.get(key) : defaultValue;
		}
	}
	
}
