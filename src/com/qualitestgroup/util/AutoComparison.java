package com.qualitestgroup.util;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataDriverFactory;

/**
 * Comparison methods for result sets
 * 
 * @author Neo
 */
public class AutoComparison 
{
	static Connection Qcon;
	static String file;
	static DataDriver data;
	static ResultSet rs, rs2;
	static String path, path2;
	static String userDir = System.getProperty("user.dir");
	private static PrintWriter writer = null;
	static ArrayList<String> diff = new ArrayList<String>();
	
	private static String logFile = "";
	
	/**
	 * Testing purposes
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void main (String args[]) throws FileNotFoundException, UnsupportedEncodingException
	{
		HashMap<String, String> map1, map2;
		map1 = new HashMap<String, String>();
		map2 = new HashMap<String, String>();
		
		map1.put("Test1", "asd");
		map1.put("Test2", "dsa");
		map1.put("Test3", "qwe");
		map1.put("Null", null);
		
		map2.put("Test1", "asd");
		map2.put("Test2", null);
		map2.put("Test3", "poi");
		map2.put("2adsasd", "12345");
		
		for(Diff<String, String> d : MapComparison(map1, map2))
		{
			System.out.println(d);
		}
	}
	
	/**
	 * Comparison file that appends the logs
	 * 
	 * @param appName - name of app
	 * @param loggingDir - location of directory
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void ComparisonAppender(String appName, String loggingDir) throws FileNotFoundException, UnsupportedEncodingException
	{	
		writer = new PrintWriter(userDir + "\\logs\\" + logFile, "UTF-8");
		
		for(String str : diff)
		{
			writer.println(str);
		}
		writer.close();
	}
	
	/**
	 * Comparison from Result Set to Data Set
	 * 
	 * @param file - file path
	 * @param rs - result set
	 * @throws SQLException
	 */
	public static void RSDataComparison(String file, ResultSet rs) throws SQLException
	{
		int columnCount;
		int row1Count = 0, row2Count = 0;
		boolean found = false;
		
		diff = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		
        data = DataDriverFactory.create(file);
		
		//Get the max column count
		ResultSetMetaData rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		
		//Get headers
		for(int i = 0; i < columnCount; i++) 
		{
		    columns.add(rsmd.getColumnName(i));
		}
		
		//Iterates over result set row
		while(rs.next())
		{	
			//Initializes Result Set array length
			String[] rsArray = new String[columnCount];
			
			//Stores columns into sizeable array
			for(int j = 0; j < columnCount; j++)
			{
				rsArray[j] = rs.getNString(j);
			}
			
			row1Count++;
			row2Count = 0;
			
			//Iterates current result set row to data set
			//and checks if it is found
			while(data.hasNext() && !found)
			{
				//Iterates over data set row
				String[] dArray = data.next();
				
				row2Count++;
				
				//Compare with identifier, change column if need
				if(dArray[0] == rsArray[0])
				{
					//If it is found, move to next comparison
					found = true;
					
					//Compare data sets value by value
					for(int i = 0; i < columnCount - 1; i++)
					{	
						if(dArray[i] != rsArray[i])
						{
							diff.add("Type mismatch at RS - " + row1Count + " and Data - " + row2Count +
									" at column" + columnCount);
							diff.add("RS : " + rsArray[i] + " does not equal Data : " + dArray[i]);
						}
					}
				}
			}
		}
		
		//If result set ends early, log error
		if(data.hasNext())
		{
			diff.add("Result set has exhausted before data set!");
		}
	}
	
	/**
	 * Comparison from Data Set to Result Set
	 * 
	 * @param file - file path
	 * @param rs - result set
	 * @throws SQLException
	 */
	public static void DataRSComparison(String file, ResultSet rs) throws SQLException
	{
		int columnCount;
		int row1Count = 0, row2Count = 0;
		boolean found = false;
		
		diff = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		
        data = DataDriverFactory.create(file);
		
		//Get the max column count
		ResultSetMetaData rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();
		
		//Get headers
		for(int i = 0; i < columnCount; i++) 
		{
		    columns.add(rsmd.getColumnName(i));
		}
		
		//Iterates over result set row
		while(data.hasNext())
		{	
			//Initializes Result Set array length
			String[] rsArray = new String[columnCount];
			
			//Stores columns into sizeable array
			for(int j = 0; j < columnCount; j++)
			{
				rsArray[j] = rs.getNString(j);
			}
			
			row1Count++;
			row2Count = 0;
			
			//Iterates current result set row to data set
			//and checks if it is found
			while(rs.next() && !found)
			{
				//Iterates over data set row
				String[] dArray = data.next();
				
				row2Count++;
				
				//Compare with identifier, change column if need
				if(dArray[0] == rsArray[0])
				{
					//If it is found, move to next comparison
					found = true;
					
					//Compare data sets value by value
					for(int i = 0; i < columnCount - 1; i++)
					{	
						if(dArray[i] != rsArray[i])
						{
							diff.add("Type mismatch at Data - " + row1Count + " and RS - " + row2Count +
									" at column" + columnCount);
							diff.add("Data : " + dArray[i] + " does not equal RS : " + rsArray[i]);
						}
					}
				}
			}
		}
		
		//If result set ends early, log error
		if(rs.next())
		{
			diff.add("Data set has exhausted before result set!");
		}
	}
	
