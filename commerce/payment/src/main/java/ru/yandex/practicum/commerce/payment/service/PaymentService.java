package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.payment.dto.PaymentDto;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    BigDecimal totalCostPayment(OrderDto orderDto);

    void refundPayment(String paymentId);

    BigDecimal productCostPayment(OrderDto orderDto);

    void failedPayment(String paymentId);

}