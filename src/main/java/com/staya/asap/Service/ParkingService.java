package com.staya.asap.Service;

import static java.lang.Math.abs;

import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Controller.ExcelToDatabase;
import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Repository.ParkingRepo;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ParkingService {
    public final static Double MAX_COST_PREFER = 1000.0;
    public final static Double MAX_DIST_PREFER = 1.5;

    private final ParkingRepo parkingRepo;
    private final PreferenceService preferenceService;

    public ParkingService(ParkingRepo parkingRepo, PreferenceService preferenceService) {
        this.parkingRepo = parkingRepo;
        this.preferenceService = preferenceService;
    }

    public void saveParkingLot(ParkingDTO parkingDTO) {
        this.parkingRepo.createParkingLot(parkingDTO);
    }

    // 주차가능한 반경 (rad)km 내의 주차장 리턴
    public List<ParkingDTO> findAdjacentParkingLot(PreferenceDTO prefer, double lat, double lng, Integer radius) {
        final Integer mechanical = (prefer.getCan_mechanical()) ? 1 : 0;
        final Integer narrow = (prefer.getCan_narrow()) ? 1 : 0;
        final Double dist_prefer = prefer.getDist_prefer();
        final Double cost_prefer = prefer.getCost_prefer();

        return this.parkingRepo.getSearchList(lat, lng, radius, mechanical, narrow, dist_prefer, cost_prefer);
    }

    public void saveParkingLotData() {
        ExcelToDatabase excelToDatabase = new ExcelToDatabase();
        List<ParkingDTO> parkingLots = excelToDatabase.upload();

        for (ParkingDTO parkingDTO : parkingLots) {
            saveParkingLot(parkingDTO);
        }
    }

    public ArrayList<Float> latLng(String searching) {
        final String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + searching + "&radius=20000";
        final String key = "d08482d7a9775511b47dfeaa8b8997f7";

        final ArrayList<Object> searchList = getSearchList(url, searching, key);
        final LinkedHashMap searchItem = (LinkedHashMap) searchList.get(0);
        System.out.println("LatLng::" + searchItem);

        final ArrayList<Float> LatLng = new ArrayList<>();
        LatLng.add(Float.parseFloat(searchItem.get("y").toString())); //위도
        LatLng.add(Float.parseFloat(searchItem.get("x").toString())); //경도

        return LatLng;
    }

    public ArrayList<Object> getSearchList(String url, String searching, String key) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key); //Authorization 설정
        final HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders); //엔티티로 만들기
        final URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", searching) //인자
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();

        final ResponseEntity<Map<String, Object>> result = restTemplate.exchange(
                    targetUrl, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                });

        return (ArrayList<Object>) result.getBody().get("documents");
    }

    public boolean hasParkingLot(String searching, Double lat, Double lng) {
        final String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + searching
                + "&category_group_code=PK6&radius=1";
        final String key = "d08482d7a9775511b47dfeaa8b8997f7";

        final ArrayList<Object> searchList = getSearchList(url, searching, key);

        if (!searchList.isEmpty()) {
            LinkedHashMap searchItem = (LinkedHashMap) searchList.get(0);
            System.out.println("hasparkinglot:: " + searchItem);

            ArrayList<Float> LatLng = new ArrayList<>();
            LatLng.add(Float.parseFloat(searchItem.get("y").toString())); //위도
            LatLng.add(Float.parseFloat(searchItem.get("x").toString())); //경도

            System.out.println(
                    "hasparkinglot:: 위도 차이: " + abs(LatLng.get(0) - lat) + "경도 차이: " + abs(LatLng.get(1) - lng));
            // category_group_code MT1 대형마트 SC4 학교 PK6 주차장 OL7 주유소 충전소 HP8 병원 (주차장 없을 경우)
            // 경도 위도 차이가 거의 없는 경우
            return abs(LatLng.get(0) - lat) <= 0.001 || abs(LatLng.get(1) - lng) <= 0.001;
        }
        return false;
    }

    public ParkingDTO getFinalParkingLot(List<ParkingDTO> filtered, PreferenceDTO prefer) {
        Double cost_weight, cost_prefer, dist_weight, dist_prefer;

        cost_weight = prefer.getCost_weight();
        dist_weight = prefer.getDist_weight();
        cost_prefer = prefer.getCost_prefer();
        dist_prefer = prefer.getDist_prefer();

        if (cost_prefer < 0) {
            cost_prefer = MAX_COST_PREFER;
        }
        if (dist_prefer < 0) {
            dist_prefer = MAX_DIST_PREFER;
        }

        final double adv = 0.2;
        // 가중치 1 : 유저 선호 범위 내에 존재 => 0.2 / 그 외 => 0.8 (adv 사용)
        // 가중치 2 : 거리와 요금의 상대적인 중요도 비율 (cost_weight, dist_weight 사용)
        ParkingDTO result = filtered.get(0);
        double ParkingScore = 1000.0; // 최댓값으로 초기화
        for (ParkingDTO data : filtered) {
            // 현재 요일에 맞는 요금
            double cost = data.getCost();
            double dist = (double) (data.getDistance());
            double score = 0.0;

            if (cost < cost_prefer) {
                cost *= adv;
            } else {
                cost *= (1 - adv);
            }

            if (dist < dist_prefer) {
                dist *= adv;
            } else {
                dist *= (1 - adv);
            }

            score = cost * cost_weight + dist * dist_weight;

            if (score < ParkingScore) {
                ParkingScore = score;
                result = data;
            }
        }

        return result;
    }

    public ParkingDTO findParkingLot(double lat, double lng) {
        // 0. 로그인한 유저 정보 불러오기
        final Integer userId = ((PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        final PreferenceDTO prefer = preferenceService.getPreferenceByUserId(userId);

        // 1. 주차장 탐색
        // 1) rad 반경 내 2) 주차 가능 대수 > 0 3) 운영 시간 내 4) 주차장 면적 5) 기계식 유무
        Integer rad = 1;
        List<ParkingDTO> searchList = findAdjacentParkingLot(prefer, lat, lng, rad);
        // 주차장 X => 반경 늘려 재탐색
        if (searchList.isEmpty()) {
            rad = 2;
            searchList = findAdjacentParkingLot(prefer, lat, lng, rad);

            if (searchList.isEmpty()) {
                // 주차 가능 주차장 없어서 가장 가까운 주차장으로 안내
                prefer.setCan_mechanical(true);
                prefer.setCan_narrow(true);
                searchList = findAdjacentParkingLot(prefer, lat, lng, rad);
                return searchList.get(0);
            }
        }
        // 2. 주차장 점수 계산 후 사용자 최적의 주차장 선정
        return getFinalParkingLot(searchList, prefer);
    }
}