	/**
	 * Comparison between two result sets
	 * 
	 * @param rs - 1st result set
	 * @param rs2 - 2nd result set
	 * @throws SQLException
	 */
	public static void RSComparison(ResultSet rs, ResultSet rs2) throws SQLException
	{
		int columnCount, columnCount2;
		int row1Count = 0, row2Count = 0;
		
		diff = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		
		//Get the max column count for rs1
		ResultSetMetaData rsmd = rs.getMetaData();
		columnCount = rsmd.getColumnCount();

		//Get the max column count for rs2
		ResultSetMetaData rsmd2 = rs2.getMetaData();
		columnCount2 = rsmd2.getColumnCount();
		
		//Get headers
		for(int i = 0; i < columnCount; i++) 
		{
		    columns.add(rsmd.getColumnName(i));
		}
		
		//Iterates over result set row
		while(rs.next())
		{	
			String[] rsArray = new String[columnCount];
			boolean found = false;
			
			//Stores columns into sizeable array
			for(int j = 0; j < columnCount; j++)
			{
				rsArray[j] = rs.getNString(j);
			}
			
			row1Count++;
			row2Count = 0;
			
			while(rs2.next() && !found)
			{
				row2Count++;
				
				//Initializes Result Set array length
				String[] rsArray2 = new String[columnCount2];
				
				//Stores columns into sizeable array
				for(int k = 0; k < columnCount2; k++)
				{
					rsArray2[k] = rs2.getNString(k);
				}
				
				//Compare with identifier, change column if need
				if(rsArray[0] == rsArray2[0])
				{
					found = true;
					
					//Compare data sets value by value
					for(int i = 0; i < columnCount - 1; i++)
					{
						if(rsArray[i] != rsArray2[i])
						{
							diff.add("Type mismatch at RS1 - " + row1Count + " and RS2 - " + row2Count +
									" at column" + columnCount);
							diff.add("RS : " + rsArray[i] + " does not equal RS2 : " + rsArray2[i]);
						}
					}
				}
			}
		}
		
		//If result set 2 ends early, log error
		if(rs2.next())
		{
			diff.add("1st Result Set has exhausted before 2nd Result Set!");
			rs.close();
			rs2.close();
		}
		
		//If result set ends early, log error
		if(rs.next())
		{
			diff.add("1st Result Set has exhausted before 2nd Result Set!");
			rs.close();
			rs2.close();
		}
	}


	public static <K,V> List<Diff<K,V>> MapComparison(Map<K,V> map1, Map<K,V> map2)
	{
		List<Diff<K,V>> result = new LinkedList<Diff<K,V>>();
		
		// null checks
		if(map1 == null)
		{
			map1 = new HashMap<K,V>();
		}
		if(map2 == null)
		{
			map2 = new HashMap<K,V>();
		}
		
		HashSet<K> allKeys = new HashSet<K>();
		allKeys.addAll(map1.keySet());
		allKeys.addAll(map2.keySet());
		
		for(K key : allKeys)
		{
			V exp = map1.get(key);
			V act = map2.get(key);
			Diff<K,V> d;
			
			if(!map1.containsKey(key))
			{
				d = new Diff<K,V>(key, null, act);				
			}
			else if(!map2.containsKey(key))
			{
				d = new Diff<K,V>(key, exp, null);
			}
			else // both maps contain the key
			{				
				// null checks
				if(exp == null && act == null)
				{
					// both null, so no difference
					continue;
				}
				else if(exp == null)
				{
					d = new Diff<K,V>(key, null, act);
				}
				else if(act == null)
				{
					d = new Diff<K,V>(key, exp, null);
				}
				else
				{
					// compare the entries
					if(exp.equals(act))
					{
						continue;
					}
					else
					{
						d = new Diff<K,V>(key, exp, act);
					}
				}
			}
			result.add(d);
		}
		
		
		return result;
	}

	
	public static class Diff<K, V>
	{
		private K key;
		private V exp, act;
		
		public Diff(K key, V expected, V actual)
		{
			this.key = key;
			this.exp = expected;
			this.act = actual;
		}
		
		public K getKey()
		{
			return key;
		}
		
		public V getExpected()
		{
			return exp;
		}
		
		public V getActual()
		{
			return act;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(o instanceof Diff)
			{
				Diff<?, ?> d = (Diff<?, ?>) o;
				boolean k, e, a;
				if(key == null)
				{
					k = d.key == null;
				}
				else
				{
					k = key.equals(d.key);
				}
				
				if(exp == null)
				{
					e = d.exp == null;
				}
				else
				{
					e = exp.equals(d.exp);
				}
				
				if(act == null)
				{
					a = d.act == null;
				}
				else
				{
					a = act.equals(d.act);
				}
				
				return k && e && a;
			}
			else
			{
				return false;
			}
		}
		
	}

}