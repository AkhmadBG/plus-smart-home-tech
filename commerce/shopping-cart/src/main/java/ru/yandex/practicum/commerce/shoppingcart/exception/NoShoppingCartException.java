package ru.yandex.practicum.commerce.shoppingcart.exception;

public class NoShoppingCartException extends RuntimeException {
    public NoShoppingCartException(String message) {
        super(message);
    }
}