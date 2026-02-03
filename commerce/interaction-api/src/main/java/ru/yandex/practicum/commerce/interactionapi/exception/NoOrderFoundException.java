package ru.yandex.practicum.commerce.interactionapi.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}