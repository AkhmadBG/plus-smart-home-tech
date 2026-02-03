package ru.yandex.practicum.commerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.commerce.interactionapi"
})
@SpringBootApplication
public class PaymentApp {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }

}