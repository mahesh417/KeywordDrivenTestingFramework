package com.qualitestgroup.util.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.w3c.dom.*;

import com.qualitestgroup.kdt.KDTConstants;
import com.qualitestgroup.kdt.KDTDriver.TestRunData;
import com.qualitestgroup.util.DateUtil;
import com.qualitestgroup.util.datadriver.DataDriver;
import com.qualitestgroup.util.datadriver.DataDriverFactory;
import com.qualitestgroup.util.datadriver.DataRow;
import com.google.common.io.Files;

import au.com.bytecode.opencsv.*;
/**
 * Methods for opening and saving different types of files.
 * 
 * @author Matthew Swircenski
 *
 */
public final class FileTools {
	
	// Private constructor to block instantiation
	private FileTools(){}
	
	/**
	 * File extension enumerator. Denotes valid file extensions, for use in comparisons and such.
	 *  
	 * @author Matthew Swircenski
	 */
	public enum FileExt{
		XLS,	// Excel '97
		XLSX,	// Excel '07
		CSV,	// Comma-separated value
		XML,	// XML
		TXT,	// Text
		LOG,	// Log
//		DB,		// Database (curr. unsupported)
		SQL,	// SQL query files
		HTML,	// HTML
		ERR;	// Unsupported/invalid extension
	}
    
    // Excel

    /**
     * Opens an Excel workbook.
     * 
     * @param filePath Path of the file to open.
     * @return Workbook object representing the Excel file
     * @throws IOException
     */
    public static CloseableWorkbook openExcel(String filePath) throws IOException
    {
    	FileInputStream f = new FileInputStream(filePath);
    	Workbook wb = null;
    	NPOIFSFileSystem npoifs = null;
    	OPCPackage pkg = null;

    	try {	    		    	
	        if(getFileExt(filePath) == FileExt.XLS)
	        {
	        	npoifs = new NPOIFSFileSystem(f);
	            wb = WorkbookFactory.create(npoifs);
	            return new CloseableWorkbook(wb, npoifs);
	        } 
	        else if (getFileExt(filePath) == FileExt.XLSX)
	    	{
	        	pkg = OPCPackage.open(f);
	            wb = WorkbookFactory.create(pkg);
	            return new CloseableWorkbook(wb, pkg);
	    	} else throw new IOException(filePath + " (Invalid file extension)");
    	} 
    	catch(IOException io) 
    	{
    		if (npoifs != null) { npoifs.close(); }
	        if (pkg != null) { pkg.close(); }
    		throw io;
    	}
    	catch(Exception e) 
    	{
    		if (npoifs != null) { npoifs.close(); }
	        if (pkg != null) { pkg.close(); }
    		throw new IOException(e.getMessage());
    	}    	
    }
    
    /**
     * Saves an Excel workbook.
     * 
     * @param wb The workbook file to save.
     * @param filePath The path to save the file to.
     * @throws IOException
     */
    public static void saveExcel(Workbook wb, String filePath) throws IOException
    {
    	if(wb instanceof CloseableWorkbook)
    	{
    		//((CloseableWorkbook) wb).close();
    		wb = ((CloseableWorkbook) wb).getWorkbook();
    	}
        if(getFileExt(filePath) == FileExt.XLS && wb instanceof HSSFWorkbook || 
           getFileExt(filePath) == FileExt.XLSX && wb instanceof XSSFWorkbook)
        {
        	try
        	{
	            FileOutputStream fileOut = new FileOutputStream(filePath);
	            wb.write(fileOut);
	            fileOut.close();
        	} catch (java.io.FileNotFoundException e)
        	{
        		// File probably open, suppress exception.
        	}
        } else throw new IOException(filePath + " (Invalid file extension)");
    }
    
    /**
     * Creates a new workbook of the appropriate type (xls or xlsx) based on the given file path.
     * 
     * @param fileExt Extension of the workbook file to create.
     * @return Workbook, or null if extension inappropriate.
     */
    public static Workbook createWorkbook(FileExt fileExt)
    {
    	if(fileExt == FileExt.XLS)
    	{
    		return new HSSFWorkbook();
    	}
    	else if (fileExt == FileExt.XLSX)
    	{
    		return new XSSFWorkbook();
    	}
    	else return null;
    }
    
