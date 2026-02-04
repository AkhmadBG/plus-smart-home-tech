package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.DeliveryOperations;
import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.util.Logging;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryOperations {

    public final DeliveryService deliveryService;

    @Logging
    @PutMapping
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody DeliveryDto deliveryDto) {
        DeliveryDto newDeliveryDto = deliveryService.createDelivery(deliveryDto);
        return ResponseEntity.ok(newDeliveryDto);
    }

    @Logging
    @PostMapping("/successful")
    public ResponseEntity<Void> successfulDelivery(@RequestBody String orderId) {
        deliveryService.successfulDelivery(orderId);
        return ResponseEntity.ok().build();
    }

    @Logging
    @PostMapping("/picked")
    public ResponseEntity<Void> pickedDelivery(@RequestBody String orderId) {
        deliveryService.pickedDelivery(orderId);
        return ResponseEntity.ok().build();
    }

    @Logging
    @PostMapping("/failed")
    public ResponseEntity<Void> failedDelivery(@RequestBody String orderId) {
        deliveryService.failedDelivery(orderId);
        return ResponseEntity.ok().build();
    }

    @Logging
    @PostMapping("/cost")
    public ResponseEntity<BigDecimal> costDelivery(@RequestBody OrderDto orderDto) {
        BigDecimal costDelivery = deliveryService.costDelivery(orderDto);
        return ResponseEntity.ok(costDelivery);
    }

}