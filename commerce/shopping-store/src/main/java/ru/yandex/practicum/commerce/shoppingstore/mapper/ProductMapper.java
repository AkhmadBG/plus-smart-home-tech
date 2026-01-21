package ru.yandex.practicum.commerce.shoppingstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.entity.ProductInStore;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toProductDto(ProductInStore productInStore);

    ProductInStore toProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto, @MappingTarget ProductInStore productInStore);

}