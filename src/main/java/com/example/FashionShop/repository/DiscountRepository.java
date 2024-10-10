package com.example.FashionShop.repository;

import com.example.FashionShop.enums.DiscountStatus;
import com.example.FashionShop.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    @Query("SELECT d FROM Discount d WHERE d.discountName LIKE :discountName%")
    List<Discount> findDiscountsByDiscountName(@Param("discountName") String discountName);

    List<Discount> findDiscountByDiscountStatus(DiscountStatus discountStatus);

}
