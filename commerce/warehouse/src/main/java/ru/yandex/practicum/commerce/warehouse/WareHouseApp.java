package ru.yandex.practicum.commerce.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.interactionapi"
})
@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.warehouse",
        "ru.yandex.practicum.commerce.interactionapi"
})
public class WareHouseApp {

    public static void main(String[] args) {
        SpringApplication.run(WareHouseApp.class, args);
    }

}