package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ProductDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Product;
import org.springframework.http.ResponseEntity;

public interface IProduct extends IService<Product, ProductDTO> {
    ResponseEntity<ResponseDTO<ProductDTO>> findProductByProductName(String productName);
}