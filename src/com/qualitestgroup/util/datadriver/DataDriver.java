package com.qualitestgroup.util.datadriver;

import com.qualitestgroup.util.datadriver.DataRow.Headers;

//import java.util.Iterator;

/**
 * Interface for DataDriver method
 * 
 * @author Neo
 */
public interface DataDriver
{
	/**
	 * Check if there is another row available.
	 * @return
	 */
	public boolean hasNext();
	
	/**
	 * Determines whether or not the underlying excel workbook
	 * has another worksheet after this one. If the underlying
	 * data source is not an excel workbook, this will always
	 * return false.
	 * 
	 * @return
	 */
	public boolean hasNextSheet();
	
	/**
	 * Returns the name of the current sheet over which this
	 * DataDriver is iterating. If the underlying data source is
	 * not an excel workbook, this will always return null.
	 * 
	 * @return
	 */
	public String getSheetName();
	
	/**
	 * Gets the next row. Behavior is undefined if hasNext would return false.
	 * 
	 * @return The row as a string array.
	 */
	public String[] next();
	
	/**
	 * Gets the next row. Behavior is undefined if hasNext would return false.
	 * 
	 * @return The row as a DataRow object.
	 */
	public DataRow nextRow();	
	
	/**
	 * Advances the underlying excel workbook to the next sheet.
	 * If the underlying data source is not an excel workbook,
	 * this does nothing
	 * 
	 */
	public boolean nextSheet();
	
	/**
	 * Validates whether the DataDriver contains certain columns.
	 * 
	 * @param columns List of column names to check.
	 * @return True if all of the given columns are present.
	 */
	public boolean hasColumns(String... columns);
	
	/**
	 * Returns a list of all columns missing from the DataDriver.
	 * @param columns List of column names to check.
	 * @return Array of columns that aren't present.
	 */
	public String[] getMissingColumns(String... columns);
	
	public Headers getHeaders();
}
