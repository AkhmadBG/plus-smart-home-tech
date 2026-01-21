package ru.yandex.practicum.commerce.shoppingcart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "cart")
public class CartItem {

    @Id
    @GeneratedValue
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @NotNull
    private UUID productId;

    @NotNull
    @Min(0)
    private Integer quantity;

}