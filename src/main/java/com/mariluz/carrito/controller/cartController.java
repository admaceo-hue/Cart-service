package com.mariluz.carrito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariluz.carrito.dto.ActProductoCantRequest;
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
    @PostMapping("/agregar")
    public CartResponse agregarProducto(
        @Valid @RequestBody CartItemRequest request
    ) {
        return cartService.agregarProducto(request);
    }

    // End point para eliminar un producto del carrito
    @DeleteMapping("/eliminar/{productId}")
    public CartResponse eliminarProducto(@PathVariable Integer productId) {
        return cartService.eliminarProducto(productId);
    }

    // End point para actualizar la cantidad de un producto en el carrito
    @PutMapping("/actualizar")
    public CartResponse actualizarCantidad(
        @Valid @RequestBody ActProductoCantRequest request
    ) {
        return cartService.actualizarCantidad(request);
    }
    // En tu Controller mapea el método del TODO 3:
@GetMapping
public ResponseEntity<CartResponse> verCarrito() {
    return ResponseEntity.ok(cartService.obtenerCarritoUsuario());
}
}
