package com.staya.asap;


import com.staya.asap.Model.DB.ParkingDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelToDatabase {

    public List<ParkingDTO> upload() throws IOException, InvalidFormatException {

        String filePath = "static/parkinglotdata.xlsx";
        ClassPathResource classPathResource = new ClassPathResource(filePath);

        List<ParkingDTO> parkinglots = new ArrayList<>();

        if(classPathResource.exists() == false){
            System.out.println("Invalid filePath : "+filePath);
            throw new IllegalArgumentException();
        }
        System.out.println("file path exists : "+classPathResource.getPath());

        InputStream is = classPathResource.getInputStream();
        System.out.println("test : "+is);
        ZipSecureFile.setMinInflateRatio(0);
        System.out.println("test : "+is);
        OPCPackage opc = OPCPackage.open(is);
        System.out.println("test : "+opc);

        XSSFWorkbook workbook = new XSSFWorkbook(classPathResource.getInputStream()); //xlsx 확장자만
        opc.close();

        System.out.println("test : "+workbook);
        int rowindex = 0;
        int columnindex = 0; //시트 수 (첫번째에만 존재하므로 0을 준다) //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        XSSFSheet sheet = workbook.getSheetAt(0); //행의 수
        System.out.println("test : "+sheet);

        int rows = sheet.getPhysicalNumberOfRows();
        Sheet worksheet = workbook.getSheetAt(0);
        System.out.println("test : "+rows + worksheet);

        ParkingDTO parkingDTO = null;

        for (rowindex = 1; rowindex < rows; rowindex++) { //행을읽는다
            // System.out.println("row : "+rowindex);

            XSSFRow row = sheet.getRow(rowindex);
            Row row2 = worksheet.getRow(rowindex);

            System.out.println("row2 : "+row2);

            parkingDTO = new ParkingDTO();

            parkingDTO.setPARKING_CODE((int) row2.getCell(0).getNumericCellValue());
            parkingDTO.setPARKING_NAME(row2.getCell(1).getStringCellValue());
            parkingDTO.setADDR(row2.getCell(2).getStringCellValue());
            parkingDTO.setPARKING_TYPE(row2.getCell(3).getStringCellValue());
            parkingDTO.setTEL(row2.getCell(4).getStringCellValue());

            parkingDTO.setCAPACITY((int) row2.getCell(5).getNumericCellValue());
            parkingDTO.setCAPACITY_AVAILABLE((int) row2.getCell(6).getNumericCellValue());

            parkingDTO.setWEEKDAY_BEGIN_TIME((int) row2.getCell(7).getNumericCellValue());
            parkingDTO.setWEEKDAY_END_TIME((int) row2.getCell(8).getNumericCellValue());
            parkingDTO.setWEEKEND_BEGIN_TIME((int) row2.getCell(9).getNumericCellValue());
            parkingDTO.setWEEKEND_END_TIME((int) row2.getCell(10).getNumericCellValue());
            parkingDTO.setHOLIDAY_BEGIN_TIME((int) row2.getCell(11).getNumericCellValue());
            parkingDTO.setHOLIDAY_END_TIME((int) row2.getCell(12).getNumericCellValue());

            parkingDTO.setPAY_YN(row2.getCell(13).getBooleanCellValue());
            parkingDTO.setSATURDAY_PAY_YN(row2.getCell(14).getBooleanCellValue());
            parkingDTO.setHOLIDAY_PAY_YN(row2.getCell(15).getBooleanCellValue());

            parkingDTO.setRATES((int) row2.getCell(16).getNumericCellValue());
            parkingDTO.setTIME_RATE((int) row2.getCell(17).getNumericCellValue());
            parkingDTO.setADD_RATES((int) row2.getCell(18).getNumericCellValue());
            parkingDTO.setADD_TIME_RATE((int) row2.getCell(19).getNumericCellValue());

            parkingDTO.setDAY_MAXIMUM((int) row2.getCell(20).getNumericCellValue());
            parkingDTO.setLAT(row2.getCell(21).getNumericCellValue());
            parkingDTO.setLNG(row2.getCell(22).getNumericCellValue());

            parkingDTO.setWIDE_YN(row2.getCell(23).getBooleanCellValue());
            parkingDTO.setMECHANICAL_YN(row2.getCell(24).getBooleanCellValue());
            parkingDTO.setRATES_PER_HOUR((int) row2.getCell(25).getNumericCellValue());

            if (row != null) { //셀의 수
                int cells = row.getPhysicalNumberOfCells();

                for (columnindex = 0; columnindex <= cells; columnindex++) { //셀값을 읽는다

                    XSSFCell cell = row.getCell(columnindex);
                    String value = ""; //셀이 빈값일경우를 위한 널체크
                    if (cell == null) {
                        continue;
                    } else { //타입별로 내용 읽기

                        switch (cell.getCellType()) {
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }
                    }
                }
            }
            parkinglots.add(parkingDTO);
        }
        return parkinglots;
    }
}