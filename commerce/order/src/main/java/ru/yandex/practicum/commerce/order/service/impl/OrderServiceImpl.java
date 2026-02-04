package ru.yandex.practicum.commerce.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.delivery.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interactionapi.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interactionapi.feignclient.DeliveryFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.PaymentFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.ShoppingCartFeignClient;
import ru.yandex.practicum.commerce.interactionapi.feignclient.WarehouseFeignClient;
import ru.yandex.practicum.commerce.interactionapi.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.interactionapi.order.dto.ProductReturnRequest;
import ru.yandex.practicum.commerce.interactionapi.order.enums.OrderState;
import ru.yandex.practicum.commerce.interactionapi.payment.dto.PaymentDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.ReturnProductsToWarehouseRequest;
import ru.yandex.practicum.commerce.order.entity.Order;
import ru.yandex.practicum.commerce.order.entity.ProductItem;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryFeignClient deliveryFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final ShoppingCartFeignClient shoppingCartFeignClient;
    private final WarehouseFeignClient warehouseFeignClient;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDto> getOrdersByUserName(String userName, Pageable pageable) {
        List<String> shoppingCartIdList = shoppingCartFeignClient.getShoppingCartIdList(userName).getBody();
        List<UUID> shoppingCartUUIdList = shoppingCartIdList.stream()
                .map(UUID::fromString)
                .toList();
        Page<Order> ordersByUserName = orderRepository.findByShoppingCartIdIn(shoppingCartUUIdList, pageable);
        return ordersByUserName.map(orderMapper::mapToOrderDto);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest createNewOrderRequest) {
        ShoppingCartDto shoppingCartDto = createNewOrderRequest.getShoppingCart();
        Map<String, Integer> products = shoppingCartDto.getProducts();

        BookedProductsDto bookedProductsDto = getBookedProductsDto(products, shoppingCartDto.getShoppingCartId());

        AddressDto warehouseAddress = warehouseFeignClient.getWarehouseAddress().getBody();
        AddressDto deliveryAddress = createNewOrderRequest.getDeliveryAddress();
        DeliveryDto delivery = deliveryFeignClient.createDelivery(
                DeliveryDto.builder()
                        .fromAddress(warehouseAddress)
                        .toAddress(deliveryAddress)
                        .orderId(shoppingCartDto.getShoppingCartId())
                        .build()
        ).getBody();

        Order newOrder = Order.builder()
                .shoppingCartId(UUID.fromString(shoppingCartDto.getShoppingCartId()))
                .productItems(getProductItems(products))
                .deliveryId(UUID.fromString(delivery.getDeliveryId()))
                .state(OrderState.NEW)
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.getFragile())
                .build();

        OrderDto orderDto = orderMapper.mapToOrderDto(newOrder);

        PaymentDto payment = paymentFeignClient.createPayment(orderDto).getBody();
        BigDecimal productPrice = paymentFeignClient.productCostPayment(orderDto).getBody();

        newOrder.setPaymentId(UUID.fromString(payment.getPaymentId()));
        newOrder.setTotalPrice(payment.getTotalPayment());
        newOrder.setDeliveryPrice(payment.getDeliveryTotal());
        newOrder.setProductPrice(productPrice);

        Order saveOrder = orderRepository.save(newOrder);

        return orderMapper.mapToOrderDto(saveOrder);
    }

    private BookedProductsDto getBookedProductsDto(Map<String, Integer> products, String orderId) {
        AssemblyProductsForOrderRequest assemblyProductsForOrderRequest = AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(products)
                .build();
        return warehouseFeignClient.assemblyProductsForOrder(assemblyProductsForOrderRequest).getBody();
    }

    private List<ProductItem> getProductItems(Map<String, Integer> products) {
        List<ProductItem> productItems = new ArrayList<>();
        products.forEach((productId, productQuantity) -> {
            ProductItem productItem = ProductItem.builder()
                    .productId(UUID.fromString(productId))
                    .quantity(productQuantity)
                    .build();
            productItems.add(productItem);
        });
        return productItems;
    }

    @Transactional
    @Override
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        Order order = orderRepository.findById(UUID.fromString(productReturnRequest.getOrderId()))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + productReturnRequest.getOrderId() + " не найден"));

        List<ProductItem> productItems = order.getProductItems();

        Map<String, Integer> returnedProducts = new HashMap<>();

        productItems.forEach(productItem -> {
            if (productReturnRequest.getProducts().containsKey(productItem.getProductId().toString())) {
                order.getProductItems().remove(productItem);

                returnedProducts.put(productItem.getProductId().toString(), productItem.getQuantity());
            }
        });

        ReturnProductsToWarehouseRequest returnProductsToWarehouseRequest = ReturnProductsToWarehouseRequest.builder()
                .returnedProducts(returnedProducts)
                .build();

        warehouseFeignClient.returnProductsToWarehouse(returnProductsToWarehouseRequest);

        order.setState(OrderState.PRODUCT_RETURNED);

        Order saveOrder = orderRepository.save(order);

        paymentFeignClient.refundPayment(productReturnRequest.getOrderId());

        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Transactional
    @Override
    public OrderDto paymentOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));
        paymentFeignClient.createPayment(orderMapper.mapToOrderDto(order));

        order.setState(OrderState.PAID);

        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto paymentFailedOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));
        paymentFeignClient.createPayment(orderMapper.mapToOrderDto(order));

        order.setState(OrderState.PAYMENT_FAILED);

        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto deliveryOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        order.setState(OrderState.ON_DELIVERY);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto deliveryFailedOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        order.setState(OrderState.DELIVERY_FAILED);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto completedOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        deliveryFeignClient.successfulDelivery(orderId);

        order.setState(OrderState.COMPLETED);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto calculateTotal(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        BigDecimal totalCost = paymentFeignClient.totalCostPayment(orderMapper.mapToOrderDto(order)).getBody();

        order.setTotalPrice(totalCost);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto calculateDelivery(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        BigDecimal deliveryCost = deliveryFeignClient.costDelivery(orderMapper.mapToOrderDto(order)).getBody();

        order.setDeliveryPrice(deliveryCost);
        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto assemblyOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        Map<String, Integer> products = new HashMap<>();
        order.getProductItems().forEach(
                item -> {
                    products.put(item.getProductId().toString(), item.getQuantity());
                }
        );

        BookedProductsDto bookedProductsDto = getBookedProductsDto(products, order.getOrderId().toString());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setFragile(bookedProductsDto.getFragile());

        order.setState(OrderState.ASSEMBLED);

        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

    @Override
    public OrderDto assemblyFailedOrder(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new NoOrderFoundException("Заказ с Id = " + orderId + " не найден"));

        order.setState(OrderState.ASSEMBLY_FAILED);

        Order saveOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(saveOrder);
    }

}