package com.example.FashionShop.dto;

import com.example.FashionShop.model.OrderDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO {

    String orderDetailId;
    int quantity;
    double price;
    OrderDTO order;
    ProductColorSizeDTO productColorSize;

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();

        if (orderDetail.getOrder() != null) {
            this.order = new OrderDTO(orderDetail.getOrder(), 0);
        } else {
            this.order = null;
        }

        this.productColorSize = new ProductColorSizeDTO(orderDetail.getProductColorSize(), 0);
    }
}