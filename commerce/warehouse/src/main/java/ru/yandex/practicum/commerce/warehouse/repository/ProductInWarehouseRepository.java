package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductInWarehouseRepository extends JpaRepository<ProductInWarehouse, UUID> {
    Optional<ProductInWarehouse> findByProductId(UUID uuid);
}