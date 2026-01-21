package ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ShoppingCartDto {

    private String shoppingCartId;

    @NotNull
    private Map<String, Integer> products;

}