package com.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUpdate {

	static String xlpath = System.getProperty("user.dir") + "\\DFPExcelDump\\Analysed_Reports.xlsx";
	static String sheet = "Analysed_Reports"; //Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType"); //"Analysed_Reports";
	public static String UserType = "NA"; //Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("userType"); //"NonSubscribedUser";
	public static String ModuleName = "NA";
	static int row = (getRowCount()+1);
	static int counter = 0;

	public static void creatExcel() { 
		try {
			File file = new File(System.getProperty("user.dir") + "\\DFPExcelDump\\Analysed_Reports.xlsx");
			if (!file.exists()) {
				XSSFWorkbook workbook = new XSSFWorkbook();
				workbook.createSheet(sheet);
				FileOutputStream fos = new FileOutputStream(
						new File(System.getProperty("user.dir") + "\\DFPExcelDump\\Analysed_Reports.xlsx"));
				workbook.write(fos);
				workbook.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void writeData(String Validation, String result, String error) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
//			myExcelSheet
			XSSFRow xrow = myExcelSheet.getRow(row);
			if (xrow == null) {
				xrow = myExcelSheet.createRow(row);
			}
			Cell cell = null;
			if(counter == 0) {
				xrow = myExcelSheet.getRow(0);
				if (xrow == null) {
					xrow = myExcelSheet.createRow(0);
				}
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue("Module");
					cell = xrow.createCell(1);
					cell.setCellValue("Scenario");
					cell = xrow.createCell(2);
					cell.setCellValue("User Type");
					cell = xrow.createCell(3);
					cell.setCellValue("Validations");
					cell = xrow.createCell(4);
					cell.setCellValue("Results");
					cell = xrow.createCell(5);
					cell.setCellValue("Remarks");
					counter++;
				}
			}
			// Update the value of cell
			if (result.equals("Pass")) {
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(3);
					cell.setCellValue(Validation);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
				}
			} else if (result.equals("Fail")) {
				if (cell == null) {
					cell = xrow.createCell(0);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(5);
					cell.setCellValue(error);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
				}
			} else if (result.equals("Warning")) {
				if (cell == null) {
					cell = xrow.createCell(1);
					cell.setCellValue(ModuleName);
					cell = xrow.createCell(4);
					cell.setCellValue(result);
					cell = xrow.createCell(5);
					cell.setCellValue(error);
					cell = xrow.createCell(2);
					cell.setCellValue(UserType);
					row++;
				}
			}
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Node(String NodeName) {
		try {
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			FileOutputStream output = new FileOutputStream(xlpath);
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
//			myExcelSheet
			XSSFRow xrow = myExcelSheet.getRow(row);
			if (xrow == null) {
				xrow = myExcelSheet.createRow(row);
			}
			Cell cell = null;
			// Update the value of cell
			if (cell == null) {
				cell = xrow.createCell(1);
				cell.setCellValue(NodeName);
			}
			myExcelBook.write(output);
			myExcelBook.close();
		} catch (Exception e) {
		}
	}

	public int getMatchRow(String matchData) {
		String data = "";
		try {
			@SuppressWarnings("resource")
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			for (int i = 0; i < getRowCount(); i++) {
				data = myExcelSheet.getRow(i).getCell(0).toString();
				if (data.equals(matchData)) {
					return i;
				}
			}
		} catch (Exception e) {
		}
		return 0;
	}

	// Generic method to return the column values in the sheet.
	public static String getCellValue(int row, int col) {
		String data = "";
		try {
			@SuppressWarnings("resource")
			XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
			XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
			data = myExcelSheet.getRow(row).getCell(col).toString();
		} catch (Exception e) {
		}
		return data;
	}

	// Generic method to return the number of rows in the sheet.
	public static int getRowCount() {
		int rc = 0;
		try {
			FileInputStream fis = new FileInputStream(xlpath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet s = wb.getSheet(sheet);
			rc = s.getLastRowNum();
		} catch (Exception e) {
		}
		return rc;
	}

	@SuppressWarnings("resource")
	public static String Iterator(String toFind) throws IOException {
//		String toFind = ID;
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(xlpath));
		XSSFSheet myExcelSheet = myExcelBook.getSheet(sheet);
		for (Row row : myExcelSheet) {
			for (Cell cell : row) {
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
				DataFormatter formatter = new DataFormatter();
				String text = formatter.formatCellValue(cell);
				if (toFind.equals(text)) {
					return cellRef.formatAsString().replaceAll("\\D+", "");
				} else if (text.contains(toFind)) {
					System.out.println("Text found as part of " + cellRef.formatAsString());
				}
			}
		}
		return "";
	}

	public static void EnterResult(String ID, String result) throws NumberFormatException, IOException {
		if (!ID.isEmpty()) {
//			writeData(Integer.valueOf(Iterator(ID)), result);
		}
	}

	public static void main(String[] args) throws IOException {
//		for (int i = 1; i < 100; i++) {
//			System.out.println(getCellValue(i, 0));
//		}
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
//		writeData(2102,7,"Pass");
//		EnterResult("TC_1130", "Pass");
//		EnterResult("TC_1108");
//		EnterResult("TC_1028");
//		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
//		System.out.println((getRowCount()+1));
//		row = (getRowCount()+1);
		writeData("ABC","Fail","Error");
	}

}
