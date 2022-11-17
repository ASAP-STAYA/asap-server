package com.staya.asap.Controller;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Service.ParkingService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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

    public ParkingController(ParkingService parkingService){
        this.parkingService = parkingService;
    }


    @GetMapping("/save")
    public String saveParkingLotData()
    {
        ExcelToDatabase excelToDatabase = new ExcelToDatabase();
//        excelToDatabase.upload();

        List<ParkingDTO> parkinglots = excelToDatabase.upload();
        System.out.println("save parkinglots::"+parkinglots);

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
    public boolean hasParkingLot(@RequestParam String searching) {
//        String searching = "신촌역 주차장";
//      &category_group_code=PK6
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

        //GetForObject는 헤더를 정의할 수 없음
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        System.out.println("hasparkinglot:: "+result);
        ArrayList<Object> searchList = (ArrayList<Object>) result.getBody().get("documents");
        System.out.println("hasparkinglot:: "+searchList);


        if (!searchList.isEmpty()){

            LinkedHashMap searchItem = (LinkedHashMap) searchList.get(0);
            System.out.println("hasparkinglot:: "+searchItem);

            ArrayList<Float> LatLng = new ArrayList<>();
            LatLng.add(Float.parseFloat(searchItem.get("y").toString())); //위도
            LatLng.add(Float.parseFloat(searchItem.get("x").toString())); //경도

            ArrayList<Float> latLng = LatLng(searching);

            System.out.println("hasparkinglot:: " + "위도 차이: " +abs(LatLng.get(0)-latLng.get(0)) + "경도 차이: " + abs(LatLng.get(1)-latLng.get(1)));
            // category_group_code MT1 대형마트 SC4 학교 PK6 주차장 OL7 주유소 충전소 HP8 병원 (주차장 없을 경우)
            // 경도 위도 차이가 거의 없는 경우
            if (abs(LatLng.get(0)-latLng.get(0))<=0.001 || abs(LatLng.get(1)-latLng.get(1))<=0.001) {
                return true;
            }else{
                return false;
            }
        }

        return false;
    }
}
