package com.example.FashionShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "styles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Style extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "style_id")
    String styleId;

    @NotBlank
    @Column(name = "style_name")
    String styleName;

    @OneToMany(mappedBy = "style")
    List<Product> products;

    public Style(String styleId) {

    }

    public Style(String styleId, String styleName) {
        this.styleId = styleId;
        this.styleName = styleName;
    }

}