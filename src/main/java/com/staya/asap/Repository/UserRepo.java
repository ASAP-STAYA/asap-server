package com.staya.asap.Repository;

import com.staya.asap.Model.DB.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepo {
    public UserDTO findById(@Param("id") Integer id);
}
