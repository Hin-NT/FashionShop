package com.example.FashionShop.dto;

import com.example.FashionShop.model.Discount;
import com.example.FashionShop.model.ProductDiscount;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountDTO {

    String discountId;
    String discountName;
    String discountStatus;
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<String> productDiscountIds;

    public DiscountDTO(Discount discount, int choose) {
        if (discount != null) {
            this.discountId = discount.getDiscountId();
            this.discountName = discount.getDiscountName();
            this.discountStatus = discount.getDiscountStatus() != null ? discount.getDiscountStatus().name() : null;
            this.startTime = discount.getStartTime();
            this.endTime = discount.getEndTime();

            if (choose == 1) {
                this.productDiscountIds = discount.getProductDiscountList()
                        .stream()
                        .map(ProductDiscount::getProductDiscountId)
                        .toList();
            }
        }
    }
}
