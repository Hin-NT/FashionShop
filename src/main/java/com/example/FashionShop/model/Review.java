package com.example.FashionShop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"customerId"})
@Builder
@Entity
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id")
    String reviewId;

    @NotNull
    @Column(name = "rating")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    int rating;

    @Column(name = "comment")
    String comment;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_color_size_id")
    ProductColorSize productColorSize;

    @NotBlank
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
}
