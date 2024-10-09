package com.example.FashionShop.repository;

import com.example.FashionShop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    Optional<Product> findProductByProductName(String productName);
}
