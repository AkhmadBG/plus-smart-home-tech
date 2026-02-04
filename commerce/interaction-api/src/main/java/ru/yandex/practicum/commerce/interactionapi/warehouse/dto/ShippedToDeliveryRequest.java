package ru.yandex.practicum.commerce.interactionapi.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippedToDeliveryRequest {

    @NotNull
    private String orderId;

    @NotNull
    private String deliveryId;

}