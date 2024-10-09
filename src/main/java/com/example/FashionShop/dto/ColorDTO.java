package com.example.FashionShop.dto;

import com.example.FashionShop.model.Color;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorDTO {

    String colorId;
    String colorName;
    List<String> productColorList;

    public ColorDTO(Color color, int choose) {
        this.colorId = color.getColorId();
        this.colorName = color.getColorName();
        if (choose == 1) {
            this.productColorList = color.getProductColorList()
                    .stream()
                    .map(productColor -> productColor.getProductColorId())
                    .toList();
        }
    }
}
