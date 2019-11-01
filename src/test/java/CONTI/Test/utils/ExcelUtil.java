package CONTI.Test.utils;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.aventstack.extentreports.Status;

import CONTI.Test.utils.extentReports.ExtentTestManager;


public class ExcelUtil {
	
	private static Logger log = Logger.getLogger(ExcelUtil.class);

	private static HSSFSheet excelWSheet;
	private static HSSFWorkbook excelWBook;
	private static HSSFCell cell;

	/**
	 * to set the File path and to open the Excel file, Pass Excel Path and
	 * Sheetname as Arguments to this method
	 * 
	 * @param path
	 * @param sheetName
	 * @throws Exception
	 */
	public static void setExcelFile(String path, String sheetName) {
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);

			// Access the required test data sheet
			excelWBook = new HSSFWorkbook(ExcelFile);
			excelWSheet = excelWBook.getSheet(sheetName);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("setupTestdata failed", e);
		}

	}

	/**
	 * to read the test data from the Excel cell, in this we are passing parameters
	 * as Row num and Col num
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return
	 * @throws Exception
	 */
	public static String getCellData(int rowNum, int colNum) {
		String cellData = "";
		try {

			cell = excelWSheet.getRow(rowNum).getCell(colNum);

			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				cellData = "" + (int) cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_STRING:
				cellData = cell.getStringCellValue();
				break;
			default:
				cellData = "";
			}

		} catch (Exception e) {
			ExtentTestManager.getTest().log(Status.FAIL, "data could not be read");
			log.error("data could not be read in row " + rowNum + ", column " + colNum, e);
			e.printStackTrace();
		}

		return cellData.trim();

	}

	public static int getLastRowNumber() {
		return excelWSheet.getLastRowNum();
	}

	public static String getRow(int rowNum) {
		return excelWSheet.getRow(rowNum).toString();
	}
	
	public static String getRowData(int rowNum) {
		StringBuilder sb = new StringBuilder();
		Iterator<Cell> cells = excelWSheet.getRow(rowNum).cellIterator();
		int i = 0;
		
		while (cells.hasNext()) {
			cell = (HSSFCell) cells.next();						
			sb.append(ExcelUtil.getCellData(rowNum, i++));			
		}

		return sb.toString();

	}

	public static boolean isRowEmpty(int row, int exceed) { // to check invalid or blank
		HSSFRow rowFromExcel = excelWSheet.getRow(row);
		if (rowFromExcel == null)
			return (true);

		for (int c = rowFromExcel.getFirstCellNum(); c < exceed; c++) {
			if (rowFromExcel.getCell(c) == null || rowFromExcel.getCell(c).toString().trim().equals("")
					|| rowFromExcel.getCell(c).getCellType() == HSSFCell.CELL_TYPE_BLANK)
				return true;
		}
		return false;
	}

}
