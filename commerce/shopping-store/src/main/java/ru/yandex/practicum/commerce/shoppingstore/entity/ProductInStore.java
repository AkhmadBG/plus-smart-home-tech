package ru.yandex.practicum.commerce.shoppingstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductState;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", schema = "store")
public class ProductInStore {

    @Id
    @GeneratedValue
    private UUID productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String description;

    private String imageSrc;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @NotNull
    @DecimalMin("1.00")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

}