package com.staya.asap.Controller;

import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Service.ParkingService;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.staya.asap.Service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

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
    @Autowired
    private PreferenceService preferenceService;

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

    // 요금/거리 최댓값
    public final static Double MAX_COST_PREFER = 1000.0;
    public final static Double MAX_DIST_PREFER = 1.5;

    // 주차장 점수 계산 후 최적의 주차장 리턴
    public ParkingDTO finalParkingLot(List<ParkingDTO> filtered, PreferenceDTO prefer){
        Double cost_weight, cost_prefer, dist_weight, dist_prefer;
        cost_weight = prefer.getCost_weight();
        dist_weight = prefer.getDist_weight();
        cost_prefer = prefer.getCost_prefer();
        dist_prefer = prefer.getDist_prefer();

        if ( cost_prefer < 0 ){
            cost_prefer = MAX_COST_PREFER;
        }
        if (dist_prefer < 0){
            dist_prefer = MAX_DIST_PREFER;
        }
        double adv = 0.2;
        // 가중치 1 : 유저 선호 범위 내에 존재 => 0.2 / 그 외 => 0.8 (adv 사용)
        // 가중치 2 : 거리와 요금의 상대적인 중요도 비율 (cost_weight, dist_weight 사용)
        ParkingDTO result = filtered.get(0);
        double ParkingScore = 1000.0; //최댓값으로 초기화
        for (ParkingDTO data : filtered){
            // 현재 요일에 맞는 요금
            double rate = data.getCost();
            //System.out.println(rate);
            double dist = (double)(data.getDistance());
            double score = 0.0;

            if(rate < cost_prefer){
                rate *= adv;
            } else{
                rate *= (1-adv);
            }

            if(dist < dist_prefer){
                dist*=adv;
            } else{
                dist*=(1-adv);
            }
            score = rate*cost_weight + dist*dist_weight;
            if (score< ParkingScore){
                ParkingScore = score;
                result = data;
            }
        }
        //System.out.println(ParkingScore);
        return result;
    }


    @GetMapping("/findParkingLot")
    public ParkingDTO findParkingLot(@RequestParam double lat, @RequestParam double lng) {
        // 입력값 : 경도 , 위도
        // 리턴값 : 주차장 이름, 경도, 위도

        // 0. 로그인한 유저 정보 불러오기
        PrincipalDetails user = (PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getUserId();
        PreferenceDTO prefer;
        ParkingDTO result = new ParkingDTO();

        // 로그인한 유저
        System.out.println("Successfully Get User Data!");
        prefer = preferenceService.getPreferenceByUserId(userId);

        // 1. 주차장 탐색
        // 1) rad 반경 내 2) 주차 가능 대수 > 0 3) 운영 시간 내 4) 주차장 면적 5) 기계식 유무
        Integer rad = 1;
        List<ParkingDTO> searchList = parkingService.findAdjacentParkingLot(prefer, lat, lng, rad);
        // 주차장 X => 반경 늘려 재탐색
        if(searchList.isEmpty()) {
            rad = 2;
            searchList = parkingService.findAdjacentParkingLot(prefer, lat, lng, rad);

            if(searchList.isEmpty()){
                /*result.setId(-1);
                return result;*/
                // 주차 가능 주차장 없어서 가장 가까운 주차장으로 안내
                prefer.setCan_mechanical(true);
                prefer.setCan_narrow(true);
                searchList = parkingService.findAdjacentParkingLot(prefer,lat,lng,rad);
                return searchList.get(0);
            }
        }
        //System.out.println(searchList.size());
        // 2. 주차장 점수 계산 후 사용자 최적의 주차장 선정
        return finalParkingLot(searchList, prefer);

    }

}
