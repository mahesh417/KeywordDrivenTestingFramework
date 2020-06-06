package com.qualitestgroup.util.datadriver;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;

import com.qualitestgroup.util.datadriver.DataRow.Headers;
import com.qualitestgroup.util.fileio.CloseableWorkbook;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.util.fileio.FileTools.FileExt;

import au.com.bytecode.opencsv.*;

/**
 * Methods for reading and processing data files of different types
 * 
 * @author Neo
 */
public abstract class DataDriverFactory
{

    /**
     * Constructor
     * 
     * @param filePath - location of data file
     * @param sheetPath - location of sheet in data
     * @param filter Row value filter. Must be a string of the form "ColumnName=value". Value can have the 
     * wildcards "?" and "*" to match single or multiple characters, respectively.
     * @return object of xls
     * @return object of csv
     * @throws exception/null
     */
    public static DataDriver create(String filePath, String sheetPath, String filter)
    {
    	FileExt dataType = FileTools.getFileExt(filePath);
    	
        if (dataType == FileExt.XLS || dataType == FileExt.XLSX)
            return new ExcelDataDriver(filePath, sheetPath, filter);
        else if (dataType == FileExt.CSV)
            return new CSVDataDriver(filePath, filter);
        else
            return null;
    }
    
    public static DataDriver create(String filePath, boolean keepOpen, String filter) {    	
    	FileExt dataType = FileTools.getFileExt(filePath);
    	
        if (dataType == FileExt.XLS || dataType == FileExt.XLSX)
            return new ExcelDataDriver(filePath, keepOpen, filter);
        else
            return null;
    }
    
    /**
     * Constructor
     * 
     * @param filePath - location of data file
     * @param sheetPath - location of sheet in data
     * @param dataType - type of data file
     * @return object of xls
     * @return object of csv
     * @throws exception/null
     */
    public static DataDriver create(String filePath, String sheetPath)
    {
    	FileExt dataType = FileTools.getFileExt(filePath);
    	
        if (dataType == FileExt.XLS || dataType == FileExt.XLSX)
            return new ExcelDataDriver(filePath, sheetPath);
        else if (dataType == FileExt.CSV)
            return new CSVDataDriver(filePath);
        else
            return null;
    }
    
    /**
     * Constructor
     * 
     * @param filePath - location of data file
     * @param dataType - type of data file
     * @return object of xls
     * @return object of csv
     * @throws exception/null
     */
    public static DataDriver create(String filePath)
    {
    	FileExt dataType = FileTools.getFileExt(filePath);
        if (dataType == FileExt.XLS || dataType == FileExt.XLSX)
            return new ExcelDataDriver(filePath);
        else if (dataType == FileExt.CSV)
            return new CSVDataDriver(filePath);
        else
            return null;
    }
    
    public static DataDriver create(ResultSet rs)
    {
    	return new ResultSetDataDriver(rs);
    }
    
    private static void trimArray(String[] arr)
    {
    	for(int i = 0; i < arr.length; i++)
    	{
    		arr[i] = (arr[i] == null) ? null : arr[i].trim();    		
    	}
    }
    
    /**
     * DataDriver for Excel files that accepts an object passed and gives command methods to manipulate such data.
     * 
     * @author Neo
     * 
     * @param Sheet wSheet - sheet located in excel with the current relevant data
     * @param Workbook wBook - excels workbook that contains sheets
     * @param Iterator<Row> iterator - iterative parameter that loops through each row and cell
     */
    private static class ExcelDataDriver implements DataDriver
    {
        private Sheet wSheet;
        private CloseableWorkbook wBook;
        private Iterator<Row> iterator;
        private DataRow.Headers headers;
        private boolean hasPattern = false;
        private boolean keepWBookOpen = false;
        private String filterColumn;
        private String filterPattern;
        private int sheetIndex;
        private int rowIndex = 0;
        
        public ExcelDataDriver (String filePath, String sheetName, String filter)
        {
        	this(filePath, sheetName);
        	hasPattern = true;
        	String[] filterSplit = filter.split("=", 2);
        	filterColumn = filterSplit[0];
        	filterPattern =  filterSplit[1].replaceAll("([\\\\.\\[{(*+?^$|\\)])", "\\\\$1").replaceAll("\\\\\\*", ".*").replaceAll("\\\\\\?", ".");
        }
        
        public ExcelDataDriver (String filePath, boolean keepOpen, String filter) {
        	this(filePath);
        	keepWBookOpen = keepOpen;
        	hasPattern = !(filter == null || filter.isEmpty());
        	
        	if (hasPattern) {
        		String[] filterSplit = filter.split("=", 2);
            	filterColumn = filterSplit[0];
            	filterPattern =  filterSplit[1].replaceAll("([\\\\.\\[{(*+?^$|\\)])", "\\\\$1").replaceAll("\\\\\\*", ".*").replaceAll("\\\\\\?", ".");
        	}
        }
        
