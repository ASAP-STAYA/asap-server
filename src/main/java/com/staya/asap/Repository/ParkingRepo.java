package com.staya.asap.Repository;

import com.staya.asap.Model.DB.ParkingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ParkingRepo {

    public ParkingDTO findById(@Param("id") Integer id);

}
