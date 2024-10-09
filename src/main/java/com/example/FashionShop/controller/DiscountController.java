package com.example.FashionShop.controller;

import com.example.FashionShop.dto.DiscountDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.enums.DiscountStatus;
import com.example.FashionShop.model.Discount;
import com.example.FashionShop.service.interfaces.IDiscount;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DiscountController {

    private final IDiscount discountService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<DiscountDTO>>> getAllDiscounts() {
        return discountService.getAll();
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<ResponseDTO<DiscountDTO>> getDiscountById(@PathVariable String discountId) {
        Discount discount = new Discount();
        discount.setDiscountId(discountId);
        return discountService.findById(discount);
    }

    @GetMapping("/search/{discountName}")
    public ResponseEntity<ResponseDTO<DiscountDTO>> getDiscountByName(@PathVariable String discountName) {
        if (discountName == null || discountName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(null, "DiscountName is required"));
        }
        return discountService.findDiscountByDiscountName(discountName);
    }

    @GetMapping("/status/{discountStatus}")
    public ResponseEntity<ResponseDTO<List<DiscountDTO>>> getDiscountByStatus(@PathVariable DiscountStatus discountStatus) {
        return discountService.findDiscountByDiscountStatus(discountStatus);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Discount>> createDiscount(@RequestBody Discount discount) {
        Util.trimFields(discount);
        return discountService.create(discount);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Discount>> updateDiscount(@RequestBody Discount discount) {
        Util.trimFields(discount);
        return discountService.update(discount);
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<ResponseDTO<String>> deleteDiscount(@PathVariable String discountId) {
        Discount discount = new Discount();
        discount.setDiscountId(discountId);
        return discountService.delete(discount);
    }

}
