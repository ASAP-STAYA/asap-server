package com.staya.asap.Controller;


import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Model.DB.ReviewDTO;
import com.staya.asap.Service.PreferenceService;
import com.staya.asap.Service.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(maxAge = 3600)
public class ReviewController {

    private ReviewService reviewService;
    private PreferenceService preferenceService;

    public ReviewController(ReviewService reviewService, PreferenceService preferenceService) {
        this.reviewService = reviewService;
        this.preferenceService = preferenceService;
    }

    // 리뷰로 상대 중요도 업데이트
    public Boolean updateWeights(ReviewDTO review){
        Boolean updated = false;
        PreferenceDTO prefer = preferenceService.getPreferenceByUserId(review.getUser_id());

        // 어쩔 수 없이 배정된 기준 밖의 주차장의 경우 => 중요도 업데이트 필요 X
        if (review.getDist() > prefer.getDist_prefer() || review.getCost() > prefer.getCost_prefer()){
            return updated;
        }

        System.out.println(prefer.getDist_weight());
        System.out.println(prefer.getCost_weight());
        // 불만족 항목에 대한 상대 중요도를 높인다
        // discontent : 거리 불만족 = 0 / 요금 불만족 = 1
        if (review.getDiscontent() == 0){
            // 거리 불만족  == 거리가 중요한 사람 -> 요금 중요도 * 0.8
            double weight = prefer.getCost_weight()*0.8;
            prefer.setCost_weight(weight);
            prefer.setDist_weight(1-weight);
        } else {
            double weight = prefer.getDist_weight()*0.8;
            prefer.setDist_weight(weight);
            prefer.setCost_weight(1-weight);
        }
        System.out.println(prefer.getDist_weight());
        System.out.println(prefer.getCost_weight());
        preferenceService.updateWeights(prefer, review.getUser_id());
        return updated;
    }

    @PostMapping("/save")
    public String saveReview(@RequestBody ReviewDTO review){
        PrincipalDetails user = (PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setUser_id(user.getUserId());
        review.setId(0);
        reviewService.saveReview(review);

        if (review.getDiscontent() >= 0){
            updateWeights(review);
        }
        return "Review save done";
    }

}
