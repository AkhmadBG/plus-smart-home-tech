package ru.yandex.practicum.commerce.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", schema = "warehouse")
public class ProductInWarehouse {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private UUID productId;

    private Boolean fragile;

    @DecimalMin("1.0")
    private Double width;

    @DecimalMin("1.0")
    private Double height;

    @DecimalMin("1.0")
    private Double depth;

    private Double weight;

    private Integer quantity;

}