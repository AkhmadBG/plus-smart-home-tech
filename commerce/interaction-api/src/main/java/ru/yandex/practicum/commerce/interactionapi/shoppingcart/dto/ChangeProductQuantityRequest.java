package ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeProductQuantityRequest {

    @NotBlank
    private String productId;

    @NotNull
    private Integer newQuantity;

}