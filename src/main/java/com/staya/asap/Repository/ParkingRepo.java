package com.staya.asap.Repository;

import com.staya.asap.Model.DB.ParkingDTO;
import com.staya.asap.Model.DB.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Mapper
public interface ParkingRepo {

    public ParkingDTO findById(@Param("id") Integer id);

    public void createParkingLot(@Param("parkinglot") ParkingDTO parkingDTO);

    public List<ParkingDTO> getSearchList(@Param("lat") double lat, @Param("lng") double lng, @Param("rad") Integer radius, @Param("mechanical") Integer mechanical, @Param("narrow") Integer narrow, @Param("dist") Double dist_prefer, @Param("cost") Double cost_prefer);
}
