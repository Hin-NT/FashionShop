package com.example.FashionShop.repository;

import com.example.FashionShop.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
    Optional<Style> findStyleByStyleName(String styleName);
}
