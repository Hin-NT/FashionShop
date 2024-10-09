package com.example.FashionShop.dto;

import com.example.FashionShop.enums.OrderStatus;
import com.example.FashionShop.model.Order;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {

    String orderId;
    double totalPrice;
    OrderStatus orderStatus;
    String paymentMethod;
    String shippingAddress;
    List<String> orderDetailIds;
    String customerId;

    public OrderDTO(Order order, int choose) {
        if (order != null) {
            this.orderId = order.getOrderId();
            this.totalPrice = order.getTotalPrice();
            this.orderStatus = order.getOrderStatus();
            this.paymentMethod = order.getPaymentMethod();
            this.shippingAddress = order.getShippingAddress();

            if (choose == 1) {
                this.orderDetailIds = order.getOrderDetails().stream()
                        .map(orderDetail -> orderDetail.getOrderDetailId())
                        .collect(Collectors.toList());
            }

            if (order.getCustomer() != null) {
                this.customerId = order.getCustomer().getCustomerId();
            } else {
                this.customerId = null;
            }
        }
    }
}