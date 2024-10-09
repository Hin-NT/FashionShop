package com.example.FashionShop.dto;

import com.example.FashionShop.model.Product;
import com.example.FashionShop.model.ProductColor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    String productId;
    String productName;
    String description;
    CategoryDTO category;
    StyleDTO style;
    List<String> productColorIds;

    public ProductDTO(Product product, int choose) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.category = new CategoryDTO(product.getCategory(), 0);
        this.style = new StyleDTO(product.getStyle(), 0);

        if (choose == 1) {
            this.productColorIds = product.getProductColorList().stream()
                    .map(ProductColor::getProductColorId)
                    .collect(Collectors.toList());
        }
    }
}
