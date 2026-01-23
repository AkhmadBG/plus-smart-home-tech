package ru.yandex.practicum.commerce.shoppingstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;

import java.util.UUID;

@Service
public interface ShoppingStoreService {

    Page<ProductDto> getListProductDtoByCategory(ProductCategory productCategory, Pageable pageable);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProductFromStore(UUID productId);

    Boolean setProductQuantityState(SetProductQuantityStateRequest quantityStateRequest);

    ProductDto getProductById(UUID productId);

}