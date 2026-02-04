package ru.yandex.practicum.commerce.interactionapi.delivery.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.commerce.interactionapi.delivery.enums.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AddressDto;

@Data
@Builder
public class DeliveryDto {

    private String deliveryId;

    private String orderId;

    private AddressDto fromAddress;

    private AddressDto toAddress;

    private DeliveryState deliveryState;

}