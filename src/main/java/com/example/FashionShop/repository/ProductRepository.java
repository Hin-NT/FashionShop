package com.example.FashionShop.repository;

import com.example.FashionShop.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE p.productName LIKE :productName%")
    List<Product> findProductsByProductName(@Param("productName") String productName);
}
