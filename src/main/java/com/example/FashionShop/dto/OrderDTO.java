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
    List<OrderDetailDTO> orderDetails;
    CustomerDTO customer;

    public OrderDTO(Order order, int choose) {
        if (order != null) {
            this.orderId = order.getOrderId();
            this.totalPrice = order.getTotalPrice();
            this.orderStatus = order.getOrderStatus();
            this.paymentMethod = order.getPaymentMethod();
            this.shippingAddress = order.getShippingAddress();

            if (choose == 1) {
                this.orderDetails = order.getOrderDetails().stream()
                        .map(orderDetail -> new OrderDetailDTO(orderDetail))
                        .collect(Collectors.toList());
            }

            if (order.getCustomer() != null) {
                this.customer = new CustomerDTO(order.getCustomer(), 0);
            } else {
                this.customer = null;
            }
        }
    }

}