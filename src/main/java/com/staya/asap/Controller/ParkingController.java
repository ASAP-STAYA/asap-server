package com.staya.asap.Controller;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        excelToDatabase.upload();

        List<ParkingDTO> parkinglots = excelToDatabase.upload();

        for (ParkingDTO parkingDTO : parkinglots) {
            parkingService.saveParkingLot(parkingDTO);
        }
        return "parkinglot save done";
    }

    @GetMapping("/data")
    public String showParkingLotData()
    {
        return "parkinglot data";
    }
}
