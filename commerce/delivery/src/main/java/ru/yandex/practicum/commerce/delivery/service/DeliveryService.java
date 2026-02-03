package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;

import java.math.BigDecimal;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(String orderId);

    void pickedDelivery(String orderId);

    void failedDelivery(String orderId);

    BigDecimal costDelivery(OrderDto orderDto);

}