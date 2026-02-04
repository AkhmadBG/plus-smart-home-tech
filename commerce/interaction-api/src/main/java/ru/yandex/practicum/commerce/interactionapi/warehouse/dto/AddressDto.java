package ru.yandex.practicum.commerce.interactionapi.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String addressId;

    private String country;

    private String city;

    private String street;

    private String house;

    private String flat;

}