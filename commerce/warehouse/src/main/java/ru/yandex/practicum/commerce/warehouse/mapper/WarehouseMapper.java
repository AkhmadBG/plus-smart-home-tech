package ru.yandex.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.commerce.interactionapi.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface WarehouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId",
            expression = "java(UUID.fromString(newProductInWarehouseRequest.getProductId()))")
    @Mapping(target = "width", source = "dimensionDto.width")
    @Mapping(target = "height", source = "dimensionDto.height")
    @Mapping(target = "depth", source = "dimensionDto.depth")
    @Mapping(target = "quantity", constant = "0")
    ProductInWarehouse toProductInWarehouse(
            NewProductInWarehouseRequest newProductInWarehouseRequest
    );
}