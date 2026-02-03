package ru.yandex.practicum.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.delivery.entity.Address;
import ru.yandex.practicum.commerce.delivery.entity.Delivery;
import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AddressDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    Address mapToAddress(AddressDto fromAddress);

    DeliveryDto mapToDeliveryDto(Delivery delivery);

}