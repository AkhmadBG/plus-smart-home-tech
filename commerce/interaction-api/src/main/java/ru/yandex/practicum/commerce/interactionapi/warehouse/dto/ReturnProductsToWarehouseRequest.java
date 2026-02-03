package ru.yandex.practicum.commerce.interactionapi.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ReturnProductsToWarehouseRequest {

    @NotNull
    private Map<String, Integer> returnedProducts;

}