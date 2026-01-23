package ru.yandex.practicum.commerce.interactionapi.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.ShoppingStoreOperations;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreFeignClient extends ShoppingStoreOperations {
}