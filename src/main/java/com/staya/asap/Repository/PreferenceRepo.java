package com.staya.asap.Repository;

import com.staya.asap.Model.DB.PreferenceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Mapper
@Repository
public interface PreferenceRepo {
    void createPreference(@Param("preference") PreferenceDTO preference);

    void updatePreference(@Param("preference") PreferenceDTO preference, @Param("userName") String userName);

    void updateWeights(@Param("preference") PreferenceDTO preference, @Param("userId") Integer userId);
    PreferenceDTO findById(@Param("id") Integer id);

    PreferenceDTO findByUserId(@Param("userId") Integer userId);

    PreferenceDTO findByUserName(@Param("userName") String userName);


}
