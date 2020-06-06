package com.qualitestgroup.kdt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Stack;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.qualitestgroup.util.logging.Logger;
import com.qualitestgroup.util.logging.Logger.LogLevel;

/**
 * Singleton class for common context within a test run.
 * 
 * @author Matthew Swircenski
 *
 */
public class TestContext {

	// Singleton
	private static TestContext me = null;
	private WebDriver driver;
	private Data data;
	private HashMap<String, Object> storage;
	private HashMap<String, Object> globalStorage;
	private HashMap<String, Connection> connections;
	private Connection latestConn;
	private String latestConnName;
	private Logger log;
	private String testFilePath;

	private Stack<TestContext> tcStack = new Stack<TestContext>();

	private TestContext() {
		log = new Logger();
		connections = new HashMap<String, Connection>();
		storage = new HashMap<String, Object>();
		globalStorage = new HashMap<String, Object>();
	}

	public void pushContext() {
		tcStack.push(me);
		me = new TestContext();
	}

	public void popContext() {
		if (!tcStack.isEmpty()) {
			cleanup();
			me = tcStack.pop();
		}
	}

	/**
	 * Cleans up any remaining data in the
	 */
	public void cleanup() {
		if (driver != null) {
			System.out.println("Closing driver");
			// Repeatedly try to close all windows
			try {
				int latestSize = Integer.MAX_VALUE;
				int loops = 0;
				for (int size = driver.getWindowHandles().size(); size > 0; size = driver.getWindowHandles().size()) {
					if (size < latestSize) {
						latestSize = size;
						loops = 0;
					} else {
						loops++;
					}

					if (loops > 100) {
						throw new RuntimeException("Unable to close browser windows");
					}

					for (String window : driver.getWindowHandles()) {

						try {
							driver.switchTo().window(window);
							System.out.println("Closing " + window);
							System.out.println(driver.getTitle());
							String userAgent = (String) ((JavascriptExecutor) driver)
									.executeScript("return navigator.userAgent;", new Object[0]);
							System.out.println(userAgent);
							if (userAgent.toLowerCase().contains("msie 8.0")) {
								((JavascriptExecutor) driver).executeScript("window.open('','_self').close();",
										new Object[0]);
							} else {
								driver.quit();
							}
						} catch (Exception e) {
							// System.err.println(e.getMessage());
							// e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				driver.quit();
			} catch (UnreachableBrowserException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			System.out.println("Driver closed");
		}
		for (Connection conn : connections.values())
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		driver = null;
		data = null;
		storage.clear();
	}

	/**
	 * Returns the singleton TestContext object.
	 * 
	 * @return
	 */
	public static TestContext getContext() {
		// Create a new TestContext if it doesn't exist already.
		if (me == null) {
			me = new TestContext();
		}
		return me;
	}

	/**
	 * Sets the data set for the current test run.
	 * 
	 * @param data
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Sets the WebDriver for the current test run.
	 * 
	 * @param wd
	 */
	public void setWebDriver(WebDriver wd) {
		driver = wd;
	}

	/**
	 * Sets the log level for the current test run.
	 * 
	 * @param logLevel
	 */
	public void setLog(int logLevel) {
		log.changeLogLevel(LogLevel.get(logLevel));
	}

	public void setConn(Connection conn, String name) {
		connections.put(name, conn);
		latestConnName = name;
		latestConn = conn;
	}

	public void setConn(Connection conn) {
		connections.put("", conn);
		latestConnName = "";
		latestConn = conn;
	}

	public void setTestFilePath(String filePath) {
		testFilePath = filePath;
	}

	/**
	 * Returns the current WebDriver.
	 * 
	 * @return
	 */
	public WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * Returns the current data set.
	 * 
	 * @return
	 */
	public Data getData() {
		return data;
	}

	public Connection getConn() {
		return latestConn;
	}

	public String getConnName() {
		return latestConnName;
	}

	public Connection getConn(String name) {
		return connections.get(name);
	}

	public String getTestFilePath() {
		return testFilePath;
	}

	public void store(String key, Object value) {
		storage.put(key, value);
	}

	public Object retrieve(String key) {
		return storage.get(key);
	}

	public void storeGlobal(String key, Object value) {
		globalStorage.put(key, value);
	}

	public Object retrieveGlobal(String key) {
		return globalStorage.get(key);
	}

	/**
	 * Writes a log message.
	 * 
	 * @param level
	 *            The Log level of the message
	 * @param message
	 */
	public void log(LogLevel level, String message) {
		switch (level) {
		case debug:
			Logger.debug(message);
			break;
		case error:
			Logger.error(message);
			break;
		case fatal:
			Logger.fatal(message);
			break;
		case info:
			Logger.info(message);
			break;
		case trace:
			Logger.trace(message);
			break;
		case warn:
			Logger.warning(message);
			break;
		default:
			break;
		}
	}

	public LogLevel getLogLevel() {
		return log.getLogLevel();
	}

	/**
	 * Class for storing variable-to-value mappings for the test context.
	 * 
	 * @author Matthew Swircenski
	 *
	 */
	public static class Data extends HashMap<String, String> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7287830677076141358L;

		private int rowIndex;

		public int getRowIndex() {
			return rowIndex;
		}

		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}

		public Data(int initialCapacity) {
			super(initialCapacity);
		}

		public Data() {
			super();
		}
	}

}
