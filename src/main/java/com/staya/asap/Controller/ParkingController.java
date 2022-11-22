package com.staya.asap.Controller;

import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Service.ParkingService;
import com.staya.asap.Service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private UserService userService;

    public ParkingController(ParkingService parkingService){
        this.parkingService = parkingService;
    }


    @GetMapping("/save")
    public String saveParkingLotData()
    {
        ExcelToDatabase excelToDatabase = new ExcelToDatabase();

        List<ParkingDTO> parkinglots = excelToDatabase.upload();

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

            ArrayList<Float> LatLng = new ArrayList<>();
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
    @GetMapping("/findParkingLot")
    public ParkingDTO findParkingLot(@RequestParam double lat, @RequestParam double lng) {
        // 입력값 : 경도 , 위도
        // 리턴값 : 주차장 이름, 경도, 위도

        // 1. 1km 반경 내의 주차장 탐색
        List<ParkingDTO> searchList = parkingService.findAdjacentParkingLot(lat,lng);
        if(!searchList.isEmpty()) {
            //System.out.println("find 1km inside parking lots ");
            searchList.forEach(data -> System.out.println("data : " + data));
        }


        // 2. 해당 유저 선호도 불러오기
        // 2-1. 로그인한 유저 정보 불러오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof PrincipalDetails) {
            username = ((PrincipalDetails)principal).getUsername();
        } else {
            username = principal.toString();

        }
        // 로그인한 유저
        if(username != "anonymousUser"){
            // 2-2. 유저의 선호도 정보 불러오기
            //UserDTO user = userService.getUserByEmail(username);

        }
        return searchList.get(0);
    }

}
