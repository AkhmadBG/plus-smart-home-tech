package ru.yandex.practicum.commerce.interactionapi.warehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewProductInWarehouseRequest {

    @NotBlank
    private String productId;

    private Boolean fragile;

    @NotNull
    private DimensionDto dimensionDto;

    @NotNull
    @DecimalMin("1.0")
    private Double weight;

}