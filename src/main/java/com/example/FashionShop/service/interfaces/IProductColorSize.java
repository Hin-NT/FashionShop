package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ProductColorSizeDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Filter;
import com.example.FashionShop.model.ProductColorSize;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductColorSize extends IService<ProductColorSize, ProductColorSizeDTO> {
    ResponseEntity<ResponseDTO<List<ProductColorSizeDTO>>> filterProduct(Filter filter);
}

