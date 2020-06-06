package com.qualitestgroup.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import oracle.jdbc.pool.OracleDataSource;

public class ConnectionUtils {
	private static final String MS_SQL_PREFIX = "jdbc:sqlserver://";
	
	private ConnectionUtils() {
	}
	
	public static Connection createConnection(String address, String user, String password, String type) throws SQLException {
		return createConnection(address, user, password, type, false);
	}
	
	public static Connection createConnection(String address, String type) throws SQLException {
		return createConnection(address, "", "", type, true);
	}
	
	private static Connection createConnection(String address, String user, String password, String type, boolean integratedSecurity) throws SQLException {
		Connection conn = null;
		ConnectionType ctype = ConnectionType.fromString(type);
		
		// assume all non MSSQL types are Oracle for now
		if (ctype == ConnectionType.ORACLE) {
			// setting tnsnames.ora location
			System.setProperty("oracle.net.tns_admin", System.getProperty("user.dir"));
			
			OracleDataSource ods = new OracleDataSource();
			
			ods.setTNSEntryName(address);
			ods.setUser(user);
			ods.setPassword(password);
			ods.setDriverType("thin");
			
			conn = ods.getConnection();
		}
		else {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				System.out.println("Driver launched");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (!StringUtils.startsWithIgnoreCase(address, MS_SQL_PREFIX)) {
				address = MS_SQL_PREFIX + address;
			}
			
			if (integratedSecurity) {
				String temp = "integratedSecurity=true;";
				address += address.endsWith(";") ? temp : ";" + temp;
				conn = DriverManager.getConnection(address);
			} else {
				conn = DriverManager.getConnection(address, user, password);				
			}
		}
		
		return conn;
	}
	
	public static void close(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}
	
	public static void closeQuietly(Connection conn) {
		try {
			close(conn);
		} catch (SQLException e) {
			// suppress
		}
	}
	
	public static void commitAndClose(Connection conn) throws SQLException {
		if (conn != null) {
			try {
				conn.commit();
			} finally {
				conn.close();
			}
		}
	}
	
	public static void commitAndCloseQuietly(Connection conn) {
		try {
			commitAndClose(conn);
		} catch (SQLException e) {
			// suppress
		}
	}
	
	public static void rollback(Connection conn) throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}
	
	public static void rollbackAndClose(Connection conn) throws SQLException {
		try {
			rollback(conn);
		} finally {
			close(conn);
		}
	}
	
	public static void rollbackAndCloseQuietly(Connection conn) {
		try {
			rollbackAndClose(conn);
		} catch (SQLException e) {
			// suppress
		}
	}
}