    // XML
    /**
     * Opens an XML file
     * 
     * @param filePath Path of the file to open.
     * @return Document object representing the XML file.
     * @throws IOException
     */
    public static Document openXML(String filePath) throws IOException
    {
        if(getFileExt(filePath) == FileExt.XML)
        {
            try {
                File fXmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                return doc;
            } catch (IOException e) {
                throw e;
            } catch(Exception e) {
                // Simplify exception throwing
                throw new IOException(e.getMessage());
            }        
        } else throw new IOException(filePath + " (Invalid file extension)");
    }
    
    // CSV
    
    /**
     * Opens a CSV file reader.
     * @param filePath Path of the file to open.
     * @return CSVReader object representing the CSV file.
     * @throws IOException
     */
    public static CSVReader openCSV(String filePath) throws IOException
    {
        if(getFileExt(filePath) == FileExt.CSV)
        {
            return new CSVReader(new FileReader(filePath));
        } else throw new IOException(filePath + " (Invalid file extension)");
    }
    
    // txt
    /**
     * Opens a text (txt, log, sql, html) file for reading.
     * @param filePath Path of the file to open.
     * @return FileReader object representing the text file.
     * @throws IOException
     */
    public static BufferedReader openTxt(String filePath) throws IOException
    {
    	/*switch(getFileExt(filePath))
    	{
	    	case TXT:
	    	case LOG:
	    	case SQL:    	
	    	case HTML:
	    		return new BufferedReader(new FileReader(filePath));
	    		//return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-16"));
	    	default:
	    		throw new IOException(filePath + " (Invalid file extension)");
    	}*/
    	return new BufferedReader(new FileReader(filePath));
    }
    
    /**
     * Writes a line to a txt file.
     * @param data The string to write to the file.
     * @param filePath The path of the file to write.
     * @throws IOException
     */
    public static void writeTxtLine(String data, String filePath) throws IOException
    {
    	FileExt ext = getFileExt(filePath);
    	if(ext == FileExt.TXT ||
    	   ext == FileExt.LOG || 
    	   ext == FileExt.HTML
    	   )
    	{
    		PrintWriter pw = new PrintWriter(filePath);
    		pw.println(data);
    		pw.close();
    	} else throw new IOException(filePath + " (Invalid file extension)");
    }
    
    public static void writeFile(String filePath, String... data) throws IOException
    {
    	PrintWriter pw = new PrintWriter(filePath);
    	for(String s : data)
    	{    		
    		pw.println(s);
    	}
    	pw.close();
    }
    
    public static String readerToString(BufferedReader reader, boolean closeReader)
    {
    	StringBuffer sb = new StringBuffer();
    	String line;
		try {
			line = reader.readLine();		
	    	while(line != null)
	    	{
	    		sb.append(line);
	    		sb.append("\n");
	    		line = reader.readLine();
	    	}
		} catch (IOException e) {
			line = null;
		} finally {
			if (closeReader) {
				IOUtils.closeQuietly(reader);
			}
		}
    	return sb.toString();
    }
    
    public static String readerToString(BufferedReader reader) {
    	return readerToString(reader, false);
    }
    
    public static String[] readerToStringArray(BufferedReader reader, boolean closeReader)
    {
    	LinkedList<String> lls = new LinkedList<String>();
    	String line;
		try {
			line = reader.readLine();		
	    	while(line != null)
	    	{
	    		lls.add(line);
	    		line = reader.readLine();
	    	}
		} catch (IOException e) {
			line = null;
		} finally {
			if (closeReader) {
				IOUtils.closeQuietly(reader);
			}
		}
    	return lls.toArray(new String[0]);
    }
    
    public static String[] readerToStringArray(BufferedReader reader) {
    	return readerToStringArray(reader, false);
    }
    
