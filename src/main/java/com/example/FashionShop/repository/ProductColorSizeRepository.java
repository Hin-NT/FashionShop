package com.example.FashionShop.repository;

import com.example.FashionShop.model.ProductColor;
import com.example.FashionShop.model.ProductColorSize;
import com.example.FashionShop.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductColorSizeRepository extends JpaRepository<ProductColorSize, String> {
    boolean existsByProductColorAndSize(ProductColor productColor, Size size);

    @Query("SELECT pcs FROM ProductColorSize pcs " +
            "JOIN pcs.productColor pc " +
            "JOIN pc.product p " +
            "WHERE (:categoriesId IS NULL OR p.category.categoryId IN :categoriesId) " +
            "AND (:stylesId IS NULL OR p.style.styleId IN :stylesId) " +
            "AND (:colorsId IS NULL OR pc.color.colorId IN :colorsId) " +
            "AND (:sizeId IS NULL OR pcs.size.sizeId IN :sizeId) " +
            "AND (:startPrice IS NULL OR pcs.price >= :startPrice) " +
            "AND (:endPrice IS NULL OR pcs.price <= :endPrice)")
    List<ProductColorSize> filterProducts(
            @Param("categoriesId") List<String> categoriesId,
            @Param("stylesId") List<String> stylesId,
            @Param("colorsId") List<String> colorsId,
            @Param("sizeId") List<String> sizeId,
            @Param("startPrice") Double startPrice,
            @Param("endPrice") Double endPrice
    );
}
