package com.example.FashionShop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_discount")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDiscount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_discount_id")
    String productDiscountId;

    @ManyToOne
    @JoinColumn(name = "product_color_size_id")
    ProductColorSize productColorSize;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount discount;

    @Column(name = "percent")
    int percent;

}
