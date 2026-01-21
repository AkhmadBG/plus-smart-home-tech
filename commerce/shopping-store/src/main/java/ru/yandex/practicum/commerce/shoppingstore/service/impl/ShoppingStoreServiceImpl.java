package ru.yandex.practicum.commerce.shoppingstore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.entity.ProductInStore;
import ru.yandex.practicum.commerce.shoppingstore.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.repository.ShoppingStoreRepository;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDto> getListProductDtoByCategory(ProductCategory productCategory, Pageable pageable) {
        Page<ProductInStore> products = shoppingStoreRepository.findByProductCategory(productCategory, pageable);
        return products.map(productMapper::toProductDto);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        ProductInStore productInStore = shoppingStoreRepository.save(productMapper.toProduct(productDto));
        return productMapper.toProductDto(productInStore);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        ProductInStore productInStore = shoppingStoreRepository.findById(UUID.fromString(productDto.getProductId())).
                orElseThrow(() -> new ProductNotFoundException("Не найден товар с id = " + productDto.getProductId()));
        productMapper.updateProduct(productDto, productInStore);
        ProductInStore updateProductInStore = shoppingStoreRepository.save(productInStore);
        return productMapper.toProductDto(updateProductInStore);
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) {
        ProductInStore productInStore = shoppingStoreRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException("Не найден товар с id = " + productId));
        productInStore.setProductState(ProductState.DEACTIVATE);
        shoppingStoreRepository.save(productInStore);
        return true;
    }

    @Override
    public Boolean setProductQuantityState(SetProductQuantityStateRequest quantityStateRequest) {
        ProductInStore productInStore = shoppingStoreRepository.findById(UUID.fromString(quantityStateRequest.getProductId())).
                orElseThrow(() -> new ProductNotFoundException("Не найден товар с id = " + quantityStateRequest.getProductId()));
        productInStore.setQuantityState(quantityStateRequest.getQuantityState());
        shoppingStoreRepository.save(productInStore);
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        ProductInStore productInStore = shoppingStoreRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(String.valueOf(productId)));
        return productMapper.toProductDto(productInStore);
    }

}