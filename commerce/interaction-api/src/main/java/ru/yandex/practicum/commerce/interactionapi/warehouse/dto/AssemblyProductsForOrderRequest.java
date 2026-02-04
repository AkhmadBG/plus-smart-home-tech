package ru.yandex.practicum.commerce.interactionapi.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AssemblyProductsForOrderRequest {

    @NotNull
    private Map<String, Integer> products;

    @NotNull
    private String orderId;

}