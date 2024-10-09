package com.example.FashionShop.model;

import com.example.FashionShop.enums.DiscountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "discounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "discount_id")
    String discountId;

    @NotBlank
    @Column(name = "discount_name")
    String discountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    DiscountStatus discountStatus;

    @NotNull
    @Column(name = "start_time")
    LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    LocalDateTime endTime;

    @OneToMany(mappedBy = "discount")
    List<ProductDiscount> productDiscountList;

}
