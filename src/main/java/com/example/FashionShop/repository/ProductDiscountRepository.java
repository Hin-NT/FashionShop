package com.example.FashionShop.repository;

import com.example.FashionShop.model.ProductDiscount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDiscountRepository extends CrudRepository<ProductDiscount, String> {
}
