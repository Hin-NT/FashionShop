package com.example.FashionShop.repository;

import com.example.FashionShop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findReviewsByCustomer_CustomerId(String customerId);
}
