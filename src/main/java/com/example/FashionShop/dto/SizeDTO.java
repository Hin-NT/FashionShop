package com.example.FashionShop.dto;

import com.example.FashionShop.model.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeDTO {

    String sizeId;
    String description;

    public SizeDTO(Size size) {
        this.sizeId = size.getSizeId();
        this.description = size.getDescription();
    }
}
