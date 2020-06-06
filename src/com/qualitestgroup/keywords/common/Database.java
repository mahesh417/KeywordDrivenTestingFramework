package com.qualitestgroup.keywords.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.qualitestgroup.kdt.Keyword;
import com.qualitestgroup.kdt.KeywordGroup;
import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataDriverBuilder;
import com.qualitestgroup.util.datadriver.DataDriverFactory;
import com.qualitestgroup.util.datadriver.DataRow;
import com.qualitestgroup.util.datadriver.DataSource;
import com.qualitestgroup.util.db.ConnectionUtils;
import com.qualitestgroup.util.db.Query;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.kdt.exceptions.KDTException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordExecException;
import com.qualitestgroup.kdt.exceptions.KDTKeywordInitException;
import com.qualitestgroup.kdt.exceptions.KDTParseException;
import com.qualitestgroup.util.logging.Logger.LogLevel;

public class Database extends KeywordGroup {

	private static boolean LOADED_INT_SEC_LIBS = false;
	private static File lib_dir;

	static {
		String arch = System.getProperty("os.arch");
		try {

			if (arch.equalsIgnoreCase("x86")) {
				lib_dir = new File("./ExternalLibs/sqljdbc_libs/auth/x86");
			} else {
				lib_dir = new File("./ExternalLibs/sqljdbc_libs/auth/x64");
			}
			addDir(lib_dir.getAbsolutePath());

			LOADED_INT_SEC_LIBS = true;
		} catch (IOException e) {
			LOADED_INT_SEC_LIBS = false;
		}
	}

