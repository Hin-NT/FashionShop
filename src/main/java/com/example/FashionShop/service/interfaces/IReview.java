package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.ReviewDTO;
import com.example.FashionShop.model.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReview extends IService<Review, ReviewDTO> {

    ResponseEntity<ResponseDTO<List<ReviewDTO>>> getReviewsByCustomerId(String customerId);

}
