package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ProductColorDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.service.interfaces.IProductColor;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-colors")
public class ProductColorController {

    private final IProductColor productColorService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ProductColorDTO>>> getAllProductColors() {
        return productColorService.getAll();
    }

    @GetMapping("/{productColorId}")
    public ResponseEntity<ResponseDTO<ProductColorDTO>> getProductColorById(@PathVariable String productColorId) {
        ProductColor productColor = new ProductColor();
        productColor.setProductColorId(productColorId);
        return productColorService.findById(productColor);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<ProductColor>> createProductColor(@RequestBody ProductColor productColor) {
        Util.trimFields(productColor);
        return productColorService.create(productColor);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<ProductColor>> updateProductColor(@RequestBody ProductColor productColor) {
        Util.trimFields(productColor);
        return productColorService.update(productColor);
    }

    @DeleteMapping("/{productColorId}")
    public ResponseEntity<ResponseDTO<String>> deleteProductColor(@PathVariable String productColorId) {
        ProductColor productColor = new ProductColor();
        productColor.setProductColorId(productColorId);
        return productColorService.delete(productColor);
    }
}

