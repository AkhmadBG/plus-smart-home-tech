package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.payment.dto.PaymentDto;

import java.math.BigDecimal;

public interface PaymentOperations {

    @PostMapping
    ResponseEntity<PaymentDto> createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    ResponseEntity<BigDecimal> totalCostPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    ResponseEntity<Void> refundPayment(@RequestBody String paymentId);

    @PostMapping("/productCost")
    ResponseEntity<BigDecimal> productCostPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    ResponseEntity<Void> failedPayment(@RequestBody String paymentId);

}