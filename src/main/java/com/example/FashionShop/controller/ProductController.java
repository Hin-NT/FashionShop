package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ProductDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Product;
import com.example.FashionShop.service.interfaces.IProduct;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final IProduct productService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        return productService.getAll(page, limit);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDTO<ProductDTO>> getProductById(@PathVariable String productId) {
        Product product = new Product();
        product.setProductId(productId);

        return productService.findById(product);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Product>> createProduct(@RequestBody Product product) {
        Util.trimFields(product);
        return productService.create(product);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Product>> updateProduct(@RequestBody Product product) {
        Util.trimFields(product);
        return productService.update(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseDTO<String>> deleteProduct(@PathVariable String productId) {
        Product product = new Product();
        product.setProductId(productId);
        return productService.delete(product);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> getProductByProductName(@RequestParam String productName) {
        if (productName == null || productName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "ProductName is required"));
        }
        return productService.findProductByProductName(productName.trim());
    }

}
