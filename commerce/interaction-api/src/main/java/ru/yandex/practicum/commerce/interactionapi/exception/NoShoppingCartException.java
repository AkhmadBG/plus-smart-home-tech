package ru.yandex.practicum.commerce.interactionapi.exception;

public class NoShoppingCartException extends RuntimeException {
    public NoShoppingCartException(String message) {
        super(message);
    }
}