	private static void addDir(String s) throws IOException {
		try {
			// This enables the java.library.path to be modified at runtime
			// From a Sun engineer at
			// http://forums.sun.com/thread.jspa?threadID=707176
			//
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[]) field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length + 1];
			System.arraycopy(paths, 0, tmp, 0, paths.length);
			tmp[paths.length] = s;
			field.set(null, tmp);
			System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			throw new IOException("Failed to get field handle to set library path");
		}
	}

	/**
	 * Connects to a Database. If given a name for the connection, the
	 * connection can be later referenced using that name.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><b>Username:</b> (Required if CredentialsDataSource not passed) The
	 * username to log in to the database with.</li>
	 * <li><b>Password:</b> (Required if CredentialsDataSource not passed) The
	 * password to log in to the database with.</li>
	 * <li><b>Type:</b> (Required if CredentialsDataSource not passed) The type
	 * of database you're connecting to.</li>
	 * <li><b>Address:</b> (Required if CredentialsDataSource not passed) The
	 * address of the database.</li>
	 * <li><b>ConnectionName:</b> (Optional) The custom name for this
	 * connection.</li>
	 * <li><b>CredentialsDataSource:</b> (Optional, if passed will override the
	 * parameters [Username, Password, Type, Address, ConnectionName]) Reference
	 * to a CSV or Excel worksheet with the following required column names:
	 * [Username, Password, Type, Address] and the following optional column
	 * names: [ConnectionName].</li>
	 * <li><
	 * 
	 * @author Qualitest
	 * 
	 */
	public static class Connect extends Keyword {
		private String username;
		private String password;
		private String connType;
		private String address;
		private String connName;
		private boolean integratedSecurity;

		private static final String UNAME = "Username";
		private static final String PWORD = "Password";
		private static final String TYPE = "Type";
		private static final String ADDR = "Address";
		private static final String NAME = "ConnectionName";
		private static final String CREDS_SRC = "CredentialsDataSource";
		private static final String INT_SEC = "IntegratedSecurity";

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			if (!hasArgs(CREDS_SRC)) {
				verifyArgs(TYPE, ADDR);

				username = args.get(UNAME);
				password = args.get(PWORD);
				connType = args.get(TYPE);
				address = args.get(ADDR);
				integratedSecurity = isKDTTrue(args.get(INT_SEC));

				if (!integratedSecurity) {
					verifyArgs(UNAME, PWORD);
				}

				if (hasArgs(NAME)) {
					connName = args.get(NAME);
				} else {
					connName = "";
				}
			} else {
				verifyArgs(CREDS_SRC);

				DataSource ds = null;
				try {
					ds = new DataSource(context.getTestFilePath(), "", args.get(CREDS_SRC));
					DataDriver dd = ds.getDataDriver();

					if (!dd.hasColumns(UNAME, PWORD, TYPE, ADDR)) {
						throw new KDTKeywordInitException("Required columns for an external data source are: [" + UNAME
								+ "," + PWORD + "," + TYPE + "," + ADDR + "]. Optional arguments are: [" + NAME + "].");
					}

					DataRow row = dd.nextRow();

					username = row.getItem(UNAME);
					password = row.getItem(PWORD);
					connType = row.getItem(TYPE);
					address = row.getItem(ADDR);
					integratedSecurity = isKDTTrue(row.getItem(INT_SEC));

					if (dd.hasColumns(NAME)) {
						connName = row.getItem(NAME);
					} else {
						connName = "";
					}
				} catch (KDTParseException e) {
					e.printStackTrace();
					throw new KDTKeywordInitException(
							"There was a problem parsing the " + CREDS_SRC + " parameter's input data:", e);
				}
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {

			try {
				if (integratedSecurity) {
					if (!LOADED_INT_SEC_LIBS) {
						throw new KDTKeywordExecException(
								"Unexpected I/O error occurred while attempting to load MS SQL auth lib. [" + lib_dir
										+ "]");
					}

					context.setConn(ConnectionUtils.createConnection(address, connType), connName);
				} else {
					context.setConn(ConnectionUtils.createConnection(address, username, password, connType), connName);
				}
			} catch (SQLException e) {
				throw new KDTKeywordExecException("Unable to connect to DB", e);
			}
		}
	}

	/**
	 * Base class for keywords that use a database connection. If a connection
	 * name is not given, the last connection opened is used.
	 * 
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><i>ConnectionName:</i> The custom name of the connction to use.</li>
	 * </ul>
	 * 
	 * @author QualiTest
	 * 
	 */
	/*
	 * public abstract static class ConnectionKeyword extends Keyword { private
	 * static final String CONN = "ConnectionName"; protected Connection
	 * myConnection;
	 * 
	 * @Override public void init() throws KDTKeywordInitException {
	 * super.init(); if (hasArgs(CONN)) { myConnection =
	 * context.getConn(args.get(CONN)); } else { myConnection =
	 * context.getConn(); } } }
	 */

	/**
	 * <p>
	 * <b>NOTE:</b> This keyword is used as base for other keywords which desire
	 * database connectivity. It should not be used directly in the test file.
	 * </p>
	 * 
	 * <p>
	 * <b>Description:</b> Abstract keyword that other keywords may use. This
	 * keyword manages connection state so that keywords which subclass it do
	 * not need to handle those details.
	 * </p>
	 * 
	 * <p>
	 * <b>Arguments:</b>
	 * <ul>
	 * <li><b>ConnectionName:</b> (Optional) The name of the existing connection
	 * or new connection that is created</li>
	 * <li><b>ConfigurationData:</b> (Optional) The name of the configuration
	 * loaded from a LoadConfigurationData keyword invocation</li>
	 * <li><b>CloseConnectionOnComplete:</b> (Optional) If provided, will close
	 * connection in keyword cleanup</li>
	 * <li><b>RollbackOnError:</b> (Optional) If provided, will rollback the
	 * database transaction in keyword cleanup</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws KDTKeywordInitException,
	 *             KDTKeywordExecException
	 * @author Qualitest
	 *
	 */
	public static abstract class ConnectionKeyword extends Keyword {
		protected static final String ARG_CONN = "ConnectionName";
		protected static final String ARG_CLOSE = "CloseConnectionOnComplete";
		protected static final String ARG_CONFIG = "ConfigurationData";
		protected static final String ARG_ROLLBACK_ON_ERROR = "RollbackOnError";

		protected static final String ARG_UNAME = "Username";
		protected static final String ARG_PWORD = "Password";
		protected static final String ARG_ADDRESS = "Address";
		protected static final String ARG_TYPE = "Type";
		protected static final String ARG_INT_SEC = "IntegratedSecurity";

		private boolean integratedSecurity = false;

		@SuppressWarnings("serial")
		private static final List<String> NON_CONFIG_REQUIRED_ARGS = new LinkedList<String>() {
			{
				add(ARG_UNAME);
				add(ARG_PWORD);
				add(ARG_ADDRESS);
			}
		};

		@SuppressWarnings("serial")
		private static final List<String> ALL_NON_CONFIG_ARGS = new LinkedList<String>() {
			{
				addAll(NON_CONFIG_REQUIRED_ARGS);
				add(ARG_TYPE);
				add(ARG_INT_SEC);
			}
		};

		protected static final String DEF_ARG_TYPE = "Oracle";

		protected Connection myConnection;
		protected Map<String, Connection> myConnections;
		protected String myConnectionName;
		private boolean rollback = false;
		private boolean oldCommitSetting = false;
		private boolean configMode = true; // false indicates direct args
											// instead of config loaded thru
											// LoadConfigurationData

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();

			myConnections = new HashMap<String, Connection>();
			Properties p = null;

			// conn name is required regardless for context storage
			verifyArgs(ARG_CONN);
			String config = null;

			if (hasArgs(ARG_CONFIG)) {
				// if using config, can't specific non config args
				if (hasAnyArgs(ALL_NON_CONFIG_ARGS.toArray(new String[0]))) {
					throw new KDTKeywordInitException(
							"Cannot specify any of " + ALL_NON_CONFIG_ARGS + " when specifying " + ARG_CONFIG);
				}

				// indicates that we are using config instead of direct keyword
				// arguments
				configMode = true;

				// time to fetch the properties file
				config = args.get(ARG_CONFIG);
				p = (Properties) (context.retrieve(config));

				// if we couldn't fetch it, LoadConfigurationData wasn't
				// properly run before this to read the file
				if (p == null) {
					throw new KDTKeywordInitException("Unable to fetch Properties object."
							+ "Expected it to be returned by context.retrieve(" + config + ")");
				}
			} else {
				verifyArgs(NON_CONFIG_REQUIRED_ARGS.toArray(new String[0]));
				integratedSecurity = isKDTTrue(args.get(ARG_INT_SEC));
				configMode = false;
			}

			// get rollback arg
			rollback = isKDTTrue(args.get(ARG_ROLLBACK_ON_ERROR));

			if (configMode) {
				for (Map.Entry<String, String> e : args.entrySet()) {
					if (e.getKey().startsWith(ARG_CONN)) {
						// fetch the requested connection
						Connection conn = context.getConn(e.getValue());

						boolean isNullOrClosed = false;
						try {
							isNullOrClosed = conn == null ? true : conn.isClosed();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						if (isNullOrClosed) {
							// parse the connection info from the properties
							// object
							String connInfoString = p.getProperty(e.getValue());
							String[] connectionInfo = connInfoString.split(",");
							String uname = "";
							String pword = "";
							String address = "";
							String type = "Oracle";
							if (connectionInfo.length == 3 || connectionInfo.length == 4) {
								uname = connectionInfo[0].trim();
								pword = connectionInfo[1].trim();
								address = connectionInfo[2].trim();
								if (connectionInfo.length == 4) {
									type = connectionInfo[3].trim();
								}
							} else {
								throw new KDTKeywordInitException(
										"Invalid DB connection configuration specified in " + config);
							}

							// connect to the database
							try {
								Keyword.run("Database", "Connect", "Username", uname, "Password", pword, "Address",
										address, "Type", type, "ConnectionName", e.getValue());
							} catch (KDTException e1) {
								e1.printStackTrace();
								throw new KDTKeywordInitException("Error opening connection to DB", e1);
							}

							// fetch the newly created connection
							conn = context.getConn(e.getValue());
						} else {
							if (rollback) {
								try {
									oldCommitSetting = conn.getAutoCommit();
									conn.setAutoCommit(false);
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
							}
						}

						myConnections.put(e.getValue(), conn);
						myConnection = conn;
						myConnectionName = e.getValue();
					}
				}
			} else {
				String connName = args.get(ARG_CONN);

				// fetch the requested connection
				Connection conn = context.getConn(connName);

				// see if connection exists or is closed
				boolean isNullOrClosed = false;
				try {
					isNullOrClosed = conn == null ? true : conn.isClosed();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				if (isNullOrClosed) {
					// get the connection info from the args
					String uname = args.get(ARG_UNAME);
					String pword = args.get(ARG_PWORD);
					String address = args.get(ARG_ADDRESS);
					String type = args.get(ARG_TYPE, DEF_ARG_TYPE);

					// connect to the database
					try {
						if (!integratedSecurity) {
							Keyword.run("Database", "Connect", "Username", uname, "Password", pword, "Address", address,
									"Type", type, "ConnectionName", connName);
						} else {
							Keyword.run("Database", "Connect", "Address", address, "Type", type, "IntegratedSecurity",
									"true", "ConnectionName", connName);
						}

					} catch (KDTException e) {
						e.printStackTrace();
						throw new KDTKeywordInitException("Error opening connection to DB", e);
					}
				} else {
					if (rollback) {
						try {
							oldCommitSetting = conn.getAutoCommit();
							conn.setAutoCommit(false);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}

				myConnections.put(connName, conn);
				myConnection = conn;
				myConnectionName = connName;
			}

			// if myConnection is still null, use the latest connection
			if (myConnection == null) {
				myConnection = context.getConn();
				myConnectionName = context.getConnName();
			}
		}

		@Override
		public void cleanup() {
			boolean close = hasArgs(ARG_CLOSE) && isKDTTrue(args.get(ARG_CLOSE));

			if (close || rollback) {
				for (Connection conn : myConnections.values()) {
					try {
						if (conn != null && !conn.isClosed()) {
							if (rollback) {
								conn.commit();
								conn.setAutoCommit(oldCommitSetting);
							}
							if (close) {
								conn.close();
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			super.cleanup();
		}

		@Override
		public void cleanup(Exception e) {
			boolean close = hasArgs(ARG_CLOSE) && isKDTTrue(args.get(ARG_CLOSE));

			for (Connection conn : myConnections.values()) {
				try {
					if (conn != null && !conn.isClosed()) {
						if (rollback) {
							conn.rollback();
						}
						if (close) {
							conn.close();
						}
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}

			super.cleanup();
		}
	}

	/**
	 * Executes a query on a database connection. One of the query arguments
	 * must be passed, and they are all mutually exclusive.
	 * <p>
	 * Arguments:
	 * <ul>
	 * <li><i>QueryString:</i> The query to execute.</li>
	 * <li><i>QueryFile:</i> Path to a file containing the query.</li>
	 * <li><i>QueryFolder:</i> Path to a folder containing multiple query files
	 * to execute.</li>
	 * <li><i>See {@link ConnectionKeyword} for more arguments</i></li>
	 * </ul>
	 * 
	 * @author QualiTest
	 * 
	 */
	public static class ExecuteQuery extends Keyword {
		private static final String CONN = "ConnectionName";

		protected Connection myConnection;
		public static Boolean resultFlag = false;
		private String query;
		private boolean folder = false;
		private String savePath;

		@Override
		public void init() throws KDTKeywordInitException {
			super.init();
			if (hasArgs(CONN)) {
				myConnection = context.getConn(args.get(CONN));
			} else {
				myConnection = context.getConn();
			}

			if (hasArgs("QueryString")) {
				query = args.get("QueryString");
			} else if (hasArgs("QueryFile")) {
				try {
					BufferedReader bf = new BufferedReader(FileTools.openTxt(args.get("QueryFile")));
					StringBuilder sb = new StringBuilder();

					String line = bf.readLine();
					while (line != null) {
						sb.append(line);
						sb.append("\n");
						line = bf.readLine();
					}
					bf.close();
					query = sb.toString();
				} catch (java.io.IOException e) {
					throw new KDTKeywordInitException("Could not read file", e);
				}
			} else if (hasArgs("QueryFolder")) {
				folder = true;
				query = args.get("QueryFolder");
			} else {
				throw new KDTKeywordInitException("Need either QueryString or QueryFile argument.");
			}

			if (hasArgs("SaveResultPath")) {
				savePath = args.get("SaveResultPath");
			}

			if (myConnection == null) {
				throw new KDTKeywordInitException("Database connection not open");
			}
		}

		@Override
		public void exec() throws KDTKeywordExecException {
			String curSQL = "";
			if (folder) {
				File fold = new File(query);
				DataDriverBuilder dda = new DataDriverBuilder();
				for (File f : fold.listFiles()) {
					if (f.isFile()) {
						try {
							context.log(LogLevel.info, "Executing query " + f.getName());
							BufferedReader bf = new BufferedReader(new FileReader(f));
							StringBuilder sb = new StringBuilder();
							String line = bf.readLine();
							while (line != null) {
								sb.append(line);
								sb.append("\n");
								line = bf.readLine();
							}
							bf.close();
							System.out.println(sb);
							String query = sb.toString().trim();
							curSQL = f.getName();
							executeQueries(query, dda);
						} catch (FileNotFoundException e) {
							throw new KDTKeywordExecException("Query file does not exist", e);
						} catch (IOException e) {
							throw new KDTKeywordExecException("Could not read the query file", e);
						} catch (SQLException e) {
							System.err.println(e.getErrorCode());
							e.printStackTrace();
							throw new KDTKeywordExecException("Could not execute the query " + curSQL, e);
						}

					}

				}

				if (savePath != null) {
					DataDriver total = dda.toDataDriver();
					FileTools.saveDataDriver(total, savePath);
				}
			} else {
				DataDriverBuilder dda = new DataDriverBuilder();

				try {
					executeQueries(query, dda);
				} catch (SQLException e) {
					throw new KDTKeywordExecException("Could not execute the query " + curSQL, e);
				}

				if (savePath != null) {
					DataDriver total = dda.toDataDriver();
					FileTools.saveDataDriver(total, savePath);
				}
			}
			// } catch (Exception e) {
			// e.printStackTrace();
			// throw new KDTKeywordExecException("Error exectuting query", e);
			// }
		}

		/**
		 * Splits a string possibly containing multiple queries into separate
		 * queries, and executes them.
		 * 
		 * @param query
		 *            The query string to execute.
		 * @param results
		 *            The DataDriverBuilder to append the query results to.
		 * @throws SQLException
		 */
		private void executeQueries(String query, DataDriverBuilder results) throws SQLException {
			String[] queries = query.split(";");
			for (String qq : queries) {

				// Remove comments
				qq = qq.replaceAll("(?m)^(--.*|)?$|/\\*.*\\*/", "");
				qq = qq.trim();
				if (!qq.isEmpty() && !qq.toUpperCase().equals("EXIT")) {
					ResultSet rs = Query.OpenQuery(myConnection, qq);
					if (savePath != null) {
						DataDriver dd = DataDriverFactory.create(rs);
						results.appendDataDriver(dd);
						resultFlag = false;
						Query.closeResultSet(rs);
					}
				}

			}
		}
	}
	
	/**
	 * Splits a string possibly containing multiple queries into separate
	 * queries, and executes them.
	 * 
	 * @param jdbc_url
	 *  To connect with Amazon data base
	 * @param dburl
	 *            To connect with SQL data base url
	 * @param db_port
	 *            Port name of the data base name
	 * @param db_name
	 *            Data base name
	 * @param db_username
	 *            Data base user name
	 * @param db_password
	 *            Data base password
	 * @param verification_query
	 *           Query to execute
	 * @param sql_driver
	 *            SQL driver
	 * @throws SQLException
	 */
	public static ResultSet database_connection(String jdbc_url, String dburl, String db_port, String db_name,
			String db_username, String db_password, String verification_query, String sql_driver) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			Class.forName(sql_driver);
			conn = DriverManager.getConnection(jdbc_url + dburl + ":" + db_port + "/" + db_name, db_username,db_password);
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(verification_query);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}