        /**
         * Creates a new ExcelDataDriver given a filePath
         * and sheetName
         * 
         * @param filePath - location of data file
         * @param sheetName - sheet name selection
         */
        public ExcelDataDriver (String filePath, String sheetName)
        {
            try 
            {
                wBook = FileTools.openExcel(filePath);
                wSheet = wBook.getSheet(sheetName);
                sheetIndex = wBook.getSheetIndex(wSheet);
                iterator = wSheet.iterator();
                headers = new DataRow.Headers(this);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        
        /**
         * Creates a new ExcelDataDriver given a filePath,
         * defaults sheetName to 1st sheet
         * 
         * @param filePath - location of data file
         */
        public ExcelDataDriver (String filePath)
        {
        	try 
            {
                wBook = FileTools.openExcel(filePath);
                wSheet = wBook.getSheetAt(0);
                sheetIndex = 0;
                iterator = wSheet.iterator();
                headers = new DataRow.Headers(this);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        
        /**
         * @return - true if there is more data in the file,
         * false otherwise
         */
        @Override
		public boolean hasNext() 
        {
            if (iterator.hasNext())
                return iterator.hasNext(); //return true?
            else
            {
            	if(wBook != null && !keepWBookOpen)
            	{
	                try {
						wBook.close(); //Closes workbook
					} catch (IOException e) {}
	            	wBook = null;
            	}
                return false;
            }
        }
        
        /**
         * Determines whether or not there is another
         * sheet present after the current one
         * 
         * @return
         * 
         * @author Brian Van Stone
         */
        @Override
		public boolean hasNextSheet() {
        	if(sheetIndex >= wBook.getNumberOfSheets()-1) {
        		if(wBook != null)
            	{
	                try {
						wBook.close(); //Closes workbook
					} catch (IOException e) {}
	            	wBook = null;
            	}
                return false;
        	}
        	return true;
        }

        /**
         * @return - an array of strings representing the data of each cell of the current row
         */
        @Override
		public String[] next() 
        {
            Row r = iterator.next();
            rowIndex = r.getRowNum();
            ArrayList<String> temp = new ArrayList<String>();
            
            // Iterate through physical cells, not logical cells.
            for (int c = 0; c < r.getLastCellNum(); c++)
            {
                temp.add(r.getCell(c, Row.CREATE_NULL_AS_BLANK).toString());
            }
            
            String[] arr = temp.toArray(new String[0]);            
            //trimArray(arr);            
            return arr;
        }
        
        @Override
		public DataRow nextRow()
        {
        	if(hasPattern)
        	{
        		DataRow row = null;
        		while(this.hasNext())
        		{
        			row = new DataRow(this, headers);
        			row.setRowNum(rowIndex);
        			if(row.getItem(filterColumn).matches(filterPattern))
        			{
        				return row;
        			}        			
        		}
        		return null;
        	}
        	else
        	{
	        	if(this.hasNext())
	        	{
	        		DataRow row = new DataRow(this, headers);
	        		row.setRowNum(rowIndex);
	        		return row;
	        	}
	        	else
	        		return null;
        	}
        }
        
        /**
         * Advances this ExcelDataDriver to the next sheet in the workbook
         * 
         * @author Brian Van Stone
         */
        @Override
		public boolean nextSheet() {
        	if(!hasNextSheet())
        		return false;
    		wSheet = wBook.getSheetAt(++sheetIndex);
            iterator = wSheet.iterator();
            headers = new DataRow.Headers(this);
            return true;
        }
        
        @Override
		public boolean hasColumns(String... columns)
        {
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			return false;
        		}
        	}
        	return true;
        }
        
        @Override
		public String[] getMissingColumns(String... columns)
        {
        	LinkedList<String> ret = new LinkedList<String>();
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			ret.add(col);
        		}
        	}
        	return ret.toArray(new String[0]);
        }

        /**
         * Called by the garbage collector on an object when garbage 
         * collection determines that there are no more references to the object.
         */
        @Override
		protected void finalize() throws Throwable
        {
            try
            {
                wBook.close(); //Closes workbook
            }
            finally
            {
                super.finalize(); //Garbage collection
            }
        }
        
    	@Override
		public Headers getHeaders()
    	{
    		return headers;
    	}
    	
    	@Override
		public String getSheetName() {
    		return wSheet.getSheetName();
    	}
    }
    
    /**
     * DataDriver for CSV files that accepts an object passed and gives command methods to manipulate such data.
     * 
     * @author Neo
     * 
     * @param CSVReader reader - parameter that contains object passed
     * @param nextLine - empty array for CSV rows
     */
    private static class CSVDataDriver implements DataDriver
    {
        private CSVReader reader;
        public String[] nextLine;
        private DataRow.Headers headers;
        private boolean hasPattern = false;
        private String filterColumn;
        private String filterPattern;
        
