package com.staya.asap.Controller;

import com.staya.asap.ExcelToDatabase;
import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Service.ParkingService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api/parking")
@CrossOrigin(exposedHeaders = "authorization", maxAge = 3600)
public class ParkingController {

    private ParkingService parkingService;

    public ParkingController(ParkingService parkingService){
        this.parkingService = parkingService;
    }


    @GetMapping("/save")
    public String saveParkingLotData(@RequestParam(value = "fileUpload") MultipartFile mf) throws Exception {

        ArrayList<ParkingDTO> parkinglots = new ArrayList<>();

        OPCPackage opcPackage = OPCPackage.open(mf.getInputStream());
        XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

        XSSFSheet sheet = workbook.getSheetAt(0); //행의 수
        int rows = sheet.getPhysicalNumberOfRows();
        Sheet worksheet = workbook.getSheetAt(0);

        for (int rowindex = 1; rowindex < rows; rowindex++) { //행을읽는다
            Row row = worksheet.getRow(rowindex);

            ParkingDTO parkingDTO = new ParkingDTO();

            parkingDTO.setPARKING_CODE((int) row.getCell(0).getNumericCellValue());
            parkingDTO.setPARKING_NAME(row.getCell(1).getStringCellValue());
            parkingDTO.setADDR(row.getCell(2).getStringCellValue());
            parkingDTO.setPARKING_TYPE(row.getCell(3).getStringCellValue());
            parkingDTO.setTEL(row.getCell(4).getStringCellValue());

            parkingDTO.setCAPACITY((int) row.getCell(5).getNumericCellValue());
            parkingDTO.setCAPACITY_AVAILABLE((int) row.getCell(6).getNumericCellValue());

            parkingDTO.setWEEKDAY_BEGIN_TIME((int) row.getCell(7).getNumericCellValue());
            parkingDTO.setWEEKDAY_END_TIME((int) row.getCell(8).getNumericCellValue());
            parkingDTO.setWEEKEND_BEGIN_TIME((int) row.getCell(9).getNumericCellValue());
            parkingDTO.setWEEKEND_END_TIME((int) row.getCell(10).getNumericCellValue());
            parkingDTO.setHOLIDAY_BEGIN_TIME((int) row.getCell(11).getNumericCellValue());
            parkingDTO.setHOLIDAY_END_TIME((int) row.getCell(12).getNumericCellValue());

            parkingDTO.setPAY_YN(row.getCell(13).getBooleanCellValue());
            parkingDTO.setSATURDAY_PAY_YN(row.getCell(14).getBooleanCellValue());
            parkingDTO.setHOLIDAY_PAY_YN(row.getCell(15).getBooleanCellValue());

            parkingDTO.setRATES((int) row.getCell(16).getNumericCellValue());
            parkingDTO.setTIME_RATE((int) row.getCell(17).getNumericCellValue());
            parkingDTO.setADD_RATES((int) row.getCell(18).getNumericCellValue());
            parkingDTO.setADD_TIME_RATE((int) row.getCell(19).getNumericCellValue());

            parkingDTO.setDAY_MAXIMUM((int) row.getCell(20).getNumericCellValue());
            parkingDTO.setLAT(row.getCell(21).getNumericCellValue());
            parkingDTO.setLNG(row.getCell(22).getNumericCellValue());

            parkingDTO.setWIDE_YN(row.getCell(23).getBooleanCellValue());
            parkingDTO.setMECHANICAL_YN(row.getCell(24).getBooleanCellValue());
            parkingDTO.setRATES_PER_HOUR((int) row.getCell(25).getNumericCellValue());

//            System.out.println("parkingDTO : "+parkingDTO);
            parkinglots.add(parkingDTO);
        }

        for (ParkingDTO parkingDTO : parkinglots) {
            parkingService.saveParkingLot(parkingDTO);
        }
        return "parkinglot save done";
    }

    @GetMapping("/data")
    public ParkingDTO parkingLotData()
    {
        ParkingDTO parkingDTO = parkingService.getParkingLotById(1);
        return parkingDTO;
    }

    public ArrayList<Float> LatLng(String searching){
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+searching+"&radius=20000";
        String key = "d08482d7a9775511b47dfeaa8b8997f7";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", searching) //인자
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        ArrayList<Object> searchList = (ArrayList<Object>) result.getBody().get("documents");
        LinkedHashMap searchItem = (LinkedHashMap) searchList.get(0);
        System.out.println("LatLng::"+searchItem);

        ArrayList<Float> LatLng = new ArrayList<>();
        LatLng.add(Float.parseFloat(searchItem.get("y").toString())); //위도
        LatLng.add(Float.parseFloat(searchItem.get("x").toString())); //경도

        return LatLng;
    }

    // 위치의 경도 위도 반환
    @GetMapping("/latlng")
    public ArrayList<Float> getLatLng(@RequestParam String searching) {

        ArrayList<Float> latLng = LatLng(searching);
        return latLng;
    }

    // 위치의 주차장 있는지 여부 확인
    @GetMapping("/hasParkingLot")
    public boolean hasParkingLot(@RequestParam String searching, Double lat, Double lng) {

        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+searching+"&category_group_code=PK6&radius=1";
        String key = "d08482d7a9775511b47dfeaa8b8997f7";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", searching) //인자
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        ArrayList<Object> searchList = (ArrayList<Object>) result.getBody().get("documents");


        if (!searchList.isEmpty()){

            LinkedHashMap searchItem = (LinkedHashMap) searchList.get(0);
            System.out.println("hasparkinglot:: "+searchItem);

            ArrayList<Float> LatLng = new ArrayList<>(); // 주차장
            LatLng.add(Float.parseFloat(searchItem.get("y").toString())); //위도
            LatLng.add(Float.parseFloat(searchItem.get("x").toString())); //경도

            System.out.println("hasparkinglot:: " + "위도 차이: " +abs(LatLng.get(0)-lat) + "경도 차이: " + abs(LatLng.get(1)-lng));
            // category_group_code MT1 대형마트 SC4 학교 PK6 주차장 OL7 주유소 충전소 HP8 병원 (주차장 없을 경우)
            // 경도 위도 차이가 거의 없는 경우
            if (abs(LatLng.get(0)-lat)<=0.001 || abs(LatLng.get(1)-lng)<=0.001) {
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
}
