package com.qualitestgroup.util.fileio;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Closeable Workbook class, so that the file handle for the workbook can be closed.
 * 
 * @author Matthew Swircenski
 *
 */
public class CloseableWorkbook implements Closeable, Workbook {

	private Workbook wb;
	private Closeable file;
	
	/**
	 * Create a new CloseableWorkbook
	 * @param wb The Workbook
	 * @param file The file handler for the workbook.
	 */
	public CloseableWorkbook(Workbook wb, Closeable file)
	{
		this.wb = wb;
		this.file = file;
	}
	
	public Workbook getWorkbook()
	{
		return wb;
	}
	
	@Override
	public int addPicture(byte[] arg0, int arg1) {
				int ret = wb.addPicture(arg0, arg1);
				return ret;
	}

	@Override
	public void addToolPack(UDFFinder arg0) {
		wb.addToolPack(arg0);
	}

	@Override
	public Sheet cloneSheet(int arg0) {
				Sheet ret = wb.cloneSheet(arg0);
				return ret;
	}

	@Override
	public CellStyle createCellStyle() {
				CellStyle ret = wb.createCellStyle();
				return ret;
	}

	@Override
	public DataFormat createDataFormat() {
				DataFormat ret = wb.createDataFormat();
				return ret;
	}

	@Override
	public Font createFont() {
				Font ret = wb.createFont();
				return ret;
	}

	@Override
	public Name createName() {
				Name ret = wb.createName();
				return ret;
	}

	@Override
	public Sheet createSheet() {
				Sheet ret = wb.createSheet();
				return ret;
	}

	@Override
	public Sheet createSheet(String arg0) {
				Sheet ret = wb.createSheet(arg0);
				return ret;
	}

	@Override
	public Font findFont(short arg0, short arg1, short arg2, String arg3,
			boolean arg4, boolean arg5, short arg6, byte arg7) {
				Font ret = wb.findFont(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				return ret;
	}

	@Override
	public int getActiveSheetIndex() {
				int ret = wb.getActiveSheetIndex();
				return ret;
	}

	@Override
	public List<? extends PictureData> getAllPictures() {
				List<? extends PictureData> ret = wb.getAllPictures();
				return ret;
	}

	@Override
	public CellStyle getCellStyleAt(short arg0) {
				CellStyle ret = wb.getCellStyleAt(arg0);
				return ret;
	}

	@Override
	public CreationHelper getCreationHelper() {
				CreationHelper ret = wb.getCreationHelper();
				return ret;
	}

	@Override
	public int getFirstVisibleTab() {
				int ret = wb.getFirstVisibleTab();
				return ret;
	}

	@Override
	public Font getFontAt(short arg0) {
				Font ret = wb.getFontAt(arg0);
				return ret;
	}

	@Override
	public boolean getForceFormulaRecalculation() {
				boolean ret = wb.getForceFormulaRecalculation();
				return ret;
	}

	@Override
	public MissingCellPolicy getMissingCellPolicy() {
				MissingCellPolicy ret = wb.getMissingCellPolicy();
				return ret;
	}

	@Override
	public Name getName(String arg0) {
				Name ret = wb.getName(arg0);
				return ret;
	}

	@Override
	public Name getNameAt(int arg0) {
				Name ret = wb.getNameAt(arg0);
				return ret;
	}

	@Override
	public int getNameIndex(String arg0) {
				int ret = wb.getNameIndex(arg0);
				return ret;
	}

	@Override
	public short getNumCellStyles() {
				short ret = wb.getNumCellStyles();
				return ret;
	}

	@Override
	public short getNumberOfFonts() {
				short ret = wb.getNumberOfFonts();
				return ret;
	}

	@Override
	public int getNumberOfNames() {
				int ret = wb.getNumberOfNames();
				return ret;
	}

	@Override
	public int getNumberOfSheets() {
				int ret = wb.getNumberOfSheets();
				return ret;
	}

	@Override
	public String getPrintArea(int arg0) {
				String ret = wb.getPrintArea(arg0);
				return ret;
	}

	@Override
	public Sheet getSheet(String arg0) {
				Sheet ret = wb.getSheet(arg0);
				return ret;
	}

	@Override
	public Sheet getSheetAt(int arg0) {
				Sheet ret = wb.getSheetAt(arg0);
				return ret;
	}

	@Override
	public int getSheetIndex(String arg0) {
				int ret = wb.getSheetIndex(arg0);
				return ret;
	}

	@Override
	public int getSheetIndex(Sheet arg0) {
				int ret = wb.getSheetIndex(arg0);
				return ret;
	}

	@Override
	public String getSheetName(int arg0) {
				String ret = wb.getSheetName(arg0);
				return ret;
	}

	@Override
	public boolean isHidden() {
				boolean ret = wb.isHidden();
				return ret;
	}

	@Override
	public boolean isSheetHidden(int arg0) {
				boolean ret = wb.isSheetHidden(arg0);
				return ret;
	}

	@Override
	public boolean isSheetVeryHidden(int arg0) {
				boolean ret = wb.isSheetVeryHidden(arg0);
				return ret;
	}

	@Override
	public void removeName(int arg0) {
				wb.removeName(arg0);
	}

	@Override
	public void removeName(String arg0) {
				wb.removeName(arg0);
	}

	@Override
	public void removePrintArea(int arg0) {
				wb.removePrintArea(arg0);
	}

	@Override
	public void removeSheetAt(int arg0) {
				wb.removeSheetAt(arg0);
	}

	@Override
	public void setActiveSheet(int arg0) {
				wb.setActiveSheet(arg0);
	}

	@Override
	public void setFirstVisibleTab(int arg0) {
				wb.setFirstVisibleTab(arg0);
	}

	@Override
	public void setForceFormulaRecalculation(boolean arg0) {
				wb.setForceFormulaRecalculation(arg0);
	}

	@Override
	public void setHidden(boolean arg0) {
				wb.setHidden(arg0);
	}

	@Override
	public void setMissingCellPolicy(MissingCellPolicy arg0) {
				wb.setMissingCellPolicy(arg0);
	}

	@Override
	public void setPrintArea(int arg0, String arg1) {
				wb.setPrintArea(arg0, arg1);
	}

	@Override
	public void setPrintArea(int arg0, int arg1, int arg2, int arg3, int arg4) {
				wb.setPrintArea(arg0, arg1, arg2, arg3, arg4);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setRepeatingRowsAndColumns(int arg0, int arg1, int arg2,
			int arg3, int arg4) {
				wb.setRepeatingRowsAndColumns(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void setSelectedTab(int arg0) {
				wb.setSelectedTab(arg0);
	}

	@Override
	public void setSheetHidden(int arg0, boolean arg1) {
				wb.setSheetHidden(arg0, arg1);
	}

	@Override
	public void setSheetHidden(int arg0, int arg1) {
				wb.setSheetHidden(arg0, arg1);
	}

	@Override
	public void setSheetName(int arg0, String arg1) {
				wb.setSheetName(arg0, arg1);
	}

	@Override
	public void setSheetOrder(String arg0, int arg1) {
				wb.setSheetOrder(arg0, arg1);
	}

	@Override
	public void write(OutputStream arg0) throws IOException {
				wb.write(arg0);
	}

	@Override
	public void close() throws IOException {
				file.close();
	}

}
