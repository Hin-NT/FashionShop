package com.example.FashionShop.dto;

import com.example.FashionShop.model.Product;
import com.example.FashionShop.model.Style;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StyleDTO {

    String styleId;
    String styleName;
    List<String> productIds;

    public StyleDTO(Style style, int choose) {
        if (style != null) {
            this.styleId = style.getStyleId();
            this.styleName = style.getStyleName();

            if (choose == 1) {
                this.productIds = style.getProducts()
                        .stream()
                        .map(Product::getProductId)
                        .toList();
            }
        } else {
            this.styleId = null;
            this.styleName = null;
            this.productIds = List.of();
        }
    }

}
