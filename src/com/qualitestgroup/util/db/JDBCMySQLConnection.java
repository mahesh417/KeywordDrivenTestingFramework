package com.qualitestgroup.util.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.qualitestgroup.util.logging.Logger;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author QualiTest
 *
 */

public class JDBCMySQLConnection {

	private Connection connection = null;
	String TNS_Names = null;

	public Connection Connector(String DB_Address, String DB_Type, String DB_UserName, String DB_Password)
			throws SQLException {
		Logger log = new Logger();
		System.out.println(log);
		// log.fatal(connection);
		// Setting tnsnames.ora location
		System.setProperty("oracle.net.tns_admin", System.getProperty("user.dir"));
		// String oracle = "oracle";
		// creating object od
		OracleDataSource ods = new OracleDataSource();
		// setting reference to tns name
		ods.setTNSEntryName(DB_Address);
		/*
		 * // Load tns entry manually to use thin client instead of oci
		 * 
		 * String tnsRegex = "^" + DB_Address + "=$(.*?)^$"; // Matches
		 * DB_Address followed by '=', then captures // all following input
		 * until a blank line is found.
		 * 
		 * //System.out.println(tnsRegex); int tnsFlags = Pattern.DOTALL |
		 * Pattern.MULTILINE; // Enable multiline and dotall modes. Pattern
		 * tnsPattern = Pattern.compile(tnsRegex, tnsFlags);
		 * 
		 * // Load the TNS file for parsing String tnsFile; try{ tnsFile =
		 * FileTools.readerToString(FileTools.openTxt("tnsnames.ora")); } catch
		 * (IOException e) { throw new SQLException("Unable to open TNS file.");
		 * }
		 * 
		 * //System.out.println(tnsPattern.toString());
		 * 
		 * 
		 * Matcher tnsMatch = tnsPattern.matcher(tnsFile);
		 * 
		 * // Get the last matching entry int lastMatch = -1;
		 * while(tnsMatch.find()) { lastMatch = tnsMatch.start();
		 * //System.out.println(tnsMatch.group()); }
		 * 
		 * if(lastMatch < 0) throw new SQLException(
		 * "Unable to load TNS entry for " + DB_Address);
		 * 
		 * tnsMatch.find(lastMatch); String tnsEntry = tnsMatch.group(1);
		 * 
		 * // Remove whitespace tnsEntry = tnsEntry.replaceAll("\\s", "");
		 * 
		 * //System.out.println(tnsEntry);
		 * 
		 * //ods.setURL("jdbc:oracle:thin:@" + DB_Address);
		 * 
		 */

		// Setting UserName
		ods.setUser(DB_UserName);

		// Setting Password
		ods.setPassword(DB_Password);

		// Setting Driver Type
		ods.setDriverType("thin");

		// Getting connection
		connection = ods.getConnection();

		if (connection != null) {
			System.out.println("Connection Successful.......");
		} else {
			System.out.println("Failed to make connection!");
			// close connection
			Close();

		}

		return connection;
	}

	// gets connection
	public Connection getConnection() {
		return connection;
	}

	// Close connection
	public void Close() throws SQLException {

		connection.close();
		if (connection.isClosed() == true) {
			System.out.println("Connection Closed Successfuly....... ");
		}

		else
			System.out.println("Connection Did not Close Suecessful......");

	}

}
