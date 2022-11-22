package com.staya.asap.Controller;


import com.staya.asap.Model.DB.ReviewDTO;
import com.staya.asap.Service.ReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(maxAge = 3600)
public class ReviewController {

    private ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {this.reviewService = reviewService;}

    @PostMapping("/save")
    public String saveReview(@RequestBody ReviewDTO review){
        /*Integer userid = review.getUser_id();
        System.out.println(userid);
        review.setUser_id(0);
        review.setCost(1234);
        review.setDist(52103);
        review.setDiscontent(2);*/
        reviewService.saveReview(review);

        return "Review save done";
    }

}
