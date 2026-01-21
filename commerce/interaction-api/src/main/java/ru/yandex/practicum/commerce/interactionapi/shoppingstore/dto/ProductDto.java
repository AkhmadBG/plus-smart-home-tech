package ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductState;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductDto {

    private String productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String description;

    private String imageSrc;

    @NotNull
    private QuantityState quantityState;

    @NotNull
    private ProductState productState;

    private ProductCategory productCategory;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal price;

}