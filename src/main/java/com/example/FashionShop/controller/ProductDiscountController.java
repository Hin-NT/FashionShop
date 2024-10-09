package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ProductDiscountDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.ProductDiscount;
import com.example.FashionShop.service.interfaces.IProductDiscount;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-discounts")
public class ProductDiscountController {

    private final IProductDiscount productDiscountService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<ProductDiscountDTO>>> getAllProductDiscounts() {
        return productDiscountService.getAll();
    }

    @GetMapping("/{productDiscountId}")
    public ResponseEntity<ResponseDTO<ProductDiscountDTO>> getProductDiscountById(@PathVariable String productDiscountId) {
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setProductDiscountId(productDiscountId);
        return productDiscountService.findById(productDiscount);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<ProductDiscount>> createProductDiscount(@RequestBody ProductDiscount productDiscount) {
        Util.trimFields(productDiscount);
        return productDiscountService.create(productDiscount);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<ProductDiscount>> updateProductDiscount(@RequestBody ProductDiscount productDiscount) {
        Util.trimFields(productDiscount);
        return productDiscountService.update(productDiscount);
    }

    @DeleteMapping("/{productDiscountId}")
    public ResponseEntity<ResponseDTO<String>> deleteProductDiscount(@PathVariable String productDiscountId) {
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setProductDiscountId(productDiscountId);
        return productDiscountService.delete(productDiscount);
    }
}
