package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.DiscountDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.enums.DiscountStatus;
import com.example.FashionShop.model.Discount;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDiscount extends IService<Discount, DiscountDTO> {

    public void updateDiscountByDiscountStatus();

    ResponseEntity<ResponseDTO<DiscountDTO>> findDiscountByDiscountName(String discountName);

    ResponseEntity<ResponseDTO<List<DiscountDTO>>> findDiscountByDiscountStatus(DiscountStatus discountStatus);
}


