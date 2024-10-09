package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.ReviewDTO;
import com.example.FashionShop.model.Review;
import com.example.FashionShop.service.interfaces.ICustomer;
import com.example.FashionShop.service.interfaces.IReview;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class  ReviewController {

private final IReview reviewService;

private final ICustomer customerService;

@GetMapping("")
public ResponseEntity<ResponseDTO<List<ReviewDTO>>> getAllReviews() {
    return reviewService.getAll();
}

@GetMapping("/{reviewId}")
public ResponseEntity<ResponseDTO<ReviewDTO>> getReviewById(@PathVariable String reviewId) {
    Review review = new Review();
    review.setReviewId(reviewId);

    return reviewService.findById(review);
}

@GetMapping("/customer/{customerId}")
public ResponseEntity<ResponseDTO<List<ReviewDTO>>> getReviewsByCustomerId(@PathVariable String customerId) {
    return reviewService.getReviewsByCustomerId(customerId);
}

@PostMapping("")
public ResponseEntity<ResponseDTO<Review>> createReview(@RequestBody Review review) {
    Util.trimFields(review);
    return reviewService.create(review);
}

@PutMapping("")
public ResponseEntity<ResponseDTO<Review>> updateReview(@RequestBody Review review) {
    Util.trimFields(review);
    return reviewService.update(review);
}

@DeleteMapping("/{reviewId}")
public ResponseEntity<ResponseDTO<String>> deleteReview(@PathVariable String reviewId) {
    Review review = new Review();
    review.setReviewId(reviewId);
    return reviewService.delete(review);
}

}
