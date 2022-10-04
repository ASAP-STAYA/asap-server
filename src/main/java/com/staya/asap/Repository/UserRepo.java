package com.staya.asap.Repository;

import com.staya.asap.Model.DB.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepo {
    public UserDTO findById(@Param("id") Integer id);

    public UserDTO findByEmail(@Param("email") String email);

    public void createUser(@Param("user") UserDTO user);

    public UserDTO findByUsername(@Param("username") String username);
}
