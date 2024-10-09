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
@Table(name = "colors")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Color extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "color_id")
    String colorId;

    @NotBlank
    @Column(name = "color_name")
    String colorName;

    @OneToMany(mappedBy = "color")
    List<ProductColor> productColorList;

    public Color(String colorId) {
        this.colorId = colorId;
    }
}
