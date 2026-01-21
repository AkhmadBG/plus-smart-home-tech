package ru.yandex.practicum.commerce.interactionapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.ShoppingCartOperations;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartFeignClient extends ShoppingCartOperations {
}