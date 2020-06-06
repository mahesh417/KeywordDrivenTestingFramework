package com.qualitestgroup.util;

import java.io.IOException;

import javax.swing.filechooser.*;
import javax.swing.*;

import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.util.fileio.FileTools.FileExt;
import static com.qualitestgroup.kdt.KDTConstants.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Creates an Excel template for a KDT document
 * @author Matt Swircenski
 *
 */
public class TemplateCreator {
	
	private static HSSFWorkbook hssfTemplate;
	private static XSSFWorkbook xssfTemplate;
	
	private static void createTemplate(String path)
	{
		
		switch(FileTools.getFileExt(path))
		{
			case XLS:
				if(hssfTemplate == null)
					createWorkbook(FileExt.XLS);
				try {
					FileTools.saveExcel(hssfTemplate, path);
				} catch (IOException e) {
					System.out.println("Fail");
				}
				break;
			case XLSX:
				if(xssfTemplate == null)
					createWorkbook(FileExt.XLSX);
				try {
					FileTools.saveExcel(xssfTemplate, path);
				} catch (IOException e)
				{
					System.out.println("Fail");
				}
				
				break;
			default:
				System.out.println("Fail");
				return;		
		}
	}

	private static void createWorkbook(FileExt ft) {
		Workbook wb;
		if(ft == FileExt.XLS)
		{			
			hssfTemplate = new HSSFWorkbook();
			wb = hssfTemplate;
		}
		else
		{
			xssfTemplate = new XSSFWorkbook();
			wb = xssfTemplate;
		}
		CellStyle cs = wb.createCellStyle();
		cs.setShrinkToFit(true);
		
		// Create sheets
		Sheet cases = wb.createSheet(TEST_CASES);
		Sheet steps = wb.createSheet(TEST_STEPS);
		wb.createSheet(TEST_DATA);
		
		// Create header rows
		Row caseHead = cases.createRow(0);
		int i;
		for(i = 0; i < caseReqColumns.length; i++)
		{
			Cell c = caseHead.createCell(i, Cell.CELL_TYPE_STRING);
			c.setCellValue(caseReqColumns[i]);
			//cases.autoSizeColumn(i);		
		}
		for(int j = 0; j < caseOptColumns.length; i++, j++)
		{
			Cell c = caseHead.createCell(i, Cell.CELL_TYPE_STRING);
			c.setCellValue(caseOptColumns[j]);
			//cases.autoSizeColumn(i);
		}
		
		Row stepHead = steps.createRow(0);
		for(int k = 0; k < scenColNames.length; k++)
		{
			Cell c = stepHead.createCell(k, Cell.CELL_TYPE_STRING);
			c.setCellValue(scenColNames[k]);
			//steps.autoSizeColumn(k);
		}
		
		
	}

	public static void main(String[] args) {
		if(args.length == 0)
		{
			String path = getSaveLocation();
			createTemplate(path);
		}
		else
		{
			for(String path : args)
			{
				createTemplate(path);
			}
		}
		
	}
	
	public static String getSaveLocation()
	{
		JFileChooser FC = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel & CSV files", "xls", "xlsx", "csv");
		FC.setFileFilter(filter);
		int result = FC.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			return FC.getSelectedFile().getAbsolutePath();
		}
		else
		{
			return "";
		}
	}

}
