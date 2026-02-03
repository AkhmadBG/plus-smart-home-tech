package ru.yandex.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.order.dto.OrderDto;
import ru.yandex.practicum.commerce.order.entity.Order;
import ru.yandex.practicum.commerce.order.entity.ProductItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "productItems", target = "products")
    OrderDto mapToOrderDto(Order order);

    default Map<String, Integer> map(List<ProductItem> productItems) {
        if (productItems == null || productItems.isEmpty()) {
            return Map.of();
        }

        return productItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getProductId().toString(),
                        ProductItem::getQuantity
                ));
    }

}