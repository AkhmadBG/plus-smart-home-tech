package ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.QuantityState;

import java.util.UUID;

@Data
@Builder
public class SetProductQuantityStateRequest {

    @NotBlank
    private String productId;

    @NotNull
    private QuantityState quantityState;

}