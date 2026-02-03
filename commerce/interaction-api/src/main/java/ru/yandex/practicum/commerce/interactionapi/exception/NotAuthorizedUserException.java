package ru.yandex.practicum.commerce.interactionapi.exception;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}