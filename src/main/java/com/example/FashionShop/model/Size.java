package com.example.FashionShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "size")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Size extends BaseEntity {

    @Id
    @Column(name = "size_id")
    String sizeId;

    @NotBlank
    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "size")
    List<ProductColorSize> productColorSizeList;

}

