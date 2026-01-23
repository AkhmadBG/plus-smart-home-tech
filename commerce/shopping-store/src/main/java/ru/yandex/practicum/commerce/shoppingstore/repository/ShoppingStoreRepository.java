package ru.yandex.practicum.commerce.shoppingstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.interactionapi.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.entity.ProductInStore;

import java.util.UUID;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<ProductInStore, UUID> {

    Page<ProductInStore> findByProductCategory(ProductCategory productCategory, Pageable pageable);

}