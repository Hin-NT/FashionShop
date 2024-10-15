package com.example.FashionShop.dto;

import com.example.FashionShop.model.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {

    String categoryId;
    String categoryName;
    List<ProductDTO> products;

    public CategoryDTO(Category category, int choose) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        if(choose == 1) {
            this.products = category.getProducts().stream()
                    .map(product -> new ProductDTO(product, 1))
                    .toList();
        }
    }

}
