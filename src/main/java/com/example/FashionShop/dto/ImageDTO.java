package com.example.FashionShop.dto;

import com.example.FashionShop.model.Image;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageDTO {

    String imageId;
    String imageUrl;

    public ImageDTO(Image image) {
        this.imageId = image.getImageId();
        this.imageUrl = image.getImageUrl();
    }
}
