package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.ProductDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProduct extends IService<Product, ProductDTO> {
    ResponseEntity<ResponseDTO<List<ProductDTO>>> findProductByProductName(String productName);

    ResponseEntity<ResponseDTO<List<ProductDTO>>> getAll(int page, int limit);
}