package com.qualitestgroup.util.datadriver;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;

import com.qualitestgroup.util.fileio.CloseableWorkbook;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.kdt.exceptions.KDTParseException;

/**
 * Encapsulates a file path and sheet name.
 * 
 * This class is used to delay opening a DataDriver object until the data needs
 * to be read, in order to reduce unnecessary memory usage.
 * 
 * @author Matthew Swircenski
 * 
 */
public class DataSource {
	private String sourcePath;
	private String sheetName;
	private String filter = "";

	public DataDriver getDataDriver() {
		if (filter.isEmpty())
			return DataDriverFactory.create(sourcePath, sheetName);
		else
			return DataDriverFactory.create(sourcePath, sheetName, filter);
	}

	public Sheet getDataSheet() throws IOException {
		CloseableWorkbook wb = FileTools.openExcel(sourcePath);
		Sheet sh = wb.getSheet(sheetName);
		return sh;
	}

	/**
	 * Creates a DataSource object representing the given source path.
	 * 
	 * @param defaultPath
	 *            Default file path.
	 * @param defaultSheet
	 *            Default sheet name.
	 * @param src
	 *            String representing the sheet for the data source. Should be
	 *            of the form "{FilePath}::{SheetName}". If either the file path
	 *            or the sheet name is blank, defaultFile and/or defaultSheet
	 *            are substituted in respectively. Empty string is treated the
	 *            same as "::".
	 */
	public DataSource(String defaultPath, String defaultSheet, String src) throws KDTParseException {
		// Check for null source
		if (src == null) {
			this.sheetName = defaultSheet;
			this.sourcePath = defaultPath;
			return;
		}

		// Split off sheet name
		String[] sheetSplit = src.split("::");
		switch (sheetSplit.length) {
		case 0:
			// Empty string or "::"
			this.sheetName = defaultSheet;
			this.sourcePath = defaultPath;
			break;
		case 1:
			// Source is just a file path, use default sheet.
			this.sheetName = defaultSheet;
			break;
		case 2:
			// Source is of the form "<File path>::<sheet name>"
			this.sheetName = sheetSplit[1];
			break;
		default:
			throw new KDTParseException("Invlaid data source path: " + src);
		}

		// Get path
		if (sheetSplit.length != 0) {
			String path = sheetSplit[0];
			if (path.isEmpty()) {
				// Source is of the form "::<sheet name>" or is empty,
				// use default path.
				this.sourcePath = defaultPath;
			} else {
				this.sourcePath = path;
			}
		}

		// Get data filter
		String[] filterSplit = this.sheetName.split(":");

		if (filterSplit.length > 1) {
			this.sheetName = filterSplit[0];
			filter = filterSplit[1];
		}

		// Validate file type.
		FileTools.FileExt ext = FileTools.getFileExt(sourcePath);
		switch (ext) {
		case XLS:
		case XLSX:
		case CSV:
			break;
		default:
			throw new KDTParseException("Invalid file extension: " + sourcePath);
		}

	}
}
