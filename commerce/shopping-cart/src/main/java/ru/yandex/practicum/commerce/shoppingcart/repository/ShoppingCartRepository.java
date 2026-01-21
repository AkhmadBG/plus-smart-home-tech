package ru.yandex.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    @Query("""
            SELECT DISTINCT s FROM ShoppingCart s
            LEFT JOIN FETCH s.cartItems c
            WHERE s.userName = :userName
            """)
    Optional<ShoppingCart> findShoppingCartByUserName(@Param("userName") String userName);

}