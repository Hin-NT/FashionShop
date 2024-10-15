package com.example.FashionShop.service.interfaces;

import com.example.FashionShop.dto.OrderDTO;
import com.example.FashionShop.enums.OrderStatus;
import com.example.FashionShop.enums.PeriodType;
import com.example.FashionShop.model.Order;
import org.springframework.http.ResponseEntity;

public interface IOrder extends IService<Order, OrderDTO> {

    ResponseEntity<String> confirmOrder(String orderId);

    ResponseEntity<String> cancelOrder(Order order);

    Order getOrder(String orderId);

    ResponseEntity<Double> getTotalRevenueByPeriodTime(PeriodType periodType, long quantity);

    ResponseEntity<String> updateOrderStatus(String orderId, OrderStatus orderStatus);
}
