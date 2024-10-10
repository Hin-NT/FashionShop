package com.example.FashionShop.repository;

import com.example.FashionShop.model.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
    @Query("SELECT s FROM Style s WHERE s.styleName LIKE :styleName%")
    List<Style> findStylesByStyleName(@Param("styleName") String styleName);
}
