package com.qualitestgroup.util.datadriver;

import java.util.Iterator;
import java.util.LinkedList;

public class DataDriverBuilder {
	private LinkedList<String[]> rows;
	private DataRow.Headers headers;
	
	public DataDriverBuilder()
	{
		rows = new LinkedList<String[]>();
		headers = new DataRow.Headers(new String[0]);
	}
	
	public DataDriverBuilder(DataDriver dd)
	{
		this();
		this.appendDataDriver(dd);
	}
	
	public void appendDataDriver(DataDriver dd)
	{
		// check columns
		for(String column : dd.getHeaders().toArray())
		{
			if(!headers.containsKey(column))
			{
				int cols = headers.size();
				headers.put(column, cols++);
			}
		}
		
		// add rows
		while(dd.hasNext())
		{
			DataRow dr = dd.nextRow();
			String[] row = new String[headers.size()];
			java.util.Arrays.fill(row,"");
			
			for(String rowHead : dr.getHeaders())
			{
				int index = headers.get(rowHead);
				row[index] = dr.getItem(rowHead);
			}
			
			rows.add(row);
		}
	}
	
	public DataDriver toDataDriver()
	{
		return new DDBDataDriver();
	}
	
	private class DDBDataDriver implements DataDriver
	{
		public Iterator<String[]> iter;
		private DataRow.Headers heads;
		
		public DDBDataDriver()
		{
			iter = rows.iterator();
			heads = headers;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public String[] next() {
//			if(head)
//			{
//				head = false;
//				return heads.toArray();
//			}
//			else
//				return iter.next();
			return iter.next();
		}

		@Override
		public DataRow nextRow() {
			if(this.hasNext())
        	{
        		DataRow row = new DataRow(this, heads);
        		return row;
        	}
        	else
        		return null;
		}

		@Override
		public boolean hasColumns(String... columns) {
			for(String col : columns)
        	{
        		if(!heads.containsKey(col))
        		{
        			return false;
        		}
        	}
        	return true;
		}

		@Override
		public String[] getMissingColumns(String... columns) {
			LinkedList<String> ret = new LinkedList<String>();
        	for(String col : columns)
        	{
        		if(!heads.containsKey(col))
        		{
        			ret.add(col);
        		}
        	}
        	return ret.toArray(new String[0]);
		}

		@Override
		public DataRow.Headers getHeaders() {
			return heads;
		}

		@Override
		public boolean hasNextSheet() {
			// N/A for this context. return false.
			return false;
		}

		@Override
		public boolean nextSheet() {
			// N/A for this context. return false.
			return false;
		}

		@Override
		public String getSheetName() {
			// N/A for this context. return null.
			return null;
		}
		
	}


}
