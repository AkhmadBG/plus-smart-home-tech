package ru.yandex.practicum.commerce.interactionapi.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.commerce.interactionapi.order.enums.OrderState;

import java.util.Map;

@Data
@Builder
public class OrderDto {

    @NotNull
    private String orderId;

    private String shoppingCartId;

    @NotNull
    private Map<String, Integer> products;

    private String paymentId;

    private String deliveryId;

    private OrderState state;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;

    private Double totalPrice;

    private Double deliveryPrice;

    private Double productPrice;

}