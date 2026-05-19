package com.mariluz.carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class cartController {
    @Autowired
    private CartService cartService;

    // End point para agregar un producto al carrito
    @PostMapping("/agregar/{userId}")
    public CartResponse agregarProducto(
            @PathVariable Integer userId,
            @Valid @RequestBody CartItemRequest request
    ) {
        return cartService.agregarProducto(userId, request);
    }

    // End point para eliminar un producto del carrito
    @DeleteMapping("/eliminar/{userId}/{productId}")
    public CartResponse eliminarProducto(
            @PathVariable Integer userId,
            @PathVariable Integer productId
    ) {
        return cartService.eliminarProducto(userId, productId);
    }

    // End point para actualizar la cantidad de un producto en el carrito
    @PutMapping("/actualizar/{userId}/{productId}/{cantidad}")
    public CartResponse actualizarCantidad(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @PathVariable Integer cantidad
    ) {
        return cartService.actualizarCantidad(userId, productId, cantidad);
    }
}
