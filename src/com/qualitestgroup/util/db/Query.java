package com.qualitestgroup.util.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.qualitestgroup.kdt.TestContext;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.util.logging.Logger.LogLevel;

/**
 * @author QualiTest
 *
 */

public class Query {

	/**
	 * 
	 * @param qconn
	 * @param query
	 * @return
	 * @throws SQLException
	 */

	/*
	 * Allows the user to freely enter any type of query
	 */

	public static ResultSet OpenQuery(Connection qconn, String query) throws SQLException {

		// creating result set
		ResultSet resultSet;

		// creating stmt as null
		Statement stmt = null;

		System.out.println("Your query is about to be executed......");

		// setting sql to = query
		String sql = (query).trim();

		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}

		TestContext.getContext().log(LogLevel.trace, "Executing query: " + sql);

		// if this is a commit statement, then execute qconn.commit() [releases
		// resources]
		// doesn't work
		if (!qconn.getAutoCommit() && sql.equals("commit")) {
			qconn.commit();
			TestContext.getContext().log(LogLevel.trace, "Committing previous queries...");
			return null;
		}

		qconn.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);

		// preparing connection to execute query
		stmt = qconn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		// stmt.closeOnCompletion();

		// executing query
		resultSet = stmt.executeQuery(sql);

		System.out.println("Your query has been sucessfully executed......");

		return resultSet;

	}

	/**
	 * Safely close a ResultSet rs
	 * 
	 * @author QualiTest
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				Statement s = rs.getStatement();
				if (s != null) {
					s.close();
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves the contents of a table to a file path
	 * 
	 * @param con
	 *            Connection object
	 * @param table
	 *            Name of table to dump
	 * @param filepath
	 *            Path to save table to
	 * @throws SQLException
	 * @throws IOException
	 * @author QualiTest
	 */
	public static void dumpTableContents(Connection con, String table, String filepath)
			throws SQLException, java.io.IOException {
		ResultSet rs = OpenQuery(con, "SELECT * FROM " + table);
		if (!FileTools.saveResultSet(rs, filepath)) {
			throw new java.io.IOException("Unable to save " + table + " to " + filepath);
		}
		closeResultSet(rs);
	}

}