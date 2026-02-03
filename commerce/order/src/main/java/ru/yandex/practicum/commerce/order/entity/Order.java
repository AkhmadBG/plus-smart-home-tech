package ru.yandex.practicum.commerce.order.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interactionapi.order.enums.OrderState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders", schema = "order")
public class Order {

    @Id
    @GeneratedValue
    private UUID orderId;

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ProductItem> productItems = new ArrayList<>();

    private UUID paymentId;

    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState state;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;

    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;

}