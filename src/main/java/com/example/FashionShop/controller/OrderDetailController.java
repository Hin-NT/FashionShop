package com.example.FashionShop.controller;

import com.example.FashionShop.dto.OrderDetailDTO;
import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.model.OrderDetail;
import com.example.FashionShop.service.interfaces.IOrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-details")
public class OrderDetailController {

    private final IOrderDetail orderDetailService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<OrderDetailDTO>>> getAllOrderDetails() {
        return orderDetailService.getAll();
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<ResponseDTO<OrderDetailDTO>> getOrderDetailById(@PathVariable String orderDetailId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailId);
        return orderDetailService.findById(orderDetail);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<OrderDetail>> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderDetailService.create(orderDetail);
    }

    @PutMapping("/{orderDetailId}")
    public ResponseEntity<ResponseDTO<OrderDetail>> updateOrderDetail(
            @PathVariable String orderDetailId,
            @RequestBody OrderDetail orderDetail) {
        orderDetail.setOrderDetailId(orderDetailId);
        return orderDetailService.update(orderDetail);
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<ResponseDTO<String>> deleteOrderDetail(@PathVariable String orderDetailId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(orderDetailId);
        return orderDetailService.delete(orderDetail);
    }

}
