package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.*;

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    BookedProductsDto checkProductQuantityInShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest);

    AddressDto getWarehouseAddress();

    void shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest);

    void returnProductsToWarehouse(ReturnProductsToWarehouseRequest returnProductsToWarehouseRequest);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);

}