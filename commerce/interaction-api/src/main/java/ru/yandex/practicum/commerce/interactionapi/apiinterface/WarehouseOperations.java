package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.AddressDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.NewProductInWarehouseRequest;

public interface WarehouseOperations {

    @PutMapping
    ResponseEntity<Void> newProductInWarehouse(@RequestBody NewProductInWarehouseRequest newProductInWarehouseRequest);

    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkProductQuantityInShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    ResponseEntity<Void> addProductToWarehouse(@RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getWarehouseAddress();

}