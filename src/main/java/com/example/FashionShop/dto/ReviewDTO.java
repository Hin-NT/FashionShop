package com.example.FashionShop.dto;

import com.example.FashionShop.model.Review;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDTO {

    String reviewId;
    int rating;
    String comment;
    CustomerDTO customer;
    ProductColorSizeDTO productColorSizeId;

    public ReviewDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.rating = review.getRating();
        this.comment = review.getComment();

        if (review.getCustomer() != null) {
            this.customer = new CustomerDTO(review.getCustomer(), 0);
        } else {
            this.customer = null;
        }

        if (review.getProductColorSize() != null) {
            this.productColorSizeId = new ProductColorSizeDTO(review.getProductColorSize(), 0);
        } else {
            this.productColorSizeId = null;
        }
    }
}
