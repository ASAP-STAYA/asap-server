package com.staya.asap.Controller;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Service.ParkingService;
import com.staya.asap.Service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api/parking")
@CrossOrigin(maxAge=3600)
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
