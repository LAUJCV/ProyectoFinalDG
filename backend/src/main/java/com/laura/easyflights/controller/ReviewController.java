package com.laura.easyflights.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.easyflights.model.Review;
import com.laura.easyflights.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("http://localhost:3000")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    //Endpoint para guardar reviews
    @PostMapping("{productId}/user/{userId}")
    public Review saveReview(
        @PathVariable Long productId,
        @PathVariable Long userId,
        @RequestBody Map<String, Object> body
    ) {
    int rating = (int) body.get("rating");

    String comment = (String) body.get("comment");

    return reviewService.saveReview(productId, userId, rating, comment);
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviews(@PathVariable Long productId){
        return reviewService.findReviewsByProduct(productId);
    }
}
