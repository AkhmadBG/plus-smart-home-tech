package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.OrderOperations;
import ru.yandex.practicum.commerce.interactionapi.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController implements OrderOperations {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrdersByUserName(@RequestParam String userName,
                                                              Pageable pageable) {
        Page<OrderDto> userOrders = orderService.getOrdersByUserName(userName, pageable);
        return ResponseEntity.ok(userOrders);
    }

    @PutMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateNewOrderRequest createNewOrderRequest) {
        OrderDto orderDto = orderService.createOrder(createNewOrderRequest);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/return")
    public ResponseEntity<OrderDto> returnOrder(@RequestBody ProductReturnRequest productReturnRequest) {
        OrderDto orderDto = orderService.returnOrder(productReturnRequest);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDto> paymentOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.paymentOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/payment/failed")
    public ResponseEntity<OrderDto> paymentFailedOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.paymentFailedOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/delivery")
    public ResponseEntity<OrderDto> deliveryOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.deliveryOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/delivery/failed")
    public ResponseEntity<OrderDto> deliveryFailedOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.deliveryFailedOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/completed")
    public ResponseEntity<OrderDto> completedOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.completedOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/calculate/total")
    public ResponseEntity<OrderDto> calculateTotal(@RequestBody String orderId) {
        OrderDto orderDto = orderService.calculateTotal(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/calculate/delivery")
    public ResponseEntity<OrderDto> calculateDelivery(@RequestBody String orderId) {
        OrderDto orderDto = orderService.calculateDelivery(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/assembly")
    public ResponseEntity<OrderDto> assemblyOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.assemblyOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/assembly/failed")
    public ResponseEntity<OrderDto> assemblyFailedOrder(@RequestBody String orderId) {
        OrderDto orderDto = orderService.assemblyFailedOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

}