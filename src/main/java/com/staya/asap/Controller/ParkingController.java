package com.staya.asap.Controller;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController //@Controller + @ResponseBody
@RequestMapping("/api/parking")
@CrossOrigin(exposedHeaders = "authorization", maxAge = 3600)
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/save")
    public String saveParkingLotData() {
        parkingService.saveParkingLotData();
        return "parkinglot save done";
    }

    // 위치의 경도 위도 반환
    @GetMapping("/latlng")
    public ArrayList<Float> getLatLng(@RequestParam String searching) {
        return parkingService.latLng(searching);
    }

    // 위치의 주차장 있는지 여부 확인
    @GetMapping("/hasParkingLot")
    public boolean hasParkingLot(@RequestParam String searching, Double lat, Double lng) {
        return parkingService.hasParkingLot(searching, lat, lng);
    }

    @GetMapping("/findParkingLot")
    public ParkingDTO findParkingLot(@RequestParam double lat, @RequestParam double lng) {
        return parkingService.findParkingLot(lat, lng);
    }

}