    // Useful functions
    
    /**
     * Gets the file extension from a string representing a file path. 
     * 
     * @param filePath
     * @return The file extension.
     */
    public static FileExt getFileExt(String filePath)
    {
    	
        // Split path based on either '/' (*nix) or '\' (Windows)
        String[] dirSplit = filePath.split("/");
        if(dirSplit.length <= 1)
        {
            dirSplit = filePath.split("\\\\"); // One doubly-escaped (string + regex) backslash
        }
        String fileName = dirSplit[dirSplit.length - 1];
    	
    	
        // Split by '.' to get file extension
        String[] fileSplit = fileName.split("\\.");
        
        // Disregard files without extensions
        if(fileSplit.length <= 1)
        {
        	return FileExt.ERR;
        }
        
        // Determine and return proper extension
        String ext = fileSplit[fileSplit.length - 1];        
        for(FileExt x : FileExt.values())
        {
        	if(x.toString().equals(ext.toUpperCase()))
        		return x;
        }
        
        return FileExt.ERR;
    }
    
    /**
     * Returns the name (without the file extension) of a file.
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath)
    {
    	// Split path based on either '/' (*nix) or '\' (Windows)
        String[] dirSplit = filePath.split("/");
        if(dirSplit.length <= 1)
        {
            dirSplit = filePath.split("\\\\"); // One doubly-escaped (string + regex) backslash
        }
        String fileName = dirSplit[dirSplit.length - 1];
    	
    	
        // Split by '.' to get file extension
        String[] fileSplit = fileName.split("\\.");
        
        
        if(fileSplit.length == 0)
        {
        	return "";
        }
        
        String ret = fileSplit[0];
        
        for(int i = 1; i < fileSplit.length - 1; i++)
        {
        	ret += "." + fileSplit[i];
        }
        
        return ret;
    }
    
    /**
     * Returns the full path of a file, without it's extension.
     * @param filePath
     * @return
     */
    public static String getFilePathNoExt(String filePath)
    {
    	
    	/*// Split path based on either '/' (*nix) or '\' (Windows)
        String[] dirSplit = filePath.split("/");
        if(dirSplit.length <= 1)
        {
            dirSplit = filePath.split("\\\\"); // One doubly-escaped (string + regex) backslash
        }
        String fileName = dirSplit[dirSplit.length - 1];
    	
    	*/
    	
        // Split by '.' to get file extension
        String[] fileSplit = filePath.split("\\.");
        
        
        if(fileSplit.length == 0)
        {
        	return "";
        }
        
        String ret = fileSplit[0];
        for(int i = 1; i < fileSplit.length - 1; i++)
        {
        	ret += "." + fileSplit[i];
        }
        
        return ret;
    }
    
    /**
     * Saves a sheet from one format to another, as specified by the given paths.
     * 
     * @param srcPath The path of the source file.
     * @param destPath The destination path
     * @return True if the file was saved successfully.
     */
    public static boolean convertSheet(String srcPath, String destPath)
    {
    	// Check for valid file extensions
    	FileExt srcExt = getFileExt(srcPath);
    	
    	switch(srcExt)
    	{
    		case XLS:
    		case XLSX:
    		case CSV:
    			// Good
    			break;
    		default:
    			// Bad
    			return false;
    	}
    	DataDriver dd = DataDriverFactory.create(srcPath);

    	return saveDataDriver(dd, destPath);
    }
    
