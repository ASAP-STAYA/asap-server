package com.staya.asap.Controller;


import com.staya.asap.Configuration.Security.Auth.PrincipalDetails;
import com.staya.asap.Model.DB.ReviewDTO;
import com.staya.asap.Service.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(maxAge = 3600)
public class ReviewController {

    private ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @PostMapping("/save")
    public String saveReview(@RequestBody ReviewDTO review){
        PrincipalDetails user = (PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setUser_id(user.getUserId());
        reviewService.saveReview(review);

        return "Review save done";
    }

}
