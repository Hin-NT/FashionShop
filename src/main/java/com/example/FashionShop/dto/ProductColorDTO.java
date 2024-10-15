package com.example.FashionShop.dto;

import com.example.FashionShop.model.ProductColor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColorDTO {

    String productColorId;
    ProductDTO productDTO;
    String colorId;
    List<ImageDTO> imageUrls;
    List<ProductColorSizeDTO> productColorSizes;

    public ProductColorDTO(ProductColor productColor, int choose) {
        this.productColorId = productColor.getProductColorId();
        this.productDTO = new ProductDTO(productColor.getProduct(), 0);
        this.colorId = productColor.getColor().getColorId();

        if (choose == 1) {
            this.imageUrls = productColor.getImageList().stream()
                    .map(ImageDTO::new)
                    .toList();
            this.productColorSizes = productColor.getProductColorSizeList().stream()
                    .map(productColorSize -> new ProductColorSizeDTO(productColorSize, 0))
                    .toList();
        }
    }

}
