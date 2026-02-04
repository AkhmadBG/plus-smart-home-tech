package ru.yandex.practicum.commerce.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.entity.Delivery;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.delivery.enums.DeliveryState;
import ru.yandex.practicum.commerce.interactionapi.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.interactionapi.feignclient.OrderFeignClient;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.yandex.practicum.commerce.interactionapi.util.AppConstant.BASE_DELIVERY_RATE;

@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderFeignClient orderFeignClient;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = Delivery.builder()
                .orderId(UUID.fromString(deliveryDto.getOrderId()))
                .fromAddress(deliveryMapper.mapToAddress(deliveryDto.getFromAddress()))
                .toAddress(deliveryMapper.mapToAddress(deliveryDto.getToAddress()))
                .deliveryState(DeliveryState.CREATED)
                .build();
        Delivery saveDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.mapToDeliveryDto(saveDelivery);
    }

    @Override
    public void successfulDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(UUID.fromString(orderId))
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка заказа с номером " + orderId + " не найдена"));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderFeignClient.completedOrder(orderId);
    }

    @Override
    public void pickedDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(UUID.fromString(orderId))
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка заказа с номером " + orderId + " не найдена"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderFeignClient.deliveryOrder(orderId);
    }

    @Override
    public void failedDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(UUID.fromString(orderId))
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка заказа с номером " + orderId + " не найдена"));
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderFeignClient.deliveryFailedOrder(orderId);
    }

    @Override
    public BigDecimal costDelivery(OrderDto orderDto) {
        BigDecimal costDelivery = BASE_DELIVERY_RATE;
        Delivery delivery = deliveryRepository.findByOrderId(UUID.fromString(orderDto.getOrderId()))
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка заказа с номером " + orderDto.getOrderId() + " не найдена"));

        if (delivery.getFromAddress().equals("ADDRESS_2")) {
            costDelivery = costDelivery.add(costDelivery.multiply(BigDecimal.valueOf(2)));
        }

        if (Boolean.TRUE.equals(orderDto.getFragile())) {
            costDelivery = costDelivery.add(costDelivery.multiply(BigDecimal.valueOf(0.2)));
        }

        costDelivery = costDelivery.add(BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(BigDecimal.valueOf(0.3)));
        costDelivery = costDelivery.add(BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(BigDecimal.valueOf(0.2)));
        return costDelivery;
    }

}