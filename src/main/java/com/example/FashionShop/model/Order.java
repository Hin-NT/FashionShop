package com.example.FashionShop.model;

import com.example.FashionShop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    String orderId;

    @Column(name = "total_price")
    double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    OrderStatus orderStatus;

    @Column(name = "payment_method")
    String paymentMethod;

    @Column(name = "shipping_address")
    String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order(String orderId) {
        this.orderId = orderId;
    }
}
