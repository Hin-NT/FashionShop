package com.example.FashionShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_color")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductColor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_color_id")
    String productColorId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    Color color;

    @OneToMany(mappedBy = "productColor", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "productColor", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductColorSize> productColorSizeList = new ArrayList<>();

    public ProductColor(String productColorId) {
        this.productColorId = productColorId;
    }
}
