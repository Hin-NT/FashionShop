package com.example.FashionShop.repository;

import com.example.FashionShop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE :categoryName%")
    List<Category> findCategoriesByCategoryName(@Param("categoryName") String categoryName);
}
