package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;

import java.math.BigDecimal;

public interface DeliveryOperations {

    @PutMapping
    ResponseEntity<DeliveryDto> createDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    ResponseEntity<Void> successfulDelivery(@RequestBody String orderId);

    @PostMapping("/picked")
    ResponseEntity<Void> pickedDelivery(@RequestBody String orderId);

    @PostMapping("/failed")
    ResponseEntity<Void> failedDelivery(@RequestBody String orderId);

    @PostMapping("/cost")
    ResponseEntity<BigDecimal> costDelivery(@RequestBody OrderDto orderDto);

}