package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;

import java.util.HashMap;
import java.util.List;

public interface ShoppingCartOperations {

    @GetMapping
    ResponseEntity<ShoppingCartDto> getShoppingCart(@RequestParam String userName);

    @PutMapping
    ResponseEntity<ShoppingCartDto> addProductInShoppingCart(@RequestParam String userName,
                                                             @RequestBody HashMap<String, Integer> products);

    @DeleteMapping
    ResponseEntity<Void> deactivateShoppingCart(@RequestParam String userName);

    @PostMapping("/remove")
    ResponseEntity<ShoppingCartDto> removeProductsFromShoppingCart(@RequestParam String userName,
                                                                   @RequestBody List<String> productIds);

    @PostMapping("/change-quantity")
    ResponseEntity<ShoppingCartDto> changeProductQuantityInShoppingCart(@RequestParam String userName,
                                                                        @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest);

    @GetMapping("/list")
    ResponseEntity<List<String>> getShoppingCartIdList(@RequestParam String userName);

}