package ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SortDto {

    private String direction;

    private String property;

}