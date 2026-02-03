package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.PaymentOperations;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentOperations {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody OrderDto orderDto) {
        PaymentDto paymentDto = paymentService.createPayment(orderDto);
        return ResponseEntity.ok().body(paymentDto);
    }

    @PostMapping("/totalCost")
    public ResponseEntity<BigDecimal> totalCostPayment(@RequestBody OrderDto orderDto) {
        BigDecimal totalCost = paymentService.totalCostPayment(orderDto);
        return ResponseEntity.ok().body(totalCost);
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> refundPayment(@RequestBody String paymentId) {
        paymentService.refundPayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/productCost")
    public ResponseEntity<BigDecimal> productCostPayment(@RequestBody OrderDto orderDto) {
        BigDecimal totalCost = paymentService.productCostPayment(orderDto);
        return ResponseEntity.ok().body(totalCost);
    }

    @PostMapping("/failed")
    public ResponseEntity<Void> failedPayment(@RequestBody String paymentId) {
        paymentService.failedPayment(paymentId);
        return ResponseEntity.ok().build();
    }

}