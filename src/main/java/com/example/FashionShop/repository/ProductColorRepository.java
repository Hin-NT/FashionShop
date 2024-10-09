package com.example.FashionShop.repository;

import com.example.FashionShop.model.Color;
import com.example.FashionShop.model.Product;
import com.example.FashionShop.model.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, String> {
    boolean existsByProductAndColor(Product product, Color color);
}

