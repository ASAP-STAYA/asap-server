package com.staya.asap.Repository;

import com.staya.asap.Model.DB.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReviewRepo {
    // 리뷰 등록 순서로 찾기
    public ReviewDTO findById(@Param("id") Integer id);
    // 유저 아이디로 찾기
    public ReviewDTO findByUserId(@Param("user_id") Integer id);
    // 리뷰 등록하기
    public void createReview(@Param("review") ReviewDTO review);


}
