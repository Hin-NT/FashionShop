package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ProductColorSizeDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Filter;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.service.interfaces.IProductColorSize;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-color-sizes")
public class ProductColorSizeController {

    private final IProductColorSize productColorSizeService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<ProductColorSizeDTO>>> getAllProductColorSizes() {
        return productColorSizeService.getAll();
    }

    @GetMapping("/{productColorSizeId}")
    public ResponseEntity<ResponseDTO<ProductColorSizeDTO>> getProductColorSizeById(@PathVariable String productColorSizeId) {
        ProductColorSize productColorSize = new ProductColorSize();
        productColorSize.setProductColorSizeId(productColorSizeId);
        return productColorSizeService.findById(productColorSize);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<ProductColorSize>> createProductColorSize(@RequestBody ProductColorSize productColorSize) {
        Util.trimFields(productColorSize);
        return productColorSizeService.create(productColorSize);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<ProductColorSize>> updateProductColorSize(@RequestBody ProductColorSize productColorSize) {
        Util.trimFields(productColorSize);
        return productColorSizeService.update(productColorSize);
    }

    @DeleteMapping("/{productColorSizeId}")
    public ResponseEntity<ResponseDTO<String>> deleteProductColorSize(@PathVariable String productColorSizeId) {
        ProductColorSize productColorSize = new ProductColorSize();
        productColorSize.setProductColorSizeId(productColorSizeId);
        return productColorSizeService.delete(productColorSize);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseDTO<List<ProductColorSizeDTO>>> filterProductColorSize(@RequestBody Filter filter) {
        return productColorSizeService.filterProduct(filter);
    }
}
