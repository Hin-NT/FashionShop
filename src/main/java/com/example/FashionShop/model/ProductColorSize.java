package com.example.FashionShop.model;

import com.example.FashionShop.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_color_size")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColorSize extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_color_size_id")
    String productColorSizeId;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_color_id")
    ProductColor productColor;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "size_id")
    Size size;



    @Column(name = "price")
    double price;

    @Column(name = "quantity")
    int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ProductStatus productStatus;

    @Column(name = "num_view")
    int numView;

    @OneToMany(mappedBy = "productColorSize", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;

    public ProductColorSize(String productColorSizeId, int quantity) {
        this.productColorSizeId = productColorSizeId;
        this.quantity = quantity;
    }

    public ProductColorSize(String productColorSizeId, ProductColor productColor, Size size, double price, int quantity, ProductStatus productStatus, int numView) {
        this.productColorSizeId = productColorSizeId;
        this.productColor = productColor;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
        this.numView = numView;
    }
}
