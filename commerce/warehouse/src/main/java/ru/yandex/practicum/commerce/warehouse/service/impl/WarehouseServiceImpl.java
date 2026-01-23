package ru.yandex.practicum.commerce.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.shoppingcart.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.*;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;
import ru.yandex.practicum.commerce.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.warehouse.repository.ProductInWarehouseRepository;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductInWarehouseRepository productInWarehouseRepository;
    private final WarehouseMapper warehouseMapper;

    private static final String[] ADDRESSES =
            new String[]{"country1, city1, street1, house1, flat1",
                    "country2, city2, street2, house2, flat2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        ProductInWarehouse productInWarehouse = warehouseMapper.toProductInWarehouse(newProductInWarehouseRequest);
        productInWarehouseRepository.save(productInWarehouse);
    }

    @Override
    public BookedProductsDto checkProductQuantityInShoppingCart(ShoppingCartDto shoppingCartDto) {
        Set<UUID> productIds = shoppingCartDto.getProducts().keySet().stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        List<ProductInWarehouse> productsFromShoppingCart = productInWarehouseRepository.findAllById(productIds);

        Map<String, Integer> productsInShoppingCart = shoppingCartDto.getProducts();

        BookedProductsDto bookedProductsDto = BookedProductsDto.builder()
                .deliveryWeight(0.0)
                .deliveryVolume(0.0)
                .fragile(false)
                .build();

        productsFromShoppingCart.forEach(productInWarehouse -> {
            Integer i = productsInShoppingCart.get(productInWarehouse.getProductId());
            if (i > productInWarehouse.getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("В корзине продукта с id=" +
                        productInWarehouse.getProductId() + " больше чем на складе");
            }
            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() + productInWarehouse.getWeight());
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    (productInWarehouse.getHeight() * productInWarehouse.getDepth() * productInWarehouse.getWidth()));
            if (productInWarehouse.getFragile() == true) {
                bookedProductsDto.setFragile(true);
            }
        });

        return bookedProductsDto;
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        ProductInWarehouse productInWarehouse = productInWarehouseRepository
                .findByProductId(UUID.fromString(addProductToWarehouseRequest.getProductId()))
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Товар с id = " + addProductToWarehouseRequest.getProductId() + " не найден на складе"));

        productInWarehouse.setQuantity(productInWarehouse.getQuantity() + addProductToWarehouseRequest.getQuantity());
        productInWarehouseRepository.save(productInWarehouse);
    }


    @Override
    public AddressDto getWarehouseAddress() {
        String[] address = CURRENT_ADDRESS.split(",");
        return new AddressDto(address[0], address[1], address[2], address[3], address[4]);
    }

}