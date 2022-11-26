package com.staya.asap.Service;

import com.staya.asap.Model.DB.ReviewDTO;
import com.staya.asap.Repository.ReviewRepo;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;

    public ReviewService(ReviewRepo reviewRepo) {this.reviewRepo = reviewRepo;}

    public void saveReview(ReviewDTO reviewDTO) {this.reviewRepo.createReview(reviewDTO);}
}
