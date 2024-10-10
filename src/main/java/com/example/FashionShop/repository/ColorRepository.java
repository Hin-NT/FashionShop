package com.example.FashionShop.repository;

import com.example.FashionShop.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, String> {
    @Query("SELECT c FROM Color c WHERE c.colorName LIKE :colorName%")
    List<Color> findColorsByColorName(@Param("colorName") String colorName);

}