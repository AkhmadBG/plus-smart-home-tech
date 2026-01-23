package ru.yandex.practicum.commerce.shoppingcart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shoppingcart.entity.CartItem;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        builder = @org.mapstruct.Builder(disableBuilder = true))
public interface ShoppingCartMapper {

    @Mapping(source = "cartId", target = "shoppingCartId")
    @Mapping(source = "cartItems", target = "products")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);

    default Map<String, Integer> map(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return Map.of();
        }

        return cartItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getProductId().toString(),
                        CartItem::getQuantity
                ));
    }

}