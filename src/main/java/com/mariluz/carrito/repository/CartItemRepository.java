package com.mariluz.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mariluz.carrito.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
        @Query("SELECT c FROM CartItem c WHERE c.cart_id = :cartId")
    List<CartItem> buscarPorCartId(@Param("cartId") Integer cartId);
    
   
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart_id = :cartId AND c.product_id = :productId")
    void borrarPorCartIdYProductoId(@Param("cartId") Integer cartId, @Param("productId") Integer productId);
}