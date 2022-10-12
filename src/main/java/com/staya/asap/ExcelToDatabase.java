//package com.staya.asap;
//
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class ExcelToDatabase {
//
//	public ArrayList<ArrayList<String>> readFilter(String fileName) throws IOException {
//		FileInputStream fis = new FileInputStream(fileName);
//
//		@SuppressWarnings("resource")
//		XSS workbook = new XSSFWorkbook(fis);
//		int rowindex = 0;
//		int columnindex = 0;
//		ArrayList<ArrayList<String>> filters = new ArrayList<ArrayList<String>>();
//
//		int sheetCn = workbook.getNumberOfSheets();	// 시트 수
//		for(int sheetnum=0; sheetnum<sheetCn; sheetnum++) {	// 시트 수만큼 반복
//
//			int sheetnum2=sheetnum+1;
//			System.out.println("sheet = " + sheetnum2);
//
//			XSSFSheet sheet = workbook.getSheetAt(sheetnum);	// 읽어올 시트 선택
//			int rows = sheet.getPhysicalNumberOfRows();    // 행의 수
//			XSSFRow row = null;
//
//			for (rowindex = 1; rowindex < rows; rowindex++) {	// 행의 수만큼 반복
//
//				row = sheet.getRow(rowindex);	// rowindex 에 해당하는 행을 읽는다
//				ArrayList<String> filter = new ArrayList<String>();	// 한 행을 읽어서 저장할 변수 선언
//
//				if (row != null) {
//					int cells = 13;	// 셀의 수
//					cells = row.getPhysicalNumberOfCells();    // 열의 수
//					for (columnindex = 0; columnindex <= cells; columnindex++) {	// 열의 수만큼 반복
//						XSSFCell cell_filter = row.getCell(columnindex);	// 셀값을 읽는다
//						String value = "";
//						// 셀이 빈값일경우를 위한 널체크
//						if (cell_filter == null) {
//							continue;
//						} else {
//							// 타입별로 내용 읽기
//							switch (cell_filter.getCellType()) {
//								case XSSFCell.CELL_TYPE_FORMULA:
//									value = cell_filter.getCellFormula();
//									break;
//								case XSSFCell.CELL_TYPE_NUMERIC:
//									value = cell_filter.getNumericCellValue() + "";
//									break;
//								case XSSFCell.CELL_TYPE_STRING:
//									value = cell_filter.getStringCellValue() + "";
//									break;
//								case XSSFCell.CELL_TYPE_BLANK:
//									value = cell_filter.getBooleanCellValue() + "";
//									break;
//								case XSSFCell.CELL_TYPE_ERROR:
//									value = cell_filter.getErrorCellValue() + "";
//									break;
//							}
//						}
//						filter.add(value);	//읽은 셀들을 filter에 추가 (행)
//					}
//				}
//				filters.add(filter); //filter(행)을 filters(열)에 추가
//			}
//		}
//		fis.close();	//파일 읽기 종료
//		return filters;	//리스트 반환
//	}
//
//
//}
