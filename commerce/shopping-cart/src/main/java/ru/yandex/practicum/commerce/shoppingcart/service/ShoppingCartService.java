package ru.yandex.practicum.commerce.shoppingcart.service;

import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String userName);

    ShoppingCartDto addProductInShoppingCart(String userName, Map<String, Integer> products);

    void deactivateShoppingCart(String userName);

    ShoppingCartDto removeProductsFromShoppingCart(String userName, List<String> productIds);

    ShoppingCartDto changeProductQuantityInShoppingCart(String userName, ChangeProductQuantityRequest changeProductQuantityRequest);

    List<String> getShoppingCartIdList(String userName);

}