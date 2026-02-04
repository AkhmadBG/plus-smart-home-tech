package ru.yandex.practicum.commerce.interactionapi.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProductReturnRequest {

    private String orderId;

    @NotNull
    private Map<String, Integer> products;

}