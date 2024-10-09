package com.example.FashionShop.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Filter {
    ArrayList<String> categoriesId;
    ArrayList<String> stylesId;
    ArrayList<String> colorsId;
    ArrayList<String> sizeId;
    double startPrice;
    double endPrice;
}
