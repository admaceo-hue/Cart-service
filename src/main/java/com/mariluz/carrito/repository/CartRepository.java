package com.mariluz.carrito.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mariluz.carrito.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(String userId);
}
