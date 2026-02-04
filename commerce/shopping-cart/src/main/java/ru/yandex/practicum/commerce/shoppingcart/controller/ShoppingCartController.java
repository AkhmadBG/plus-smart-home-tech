package ru.yandex.practicum.commerce.shoppingcart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.ShoppingCartOperations;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.util.Logging;
import ru.yandex.practicum.commerce.shoppingcart.service.ShoppingCartService;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartService shoppingCartService;

    @Logging
    @GetMapping
    public ResponseEntity<ShoppingCartDto> getShoppingCart(@RequestParam String username) {
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(username);
        return ResponseEntity.ok(shoppingCartDto);
    }

    @Logging
    @PutMapping
    public ResponseEntity<ShoppingCartDto> addProductInShoppingCart(@RequestParam String username,
                                                                    @RequestBody HashMap<String, Integer> products) {
        ShoppingCartDto shoppingCartDto = shoppingCartService.addProductInShoppingCart(username, products);
        return ResponseEntity.ok(shoppingCartDto);
    }

    @Logging
    @DeleteMapping
    public ResponseEntity<Void> deactivateShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateShoppingCart(username);
        return ResponseEntity.ok().build();
    }

    @Logging
    @PostMapping("/remove")
    public ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(@RequestParam String username,
                                                                          @RequestBody List<String> productIds) {
        ShoppingCartDto shoppingCartDto = shoppingCartService.removeProductsFromShoppingCart(username, productIds);
        return ResponseEntity.ok(shoppingCartDto);
    }

    @Logging
    @PostMapping("/change-quantity")
    public ResponseEntity<ShoppingCartDto> changeProductQuantityInShoppingCart(@RequestParam String username,
                                                                               @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest) {
        ShoppingCartDto shoppingCartDto = shoppingCartService.changeProductQuantityInShoppingCart(username, changeProductQuantityRequest);
        return ResponseEntity.ok(shoppingCartDto);
    }

    @Logging
    @GetMapping("/list")
    public ResponseEntity<List<String>> getShoppingCartIdList(@RequestParam String userName) {
        List<String> shoppingCartIdList = shoppingCartService.getShoppingCartIdList(userName);
        return ResponseEntity.ok(shoppingCartIdList);
    }

}