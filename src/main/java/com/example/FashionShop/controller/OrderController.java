package com.example.FashionShop.controller;

import com.example.FashionShop.dto.OrderDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.Order;
import com.example.FashionShop.service.interfaces.IOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrder orderService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<OrderDTO>>> getAllOrders() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDTO<OrderDTO>> getOrderById(@PathVariable String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        return orderService.findById(order);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Order>> createOrder(@RequestBody Order order) {
        return orderService.create(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseDTO<Order>> updateOrder(@PathVariable String orderId, @RequestBody Order order) {
        order.setOrderId(orderId);
        return orderService.update(order);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseDTO<String>> deleteOrder(@PathVariable String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        return orderService.delete(order);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmOrder(@RequestParam String orderId) {
        return orderService.confirmOrder(orderId);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam String orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        return orderService.cancelOrder(order);
    }
}
