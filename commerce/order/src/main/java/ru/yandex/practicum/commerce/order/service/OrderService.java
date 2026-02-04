package ru.yandex.practicum.commerce.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.interactionapi.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.ProductReturnRequest;

public interface OrderService {

    Page<OrderDto> getOrdersByUserName(String userName, Pageable pageable);

    OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto paymentOrder(String orderId);

    OrderDto paymentFailedOrder(String orderId);

    OrderDto deliveryOrder(String orderId);

    OrderDto deliveryFailedOrder(String orderId);

    OrderDto completedOrder(String orderId);

    OrderDto calculateTotal(String orderId);

    OrderDto calculateDelivery(String orderId);

    OrderDto assemblyOrder(String orderId);

    OrderDto assemblyFailedOrder(String orderId);

}