package ru.yandex.practicum.commerce.interactionapi.exception;

public class OrderBookingNotFoundException extends RuntimeException {
    public OrderBookingNotFoundException(String message) {
        super(message);
    }
}