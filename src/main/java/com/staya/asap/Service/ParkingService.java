package com.staya.asap.Service;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.UserDTO;
import com.staya.asap.Repository.ParkingRepo;
import com.staya.asap.Repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingService {

    private final ParkingRepo parkingRepo;

    public ParkingService(ParkingRepo parkingRepo) {
        this.parkingRepo = parkingRepo;
    }

    public ParkingDTO getParkingLotById(Integer id) {
        return this.parkingRepo.findById(id);
    }

    public void saveParkingLot(ParkingDTO parkingDTO) {
        this.parkingRepo.createParkingLot(parkingDTO);
    }

    public List<ParkingDTO> findAdjacentParkingLot(ArrayList<Float> latlng) {
        Float lat = latlng.get(0);
        Float lng = latlng.get(1);
        return this.parkingRepo.findByLatLng(lat,lng);
    }
}
