package ru.yandex.practicum.commerce.shoppingcart.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interactionapi.feignclient.WarehouseFeignClient;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.enums.ShoppingCartState;
import ru.yandex.practicum.commerce.shoppingcart.entity.CartItem;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShoppingCart;
import ru.yandex.practicum.commerce.shoppingcart.exception.NoShoppingCartException;
import ru.yandex.practicum.commerce.shoppingcart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shoppingcart.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.shoppingcart.service.ShoppingCartService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseFeignClient warehouseFeignClient;

    @Transactional
    @Override
    public ShoppingCartDto getShoppingCart(String userName) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findShoppingCartByUserName(userName);
        if (shoppingCart.isEmpty()) {
            ShoppingCart shoppingCart1 = ShoppingCart.builder()
                    .cartState(ShoppingCartState.ACTIVE)
                    .userName(userName)
                    .cartItems(new ArrayList<>())
                    .build();

            ShoppingCart saveShoppingCart = shoppingCartRepository.save(shoppingCart1);
            return shoppingCartMapper.toShoppingCartDto(saveShoppingCart);
        }

        return shoppingCartMapper.toShoppingCartDto(shoppingCart.get());
    }

    @Transactional
    @Override
    public ShoppingCartDto addProductInShoppingCart(String userName, Map<String, Integer> products) {

        ShoppingCart cart = shoppingCartRepository
                .findShoppingCartByUserName(userName)
                .orElseGet(() -> ShoppingCart.builder()
                        .cartState(ShoppingCartState.ACTIVE)
                        .userName(userName)
                        .cartItems(new ArrayList<>())
                        .build());

        setProductsInShoppingCart(products, cart);

        ShoppingCart saved = shoppingCartRepository.save(cart);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(saved);

        warehouseFeignClient.checkProductQuantityInShoppingCart(shoppingCartDto);

        return shoppingCartDto;
    }

    private static void setProductsInShoppingCart(
            Map<String, Integer> products,
            ShoppingCart shoppingCart
    ) {
        products.forEach((k, v) -> {
            CartItem cartItem = CartItem.builder()
                    .productId(UUID.fromString(k))
                    .quantity(v)
                    .shoppingCart(shoppingCart)
                    .build();

            shoppingCart.getCartItems().add(cartItem);
        });
    }

    @Override
    public void deactivateShoppingCart(String userName) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserName(userName)
                .orElseThrow(() -> new NoShoppingCartException("ShoppingCart not found by userName: " + userName));
        shoppingCart.setCartState(ShoppingCartState.DEACTIVATE);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(shoppingCartRepository.save(shoppingCart));
        warehouseFeignClient.checkProductQuantityInShoppingCart(shoppingCartDto);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProductsFromShoppingCart(String userName, List<String> productIds) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserName(userName)
                .orElseThrow(() -> new NoShoppingCartException("ShoppingCart not found by userName: " + userName));

        Set<UUID> idsToRemove = productIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        shoppingCart.getCartItems().removeIf(
                item -> idsToRemove.contains(item.getProductId())
        );
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(shoppingCartRepository.save(shoppingCart));
        warehouseFeignClient.checkProductQuantityInShoppingCart(shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto changeProductQuantityInShoppingCart(String userName, ChangeProductQuantityRequest changes) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserName(userName)
                .orElseThrow(() -> new NoShoppingCartException("ShoppingCart not found by userName: " + userName));
        shoppingCart.getCartItems().forEach(cartItem -> {
            if (cartItem.getProductId() == UUID.fromString(changes.getProductId())) {
                cartItem.setQuantity(changes.getNewQuantity());
            }
        });
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(shoppingCartRepository.save(shoppingCart));
        warehouseFeignClient.checkProductQuantityInShoppingCart(shoppingCartDto);
        return shoppingCartDto;
    }

}