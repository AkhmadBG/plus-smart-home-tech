package ru.yandex.practicum.commerce.interactionapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.DeliveryOperations;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeignClient extends DeliveryOperations {
}