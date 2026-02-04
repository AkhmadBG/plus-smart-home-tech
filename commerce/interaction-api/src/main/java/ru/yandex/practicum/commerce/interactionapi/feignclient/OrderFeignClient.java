package ru.yandex.practicum.commerce.interactionapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.OrderOperations;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeignClient extends OrderOperations {
}