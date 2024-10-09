package com.example.FashionShop.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING,    // The order has been created but not yet confirmed or paid.
    CONFIRMED,  // The order has been confirmed and payment is successful.
    CANCELLED,  // The order has been cancelled.
    SHIPPED,    // The order has been shipped.
    DELIVERED,  // The order has been delivered to the customer.
    RETURNED;   // The order has been returned by the customer.
}
