package com.mariluz.carrito.repository;

import com.mariluz.carrito.model.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Busca el carrito usando tu campo 'userId'
    Optional<Cart> findByUserId(String userId);
}
