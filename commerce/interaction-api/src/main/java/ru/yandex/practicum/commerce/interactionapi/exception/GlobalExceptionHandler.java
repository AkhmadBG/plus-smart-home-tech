package ru.yandex.practicum.commerce.interactionapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoDeliveryFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoDeliveryFoundException(final NoDeliveryFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Доставка не найдена",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoOrderFoundException(final NoOrderFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Заказ не найден",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInShoppingCartException(final NoProductsInShoppingCartException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Нет искомых товаров в корзине",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NoShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoShoppingCartException(final NoShoppingCartException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Корзина не найдена",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseException(final NoSpecifiedProductInWarehouseException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Товар не найден на складе",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedUserException(final NotAuthorizedUserException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED,
                        "Не известное имя пользователя",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughInfoInOrderToCalculateException(final NotEnoughInfoInOrderToCalculateException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Недостаточно информации в заказе для расчёта",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartLowQuantityInWarehouse(final ProductInShoppingCartLowQuantityInWarehouse e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT,
                        "Товар из корзины товаров на складе меньше",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ProductInShoppingCartNotInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleProductInShoppingCartNotInWarehouse(final ProductInShoppingCartNotInWarehouse e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Товар из корзины товаров отсутствует на складе",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(final ProductNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Товар не найден",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleSpecifiedProductAlreadyInWarehouseException(final SpecifiedProductAlreadyInWarehouseException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT,
                        "Товар не складе уже существует",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ProductInShoppingCartNotInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleOrderBookingNotFoundException(final OrderBookingNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Доставка не найдена",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ProductInShoppingCartNotInWarehouse.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(final PaymentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND,
                        "Доставка не найдена",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Произошла внутрення ошибка",
                        e.getMessage(),
                        LocalDateTime.now()));
    }

}
