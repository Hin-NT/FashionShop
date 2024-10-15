package com.example.FashionShop.dto;

import com.example.FashionShop.enums.ProductStatus;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.model.Review;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColorSizeDTO {

    String productColorSizeId;
    ProductColorDTO productColor;
    SizeDTO sizeId;
    double price;
    int quantity;
    ProductStatus productStatus;
    int numView;
    List<ReviewDTO> reviews;
    double startReview;

    public ProductColorSizeDTO(ProductColorSize productColorSize, int choose) {
        this.productColorSizeId = productColorSize.getProductColorSizeId();
        this.productColor = new ProductColorDTO(productColorSize.getProductColor(), 0);
        this.sizeId = new SizeDTO(productColorSize.getSize());
        this.price = productColorSize.getPrice();
        this.quantity = productColorSize.getQuantity();
        this.productStatus = productColorSize.getProductStatus();
        this.numView = productColorSize.getNumView();
        this.startReview = !productColorSize.getReviews().isEmpty() ? calculateRating(productColorSize.getReviews()) : 5;
        if (choose == 1) {
            reviews = productColorSize.getReviews().stream().map(ReviewDTO::new).toList();
        }
    }

    public double calculateRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 5;
        }
        return reviews.stream().mapToInt(Review::getRating).average().orElse(5);
    }


}
