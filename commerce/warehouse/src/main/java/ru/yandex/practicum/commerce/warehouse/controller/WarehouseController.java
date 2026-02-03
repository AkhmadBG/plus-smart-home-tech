package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.interactionapi.apiinterface.WarehouseOperations;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService warehouseService;

    @PutMapping
    public ResponseEntity<Void> newProductInWarehouse(@RequestBody NewProductInWarehouseRequest newProductInWarehouseRequest) {
        warehouseService.newProductInWarehouse(newProductInWarehouseRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<BookedProductsDto> checkProductQuantityInShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto) {
        BookedProductsDto bookedProductsDto = warehouseService.checkProductQuantityInShoppingCart(shoppingCartDto);
        return ResponseEntity.ok().body(bookedProductsDto);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProductToWarehouse(@RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest) {
        warehouseService.addProductToWarehouse(addProductToWarehouseRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/address")
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        AddressDto addressDto = warehouseService.getWarehouseAddress();
        return ResponseEntity.ok().body(addressDto);
    }

    @PostMapping("/shipped")
    public ResponseEntity<Void> shippedToDelivery(@RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest) {
        warehouseService.shippedToDelivery(shippedToDeliveryRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnProductsToWarehouse(@RequestBody ReturnProductsToWarehouseRequest returnProductsToWarehouseRequest) {
        warehouseService.returnProductsToWarehouse(returnProductsToWarehouseRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assembly")
    public ResponseEntity<BookedProductsDto> assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest) {
        BookedProductsDto deliveryInfoDto = warehouseService.assemblyProductsForOrder(assemblyProductsForOrderRequest);
        return ResponseEntity.ok().body(deliveryInfoDto);
    }

}