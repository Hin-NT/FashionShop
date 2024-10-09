package com.example.FashionShop.dto;

import com.example.FashionShop.model.Discount;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.model.ProductDiscount;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDiscountDTO {

    String productDiscountId;
    ProductColorSize productColorSize;
    Discount discount;
    int percent;

    public ProductDiscountDTO(ProductDiscount productDiscount) {
        this.productDiscountId = productDiscount.getProductDiscountId();
        this.productColorSize = productDiscount.getProductColorSize();
        this.discount = productDiscount.getDiscount();
        this.percent = productDiscount.getPercent();
    }
}
