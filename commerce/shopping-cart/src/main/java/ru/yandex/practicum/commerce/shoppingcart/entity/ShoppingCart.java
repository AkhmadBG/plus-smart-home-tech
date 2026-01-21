package ru.yandex.practicum.commerce.shoppingcart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.enums.ShoppingCartState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts", schema = "cart")
public class ShoppingCart {

    @Id
    @GeneratedValue
    private UUID cartId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShoppingCartState cartState;

    @NotNull
    private String userName;

    @OneToMany(
            mappedBy = "shoppingCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

}