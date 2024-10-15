package com.example.FashionShop.repository;

import com.example.FashionShop.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrder_OrderIdAndProductColorSize_ProductColorSizeId(String orderId, String productColorSizeId);
}
