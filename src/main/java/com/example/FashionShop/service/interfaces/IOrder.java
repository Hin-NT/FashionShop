package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.OrderDTO;
import com.example.FashionShop.model.Order;
import org.springframework.http.ResponseEntity;

public interface IOrder extends IService<Order, OrderDTO> {

    public ResponseEntity<String> confirmOrder(String orderId);

    public ResponseEntity<String> cancelOrder(Order order);

    public Order getOrder(String orderId);
}
