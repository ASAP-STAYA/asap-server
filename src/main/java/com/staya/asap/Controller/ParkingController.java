package com.staya.asap.Controller;

import com.staya.asap.Service.ParkingService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api/parking")
@CrossOrigin(exposedHeaders = "authorization", maxAge = 3600)
public class ParkingController {

    private ParkingService parkingService;

    public ParkingController(ParkingService parkingService){
        this.parkingService = parkingService;
    }


    @GetMapping("/data")
    public String showParkingLotData(HttpServletRequest request)
    {
        return "parkinglot data";
    }

    @GetMapping("/home")
    public String park()
    {
        return "parkinglot data";
    }
}
