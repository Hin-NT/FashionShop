package com.example.FashionShop.dto;

import com.example.FashionShop.model.Discount;
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
    ProductColorSizeDTO productColorSize;
    Discount discount;
    int percent;

    public ProductDiscountDTO(ProductDiscount productDiscount) {
        this.productDiscountId = productDiscount.getProductDiscountId();
        this.productColorSize = new ProductColorSizeDTO(productDiscount.getProductColorSize(), 0);
        this.discount = productDiscount.getDiscount();
        this.percent = productDiscount.getPercent();
    }

}
