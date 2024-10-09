package com.example.FashionShop.dto;

import com.example.FashionShop.enums.ProductStatus;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.service.implement.ReviewService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColorSizeDTO {

    ReviewService reviewService;

    String productColorSizeId;
    ProductColorDTO productColor;
    String sizeId;
    double price;
    int quantity;
    ProductStatus productStatus;
    int numView;
    List<ReviewDTO> reviews;
    double startReview;

    public ProductColorSizeDTO(ProductColorSize productColorSize, int choose) {
        this.productColorSizeId = productColorSize.getProductColorSizeId();
        this.productColor = new ProductColorDTO(productColorSize.getProductColor(), 0);
        this.sizeId = productColorSize.getSize().getSizeId();
        this.price = productColorSize.getPrice();
        this.quantity = productColorSize.getQuantity();
        this.productStatus = productColorSize.getProductStatus();
        this.numView = productColorSize.getNumView();
        this.startReview = !productColorSize.getReviews().isEmpty() ? reviewService.calculateRating(productColorSize.getReviews()) : 5;
        if (choose == 1) {
            reviews = productColorSize.getReviews().stream().map(ReviewDTO::new).toList();
        }
    }

}
