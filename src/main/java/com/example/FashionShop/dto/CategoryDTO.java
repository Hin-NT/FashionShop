package com.example.FashionShop.dto;

import com.example.FashionShop.model.Category;
import com.example.FashionShop.model.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {

    String categoryId;
    String categoryName;
    List<String> productNames;

    public CategoryDTO(Category category, int choose) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        if(choose == 1) {
            this.productNames = category.getProducts().stream()
                    .map(Product::getProductName)
                    .toList();
        }
    }
}
