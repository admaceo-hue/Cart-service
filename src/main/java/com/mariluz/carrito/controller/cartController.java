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
public class cartController implements CartApi {

    @Autowired
    private CartService cartService;

    @Override
    @PostMapping("/agregar")
    public ResponseEntity<CartResponse> agregarProducto(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.agregarProducto(request));
        
    }

    @Override
    @DeleteMapping("/eliminar/{productId}")
    public ResponseEntity<CartResponse> eliminarProducto(@PathVariable Integer productId) {
        return ResponseEntity.ok(cartService.eliminarProducto(productId));
        
    }

    @Override
    @PutMapping("/actualizar")
    public ResponseEntity<CartResponse> actualizarCantidad(@Valid @RequestBody ActProductoCantRequest request) {
        return ResponseEntity.ok(cartService.actualizarCantidad(request));
    }

    @Override
    @GetMapping("/ver")
    public ResponseEntity<CartResponse> verCarrito() {
        return ResponseEntity.ok(cartService.obtenerCarritoUsuario());
    }
}