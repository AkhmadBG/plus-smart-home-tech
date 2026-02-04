package ru.yandex.practicum.commerce.shoppingstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.interactionapi"
})
@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.shoppingstore",
        "ru.yandex.practicum.commerce.interactionapi"
})
public class ShoppingStoreApp {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingStoreApp.class, args);
    }

}