package ru.yandex.practicum.commerce.interactionapi.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}