package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.ProductReturnRequest;

public interface OrderOperations {

    @GetMapping
    ResponseEntity<Page<OrderDto>> getOrdersByUserName(@RequestParam String userName, Pageable pageable);

    @PutMapping
    ResponseEntity<OrderDto> createOrder(@RequestBody CreateNewOrderRequest createNewOrderRequest);

    @PostMapping("/return")
    ResponseEntity<OrderDto> returnOrder(@RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping("/payment")
    ResponseEntity<OrderDto> paymentOrder(@RequestBody String orderId);

    @PostMapping("/payment/failed")
    ResponseEntity<OrderDto> paymentFailedOrder(@RequestBody String orderId);

    @PostMapping("/delivery")
    ResponseEntity<OrderDto> deliveryOrder(@RequestBody String orderId);

    @PostMapping("/delivery/failed")
    ResponseEntity<OrderDto> deliveryFailedOrder(@RequestBody String orderId);

    @PostMapping("/completed")
    ResponseEntity<OrderDto> completedOrder(@RequestBody String orderId);

    @PostMapping("/calculate/total")
    ResponseEntity<OrderDto> calculateTotal(@RequestBody String orderId);

    @PostMapping("/calculate/delivery")
    ResponseEntity<OrderDto> calculateDelivery(@RequestBody String orderId);

    @PostMapping("/assembly")
    ResponseEntity<OrderDto> assemblyOrder(@RequestBody String orderId);

    @PostMapping("/assembly/failed")
    ResponseEntity<OrderDto> assemblyFailedOrder(@RequestBody String orderId);

}