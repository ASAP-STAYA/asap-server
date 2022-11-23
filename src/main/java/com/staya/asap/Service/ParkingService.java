package com.staya.asap.Service;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.PreferenceDTO;
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

    // 주차가능한 반경 (rad)km 내의 주차장 리턴
    public List<ParkingDTO> findAdjacentParkingLot(PreferenceDTO prefer, double lat, double lng, Integer radius) {
        // userId 대신 preferenceDTO 들고오기

        Boolean mechanical = prefer.getCan_mechanical();
        Boolean narrow = prefer.getCan_narrow();
        Double dist_prefer = prefer.getDist_prefer();
        Double cost_prefer = prefer.getCost_prefer();
        return this.parkingRepo.getSearchList(lat,lng, radius, mechanical, narrow, dist_prefer, cost_prefer);
    }
}
