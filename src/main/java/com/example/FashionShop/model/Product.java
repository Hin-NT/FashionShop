package com.example.FashionShop.model;

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
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

    @Id
    @Column(name = "product_id")
    String productId;

    @NotBlank
    @Column(name = "product_name")
    String productName;

    @Column(name = "description")
    String description;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "style_id")
    Style style;

    @OneToMany(mappedBy = "product")
    List<ProductColor> productColorList;

    public Product(String productId) {
        this.productId = productId;
    }

    public Product(String productId, String productName, String description, Category category, Style style) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.style = style;
    }
}
