package ru.yandex.practicum.commerce.interactionapi.apiinterface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;

import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping
    ResponseEntity<Page<ProductDto>> getListProductDtoByCategory(@RequestParam ProductCategory productCategory,
                                                                 Pageable pageable);

    @PutMapping
    ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    ResponseEntity<Boolean> removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    ResponseEntity<Boolean> setProductQuantityState(@ModelAttribute SetProductQuantityStateRequest quantityState);

    @GetMapping("/{productId}")
    ResponseEntity<ProductDto> getProductById(@PathVariable UUID productId);

}