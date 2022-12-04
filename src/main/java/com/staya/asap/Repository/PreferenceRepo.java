package com.staya.asap.Repository;

import com.staya.asap.Model.DB.PreferenceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PreferenceRepo {
    void createPreference(@Param("preference") PreferenceDTO preference);

    void updatePreference(@Param("preference") PreferenceDTO preference, @Param("userId") int userId);

    void updateWeight(@Param("preference") PreferenceDTO preference, @Param("userId") Integer userId);

    PreferenceDTO findByUserId(@Param("userId") Integer userId);

}
