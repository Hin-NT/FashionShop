package com.example.FashionShop.service.implement;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.ReviewDTO;
import com.example.FashionShop.model.Customer;
import com.example.FashionShop.model.Review;
import com.example.FashionShop.repository.CustomerRepository;
import com.example.FashionShop.repository.ReviewRepository;
import com.example.FashionShop.service.interfaces.IReview;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReview {

    private final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final CustomerRepository customerRepository;

    @Override
    public ResponseEntity<ResponseDTO<List<ReviewDTO>>> getAll() {
        try {
            List<Review> reviews = reviewRepository.findAll();
            if (reviews.isEmpty()) {
                logger.info("No Reviews found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body
                        (new ResponseDTO<>(null, "No Reviews found"));
            }
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK).body
                    (new ResponseDTO<>(reviewDTOs, "Reviews retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching reviews: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body
                    (new ResponseDTO<>(null, "Error occurred while fetching reviews" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Review>> create(Review review) {
        if (review == null) {
            logger.warn("Review is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    (new ResponseDTO<>(null, "Review is null"));
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        review.setCreatedAt(currentDateTime);
        review.setUpdatedAt(currentDateTime);

        if (review.getCustomer() != null && review.getCustomer().getCustomerId() != null) {
            Optional<Customer> customerOpt = customerRepository.findById(review.getCustomer().getCustomerId());
            if(customerOpt.isEmpty()) {
                logger.warn("Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(null, "Customer not found"));
            }
        } else {
            logger.warn("Customer ID is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    (new ResponseDTO<>(null, "Customer ID is null"));
        }
        try {
            Review reviewSaved = reviewRepository.save(review);

            logger.info("Review created: {}", reviewSaved);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseDTO<>(reviewSaved, "Review created successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while creating review: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDTO<>(null, "Error occurred while creating review" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<ReviewDTO>> findById(Review review) {
        if (review == null || review.getReviewId() == null) {
            logger.warn("Review or ReviewId is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(
                            null, "Review or ReviewId is null"));
        }

        try {
            Optional<Review> reviewOptional = reviewRepository.findById(review.getReviewId());
            if (reviewOptional.isPresent()) {
                ReviewDTO reviewDTO = new ReviewDTO(reviewOptional.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseDTO<>(reviewDTO, "Review found"));
            } else {
                logger.warn("Review with ID: {} not found", review.getReviewId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO<>(
                                null, "Review not found"));
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching review: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(
                            null, "Error occurred while fetching review: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<List<ReviewDTO>>> getReviewsByCustomerId(String customerId) {
        try {
            List<Review> reviews = reviewRepository.findReviewsByCustomer_CustomerId(customerId);

            if (reviews.isEmpty()) {
                logger.info("No Reviews found for customer ID: {}", customerId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new ResponseDTO<>(null, "No Reviews found for customer ID: " + customerId));
            }

            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .toList();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDTO<>(reviewDTOs, "Reviews retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while fetching reviews: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDTO<>(null, "Error occurred while fetching reviews" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<Review>> update(Review review) {
        String reviewId = review.getReviewId();

        try {
            if (reviewId == null || reviewId.isEmpty()) {
                logger.warn("ReviewId is null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseDTO<>(null, "ReviewId is null"));
            }

            Optional<Review> existingReviewOptional = reviewRepository.findById(reviewId);
            if (existingReviewOptional.isEmpty()) {
                logger.warn("Review with ID: {} not found", reviewId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseDTO<>(null, "Review not found"));
            }

            review.setUpdatedAt(LocalDateTime.now());

            Review reviewUpdated = reviewRepository.save(review);
            logger.info("Review updated successfully: {}", reviewUpdated);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(
                    reviewUpdated, "Review updated successfully"));
        } catch (Exception e) {
            logger.error("Error occurred while updating review: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(
                    null, "Error occurred while updating review" + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO<String>> delete(Review review) {
        if (review == null || review.getReviewId() == null) {
            logger.warn("Review or Review ID is null when deleting review");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(
                    null, "Review or Review ID is null"));
        }

        try {
            if (reviewRepository.existsById(review.getReviewId())) {
                reviewRepository.deleteById(review.getReviewId());
                logger.info("Review with ID: {} deleted successfully", review.getReviewId());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(
                        null, "Review deleted successfully"));
            } else {
                logger.warn("Review not found with ID: {}", review.getReviewId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>(
                        null, "Review not found with ID: " + review.getReviewId()));
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting review with ID: {}: {}", review.getReviewId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(
                    null, "Error occurred while deleting review: " + e.getMessage()));
        }
    }

    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 5;
        }
        return reviews.stream().mapToInt(Review::getRating).average().orElse(5);
    }

}
