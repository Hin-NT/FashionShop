package com.example.FashionShop.dto;

import com.example.FashionShop.model.Image;
import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.model.ProductColorSize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColorDTO {

    String productColorId;
    ProductDTO productDTO;
    String colorId;
    List<String> imageUrls;
    List<String> productColorSizeIds;

    public ProductColorDTO(ProductColor productColor, int choose) {
        this.productColorId = productColor.getProductColorId();
        this.productDTO = new ProductDTO(productColor.getProduct(), 0);
        this.colorId = productColor.getColor().getColorId();

        if (choose == 1) {
            this.imageUrls = productColor.getImageList().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());
            this.productColorSizeIds = productColor.getProductColorSizeList().stream()
                    .map(ProductColorSize::getProductColorSizeId)
                    .collect(Collectors.toList());
        }
    }
}
