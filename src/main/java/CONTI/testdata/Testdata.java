package CONTI.testdata;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Testdata {

	private static HSSFSheet excelWSheet;

	private static HSSFWorkbook excelWBook;

	private static HSSFCell cell;

	public Testdata() {
	}

	/**
	 * to set the File path and to open the Excel file, Pass Excel Path and
	 * Sheetname as Arguments to this method
	 * 
	 * @param path
	 * @param sheetName
	 * @throws Exception
	 */
	public void setExcelFile(String path, String sheetName) throws Exception {

		// Open the Excel file
		FileInputStream ExcelFile = new FileInputStream(path);

		// Access the required test data sheet
		excelWBook = new HSSFWorkbook(ExcelFile);
		excelWSheet = excelWBook.getSheet(sheetName);
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
	public String getCellData(int rowNum, int colNum) throws Exception {
		cell = excelWSheet.getRow(rowNum).getCell(colNum);
		String cellData;
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

		return cellData.trim();
	}

	public static int getLastRowNumber() {
		return excelWSheet.getLastRowNum();
	}

	public static String getRow(int rowNum) {
		return excelWSheet.getRow(rowNum).toString();
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
