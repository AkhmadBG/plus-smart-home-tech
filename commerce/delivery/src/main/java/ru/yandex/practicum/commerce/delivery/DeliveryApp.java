package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.interactionapi"
})
@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.delivery",
        "ru.yandex.practicum.commerce.interactionapi"
})
public class DeliveryApp {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApp.class, args);
    }

}