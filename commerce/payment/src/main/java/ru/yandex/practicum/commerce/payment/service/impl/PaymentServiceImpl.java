package ru.yandex.practicum.commerce.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.exception.PaymentNotFoundException;
import ru.yandex.practicum.commerce.interactionapi.feignclient.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.OrderFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.ShoppingCartFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.ShoppingStoreFeignClient;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.payment.enums.PaymentState;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.payment.entity.Payment;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static ru.yandex.practicum.commerce.interactionapi.util.AppConstant.TAX_RATE;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ShoppingCartFeignClient shoppingCartFeignClient;
    private final ShoppingStoreFeignClient shoppingStoreFeignClient;
    private final DeliveryFeignClient deliveryFeignClient;
    private final OrderFeignClient orderFeignClient;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        ShoppingCartDto shoppingCartDto = shoppingCartFeignClient.getShoppingCart(orderDto.getShoppingCartId()).getBody();
        Map<String, Integer> products = shoppingCartDto.getProducts();

        BigDecimal totalPayment = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            ProductDto productDto = shoppingStoreFeignClient.getProductById(UUID.fromString(entry.getKey())).getBody();

            BigDecimal price = productDto.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            totalPayment = totalPayment.add(price.multiply(quantity));
        }

        BigDecimal deliveryTotal = deliveryFeignClient.costDelivery(orderDto).getBody();

        BigDecimal feeTotal = totalPayment.multiply(TAX_RATE)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        Payment payment = Payment.builder()
                .orderId(UUID.fromString(orderDto.getOrderId()))
                .totalPayment(totalPayment)
                .deliveryTotal(deliveryTotal)
                .feeTotal(feeTotal)
                .paymentState(PaymentState.PENDING)
                .build();
        Payment savePayment = paymentRepository.save(payment);
        return paymentMapper.mapToPaymentDto(savePayment);
    }

    @Override
    public BigDecimal totalCostPayment(OrderDto orderDto) {
        ShoppingCartDto shoppingCartDto = shoppingCartFeignClient.getShoppingCart(orderDto.getShoppingCartId()).getBody();
        Map<String, Integer> products = shoppingCartDto.getProducts();
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            ProductDto productDto = shoppingStoreFeignClient.getProductById(UUID.fromString(entry.getKey())).getBody();
            BigDecimal price = productDto.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            totalPayment = totalPayment.add(price.multiply(quantity));
        }
        BigDecimal costDelivery = deliveryFeignClient.costDelivery(orderDto).getBody();
        BigDecimal feeTotal = totalPayment.multiply(TAX_RATE);
        return totalPayment.add(costDelivery.add(feeTotal));
    }

    @Override
    public void refundPayment(String paymentId) {
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderFeignClient.paymentOrder(payment.getOrderId().toString());
    }

    @Override
    public BigDecimal productCostPayment(OrderDto orderDto) {
        ShoppingCartDto shoppingCartDto = shoppingCartFeignClient.getShoppingCart(orderDto.getShoppingCartId()).getBody();
        Map<String, Integer> products = shoppingCartDto.getProducts();
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            ProductDto productDto = shoppingStoreFeignClient.getProductById(UUID.fromString(entry.getKey())).getBody();
            BigDecimal price = productDto.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            totalPayment = totalPayment.add(price.multiply(quantity));
        }
        return totalPayment;
    }

    @Override
    public void failedPayment(String paymentId) {
        Payment payment = paymentRepository.findById(UUID.fromString(paymentId))
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + paymentId));
        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderFeignClient.paymentFailedOrder(payment.getOrderId().toString());
    }

}