package ru.yandex.practicum.commerce.shoppingstore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.ShoppingStoreOperations;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.*;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class SoppingStoreController implements ShoppingStoreOperations {

    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getListProductDtoByCategory(@RequestParam ProductCategory category,
                                                                        Pageable pageable) {
        Page<ProductDto> listProductDtoByCategory = shoppingStoreService.
                getListProductDtoByCategory(category, pageable);
        return ResponseEntity.ok(listProductDtoByCategory);
    }

    @PutMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        ProductDto newProduct = shoppingStoreService.addProduct(productDto);
        return ResponseEntity.ok().body(newProduct);
    }

    @PostMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        ProductDto updateProduct = shoppingStoreService.updateProduct(productDto);
        return ResponseEntity.ok(updateProduct);
    }

    @PostMapping("/removeProductFromStore")
    public ResponseEntity<Boolean> removeProductFromStore(@RequestBody UUID productId) {
        Boolean result = shoppingStoreService.removeProductFromStore(productId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/quantityState")
    public ResponseEntity<Boolean> setProductQuantityState(@ModelAttribute SetProductQuantityStateRequest quantityState) {
        Boolean result = shoppingStoreService.setProductQuantityState(quantityState);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID productId) {
        ProductDto productDto = shoppingStoreService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

}