package ru.yandex.practicum.commerce.interactionapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.PaymentOperations;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeignClient extends PaymentOperations {
}