    /**
     * Saves a DataDriver to a file. The file type can be Excel (.xls/.xlsx) or CSV (.csv)
     * 
     * @param dd The DataDriver to save.
     * @param destPath The path to save to.
     * @return Save was successful.
     */
    public static boolean saveDataDriver(DataDriver dd, String destPath)
    {
    	FileExt destExt = getFileExt(destPath);
    	try 
    	{
	    	switch(destExt)
	    	{
	    		case XLS:    			
	    		case XLSX:
	    			Workbook wb = createWorkbook(destExt);
	    			Sheet sheet = wb.createSheet();
//	    			String[] dataRow;
//	    			for(int r = 0; dd.hasNext(); r++)
//	    			{
//	    				dataRow = dd.next();
//	    				Row row = sheet.createRow(r);
//		    			for(int c = 0; c < dataRow.length; c++)
//		    			{
//		    				row.createCell(c).setCellValue(dataRow[c]);
//		    			}
//	    			}
	    			DataRow dataRow;
	    			Row row = sheet.createRow(0);
	    			for(int c = 0; c < dd.getHeaders().toArray().length; c++)
	    			{
	    				row.createCell(c).setCellValue(dd.getHeaders().toArray()[c]);
	    			}
	    			for(int r = 1; dd.hasNext(); r++)
	    			{
	    				dataRow = dd.nextRow();
	    				row = sheet.createRow(r);
		    			for(int c = 0; c < dataRow.length; c++)
		    			{
		    				row.createCell(c).setCellValue(dataRow.getItem(dataRow.getHeaders().toArray()[c]));
		    			}
	    			}
	    			saveExcel(wb, destPath);
	    			break;
	    		case CSV:
	    			CSVWriter csv = new CSVWriter(new FileWriter(destPath));
	    			LinkedList<String[]> rows = new LinkedList<String[]>();
	    			rows.add(dd.getHeaders().toArray());
	        		while(dd.hasNext())
	        		{
	        			rows.add(dd.next());
	        		}
	        		csv.writeAll(rows);
	        		csv.close();
	    			break;
	    		default:
	    			// Bad
	    			return false;
	    	}
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * Saves a ResultSet to a file. The file type can be Excel (.xls/.xlsx) or CSV (.csv)
     * 
     * @param rs The ResultSet to save.
     * @param filePath The path to save to.
     * @return Save was successful
     */
    public static boolean saveResultSet(ResultSet rs, String filePath)
    {
    	if (getFileExt(filePath) != FileExt.CSV) {
    		DataDriver dd = DataDriverFactory.create(rs);
    		return FileTools.saveDataDriver(dd, filePath); 
    	}
    	
    	try {
    		CSVWriter writer = new CSVWriter(new FileWriter(filePath));
    		List<String[]> rows = new LinkedList<String[]>();
    		
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int colCount = rsmd.getColumnCount();
    		String[] headers = new String[colCount];
    		
    		for (int i = 1; i <= colCount; i++) {
    			headers[i-1] = rsmd.getColumnName(i);
    		}
    		rows.add(headers);
    		
    		while (rs.next()) {
    			String[] row = new String[colCount];
    			for (int i = 1; i <= colCount; i++) {
    				int type = rsmd.getColumnType(i);
    				String value = rs.getString(i);
    				
    				if (value != null) {
    				switch (type) {
	    				case java.sql.Types.CHAR:
	    					value = value.trim();
	    				/*case java.sql.Types.VARCHAR:
	    				case java.sql.Types.LONGVARCHAR:
	    				case java.sql.Types.NCHAR:
	    					value = "'" + value + "'";*/
	    					break;
	    				case java.sql.Types.DATE:
	    					String date = rs.getString(i);
	    					value = DateUtil.reformatDate(date, "yyyy-MM-dd HH:mm:ss");
	    					break;
	    				case java.sql.Types.TIMESTAMP:
	    					String ts = rs.getString(i);
	    					value = DateUtil.reformatDate(ts, "yyyy-MM-dd HH:mm:ss");
	    					break;
	    				case java.sql.Types.CLOB:
	    					java.sql.Clob clob = rs.getClob(i);
	    					
    						StringBuilder sb = new StringBuilder((int) clob.length());
    						Reader r = clob.getCharacterStream();
    						char[] buf = new char[2048];
    						int n;
    						while ((n = r.read(buf, 0, buf.length)) != -1) {
    							sb.append(buf, 0, n);
    						}
    						value =  new String(sb);
	    					
	    					break;
	    				/*case java.sql.Types.NUMERIC:
	    				case java.sql.Types.DECIMAL:
	    				case java.sql.Types.BIT:
	    				case java.sql.Types.TINYINT:
	    				case java.sql.Types.SMALLINT:
	    				case java.sql.Types.INTEGER:
	    				case java.sql.Types.BIGINT:
	    				case java.sql.Types.REAL:
	    				case java.sql.Types.FLOAT:
	    				case java.sql.Types.DOUBLE:*/
	    				}
    				} 
    				else {
    					value = "null";
    				}
    				row[i-1] = value;
    			}
    			rows.add(row);
    		}
    		writer.writeAll(rows);
    		writer.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;    	
    }
    
    /**
     * Copies a file to another location.
     * 
     * @param sourcePath The file to copy.
     * @param destPath The destination. Can be either a directory, or a new file name.
     * @throws IOException
     */
    public static void copyFile(String sourcePath, String destPath) throws IOException
    {
    	File sourceFile = new File(sourcePath);    
    	File destFile = new File(destPath);
    	// Check if destination folder exists
    	if(!destFile.getParentFile().exists())
    	{
    		destFile.getParentFile().mkdirs();    		
    	}
    	// Check if destination is a directory
    	if(getFilePathNoExt(destPath).equals(destPath))
		{
			destFile.mkdir();
			destFile = new File(destFile.getAbsolutePath() + "/" + sourceFile.getName());
		}
	
		// Copy the file
		if(!sourceFile.isFile())
			throw new IOException("Source is not a file.");
		
		Files.copy(sourceFile, destFile);
    }
        
    
    // Move/Rename file from one directory to another
    public static void AppendFileName(String oldPath, String newPath) throws SQLException, IOException{

    	    	
    	File file = new File(oldPath);
		       
		String fileName = file.getName();        
		String ext = FilenameUtils.getExtension(fileName);
		
		// checking if ext is null
		if(ext.isEmpty())
			//if ext is null throw expt
			throw new IOException (fileName + " (Please add file with extention)");
			
			
        String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
		File newFile = new File(newPath+ "\\/" + fileNameWithOutExt+"."+ext);

		
		if(file.renameTo(newFile)){
    		System.out.println("File is moved successful!");
    	   }else{
    		System.out.println("File is failed to move!");
    	   }
    }
    
	public static void writeTestData(LinkedList<TestRunData> trds)
	{
		// If path is Excel, write to Excel file
		// If it's CSV, copy the file and overwrite it
		FileExt ext = FileTools.getFileExt(TestRunData.FilePath);
		if(ext == FileExt.CSV)
		{
			CSV csv = CSV.create();
			DataDriver dd = DataDriverFactory.create(TestRunData.FilePath);
			final LinkedList<String[]> newRows = new LinkedList<String[]>();
			newRows.add(dd.getHeaders().toArray());
			String[] row;
			while(dd.hasNext())
			{
				row = dd.next();
				boolean found = false;
				for(TestRunData trd : trds)
				{
					if(row[dd.getHeaders().get(KDTConstants.TEST_CASE)].equals(trd.TestCaseID))
					{
						found = true;
						// Construct the new row;
						row[dd.getHeaders().get(KDTConstants.LAST_LOG)] = trd.LastLog;
						row[dd.getHeaders().get(KDTConstants.LAST_REPORT)] = trd.LastReport;
						row[dd.getHeaders().get(KDTConstants.LAST_RESULT)] = trd.LastResult;
						row[dd.getHeaders().get(KDTConstants.LAST_RUN)] = TestRunData.LastRun;						
					}
					
					if(found)
					{
						break;
					}
				}
				
				// Add row to output
				newRows.add(row);
			}
			
			// Write to file.			
			csv.write(TestRunData.FilePath, new CSVWriteProc() {
			    @Override
				public void process(CSVWriter out) {
			        out.writeAll(newRows);
			   }
			});
			
		}
		else //Excel
		{
			try {
				
				CloseableWorkbook wb = openExcel(TestRunData.FilePath);
				CreationHelper createHelper = wb.getCreationHelper();
				Sheet main = wb.getSheet(KDTConstants.TEST_CASES);
				int lastRunCol = 0, lastResultCol = 0, lastLogCol = 0, lastReportCol = 0, testCaseIDCol = 0;
				Row head = main.getRow(0);
				for(int i = 0; i < head.getLastCellNum(); i++)
				{
					String cell = head.getCell(i, Row.CREATE_NULL_AS_BLANK).toString();
					
					if(cell.equals(KDTConstants.LAST_LOG))
					{
						lastLogCol = i;
					}
					if(cell.equals(KDTConstants.LAST_REPORT))
					{
						lastReportCol = i;
					}
					if(cell.equals(KDTConstants.LAST_RESULT))
					{
						lastResultCol = i;
					}
					if(cell.equals(KDTConstants.LAST_RUN))
					{
						lastRunCol = i;
					}	
					if(cell.equals(KDTConstants.TEST_CASE))
					{
						testCaseIDCol = i;
					}
				}
				
				for(Row row : main)
				{
					for(TestRunData trd : trds)
					{
						boolean found = false;
						if(trd.TestCaseID.equals(row.getCell(testCaseIDCol, Row.CREATE_NULL_AS_BLANK).toString()))
						{
							found = true;
							
							CellStyle hlink_style = wb.createCellStyle();
						    Font hlink_font = wb.createFont();
						    hlink_font.setUnderline(Font.U_SINGLE);
						    hlink_font.setColor(IndexedColors.BLUE.getIndex());
						    hlink_style.setFont(hlink_font);
						    
							Hyperlink logLink = createHelper.createHyperlink(org.apache.poi.common.usermodel.Hyperlink.LINK_URL);
							logLink.setAddress(trd.LastLog);
							
							Hyperlink reportLink = createHelper.createHyperlink(org.apache.poi.common.usermodel.Hyperlink.LINK_URL);
							reportLink.setAddress(trd.LastReport);
							Cell logCell = row.getCell(lastLogCol, Row.CREATE_NULL_AS_BLANK);
							logCell.setCellValue(trd.LastLog);
							logCell.setHyperlink(logLink);
							logCell.setCellStyle(hlink_style);
							Cell reportCell = row.getCell(lastReportCol, Row.CREATE_NULL_AS_BLANK);
							reportCell.setCellValue(trd.LastReport);
							reportCell.setHyperlink(reportLink);
							reportCell.setCellStyle(hlink_style);
							row.getCell(lastResultCol, Row.CREATE_NULL_AS_BLANK).setCellValue(trd.LastResult);
							row.getCell(lastRunCol, Row.CREATE_NULL_AS_BLANK).setCellValue(TestRunData.LastRun);
						}
						if(found)
							break;
					}
				}
				
				saveExcel(wb, TestRunData.FilePath);
				//wb.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Converts a string representation of a number to an integer.
	 * @param toInt The String representing the integer. Accepts valid double notation,
	 * such as "123", "1.23", and "-8.56e3". Numbers with decimals are rounded
	 * to the nearest value.
	 * @return integer value of toInt. If the value cannot be converted to an integer,
	 * 0 is returned instead. If the value would be greater than 2147483647 or less 
	 * than -2147483648, those values are returned instead.
	 * is returned instead.
	 */
	public static int getSafeInt(String toInt)
	{
		if(toInt == null || toInt.equals(""))
		{
			return 0;
		}
		else
		{
			try {
				// Parse to double, then cast to int
				// (Excel returns all numbers as floats)
				double d = Double.parseDouble(toInt);
				return (int) Math.round(d);
			} catch(NumberFormatException e)
			{
				return 0;
			}
		}
	}
	
	 /**
     * Iterates over a directory for reading the files in it.
     * @param dirPath Path of the directory to open.
     * @throws IOException
     */
    public static Iterator<File> iterateDirectory (File dirPath) throws IOException{
    	Iterator<File> it = FileUtils.iterateFiles(dirPath, null, false);
    	return it;
    }
}