        public CSVDataDriver (String filePath, String filter)
        {
        	this(filePath);
        	hasPattern = true;
        	String[] filterSplit = filter.split("=", 2);
        	filterColumn = filterSplit[0];
        	filterPattern =  Pattern.quote(filterSplit[1]).replaceAll("\\\\\\*", ".*").replaceAll("\\\\\\?", ".");
        }
        
        /**
         * Creates a new CSVDataDriver given a filePath
         * 
         * @param filePath - location of data file
         */
        public CSVDataDriver (String filePath)
        {
            try 
            {
                reader = FileTools.openCSV(filePath);
                nextLine = reader.readNext();
                headers = new DataRow.Headers(this);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        
        /**
         * @return - true if there is more data in the file,
         * false otherwise
         */
        @Override
		public boolean hasNext() 
        {
            return nextLine != null;
        }

        /**
         * @return - an array of strings representing the data of each delimited portion of the current row
         */
        @Override
		public String[] next() 
        {
            String[] temp = nextLine;
            
            try
            {
                nextLine = reader.readNext();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
                      
            trimArray(temp); 
            return temp;
        }
        
        @Override
		public DataRow nextRow()
        {
        	if(hasPattern)
        	{
        		DataRow row = null;
        		while(this.hasNext())
        		{
        			row = new DataRow(this, headers);
        			if(row.getItem(filterColumn).matches(filterPattern))
        			{
        				return row;
        			}        			
        		}
        		return null;
        	}
        	else
        	{
	        	if(this.hasNext())
	        	{
	        		DataRow row = new DataRow(this, headers);
	        		return row;
	        	}
	        	else
	        		return null;
        	}
        }
        
        @Override
		public boolean hasColumns(String... columns)
        {
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			return false;
        		}
        	}
        	return true;
        }
        
        @Override
		public String[] getMissingColumns(String... columns)
        {
        	LinkedList<String> ret = new LinkedList<String>();
        	
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			ret.add(col);
        		}
        	}
        	return ret.toArray(new String[0]);
        }

        /**
         * Called by the garbage collector on an object when garbage 
         * collection determines that there are no more references to the object.
         */
        @Override
		protected void finalize() throws Throwable
        {
            try
            {
                if (reader != null)
                    reader.close(); //Closes workbook
            }
            finally
            {
                super.finalize(); //Garbage collection
            }
        }
        
        @Override
		public Headers getHeaders()
    	{
    		return headers;
    	}

		@Override
		public boolean nextSheet() {
			// N/A for this context. do nothing
			return false;
		}

		@Override
		public boolean hasNextSheet() {
			// N/A for this context. return false.
			return false;
		}

		@Override
		public String getSheetName() {
			// N/A for this context. return null.
			return null;
		}
    }

    private static class ResultSetDataDriver implements DataDriver
    {

    	private ResultSet set;
    	private boolean hasNext;
    	private ResultSetMetaData meta;
    	private String[] columns;
    	public final DataRow.Headers headers;
    	
    	public ResultSetDataDriver(ResultSet rs)
    	{
    		set = rs;
    		try {
				hasNext = set.next();
				meta = set.getMetaData();
				columns = new String[meta.getColumnCount()];
				for(int i = 0; i < meta.getColumnCount(); i++)
				{
					columns[i] = meta.getColumnName(i + 1);
				}				
			} catch (SQLException e) {
				set = null;
				hasNext = false;
				columns = new String[0];
			}
    		headers = new DataRow.Headers(columns);
    	}
		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public String[] next() {
			String[] temp = new String[columns.length];
			try {
			
				for(int i = 0; i < columns.length; i++)
				{
					temp[i] = set.getString(i + 1);				
				}
				hasNext = set.next();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			trimArray(temp); 
			return temp;
		}

		@Override
		public DataRow nextRow() {
			DataRow next = new DataRow(this, this.headers);
			return next;
			
		}

		@Override
		 public boolean hasColumns(String... columns)
        {
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			return false;
        		}
        	}
        	return true;
        }
        
        @Override
		public String[] getMissingColumns(String... columns)
        {
        	LinkedList<String> ret = new LinkedList<String>();
        	
        	for(String col : columns)
        	{
        		if(!headers.containsKey(col))
        		{
        			ret.add(col);
        		}
        	}
        	return ret.toArray(new String[0]);
        }
    	
        @Override
		public Headers getHeaders()
    	{
    		return headers;
    	}

		@Override
		public boolean hasNextSheet() {
			// N/A for this context. return false.
			return false;
		}
		
		@Override
		public boolean nextSheet() {
			// N/A for this context. do nothing.
			return false;
		}

		@Override
		public String getSheetName() {
			// N/A for this context. return null.
			return null;
		}
        
       
    }
}
