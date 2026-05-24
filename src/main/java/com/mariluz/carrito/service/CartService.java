package com.mariluz.carrito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariluz.carrito.dto.ActProductoCantRequest;
import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.exception.InvalidQuantityException;
import com.mariluz.carrito.exception.ResourceNotFoundException;
import com.mariluz.carrito.exception.UnauthorizedOperationException;
import com.mariluz.carrito.model.Cart;
import com.mariluz.carrito.model.CartItem;
import com.mariluz.carrito.model.User;
import com.mariluz.carrito.repository.CartItemRepository;
import com.mariluz.carrito.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // Obtiene el usuario autenticado del contexto de seguridad
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedOperationException("No hay un usuario autenticado o token inválido");
        }
        return user;
    }

    // Método auxiliar para obtener o crear el carrito de la cabecera
    private Cart obtenerOCrearCarrito() {
        User user = getCurrentUser();
        return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart nuevoCart = Cart.builder().userId(user.getId()).build();
            return cartRepository.save(nuevoCart);
        });
    }

    //  TODO 3: METODO PARA VER EL CARRITO ACTUAL DO USUARIO
    @Transactional(readOnly = true)
    public CartResponse obtenerCarritoUsuario() {
        Cart cart = obtenerOCrearCarrito();
        return obtenerCartResponse(cart);
    }

    // 1. AGREGAR UN PRODUCTO (Suma si ya existe)
    @Transactional
    public CartResponse agregarProducto(CartItemRequest request) {
        //  TODO 1: Validaciones de negocio fundamentales
        if (request.getProductId() == null) {
            throw new InvalidQuantityException("El ID del producto es obligatorio");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidQuantityException("La cantidad a agregar debe ser mayor a cero");
        }

        Cart cart = obtenerOCrearCarrito();

        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        Optional<CartItem> itemExistente = itemsDelCarrito.stream()
                .filter(i -> i.getProduct_id().equals(request.getProductId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            CartItem item = itemExistente.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem nuevoItem = CartItem.builder()
                    .cart_id(cart.getId())
                    .product_id(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(nuevoItem);
        }

        return obtenerCartResponse(cart);
    }

    // 2. ELIMINAR UN PRODUCTO POR COMPLETO
    @Transactional
    public CartResponse eliminarProducto(Integer productId) {
        Cart cart = obtenerOCrearCarrito();

        //  TODO 1 y 2: Validar si el producto de verdad existe en el carrito antes de borrar
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        boolean existe = itemsDelCarrito.stream().anyMatch(i -> i.getProduct_id().equals(productId));
        
        if (!existe) {
            throw new ResourceNotFoundException("El producto con ID " + productId + " no se encuentra en tu carrito");
        }

        cartItemRepository.borrarPorCartIdYProductoId(cart.getId(), productId);
        return obtenerCartResponse(cart);
    }

    // 3. ACTUALIZAR LA CANTIDAD (REEMPLAZAR VALOR)
    @Transactional
    public CartResponse actualizarCantidad(ActProductoCantRequest request) {
        //  TODO 1: Validar cantidad en la actualización
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidQuantityException("La cantidad asignada debe ser mayor a cero. Si quieres quitarlo, usa eliminar.");
        }

        Cart cart = obtenerOCrearCarrito();

        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());

        //  TODO 2: Excepción personalizada en lugar de RuntimeException
        CartItem item = itemsDelCarrito.stream()
                .filter(i -> i.getProduct_id().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede actualizar: el producto no se encuentra en el carrito"
                ));

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return obtenerCartResponse(cart);
    }

    // Método auxiliar para construir la respuesta
    private CartResponse obtenerCartResponse(Cart cart) {
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        return CartResponse.builder()
                .id(cart.getId())
                .user_id(cart.getUserId())
                .items(itemsDelCarrito)
                .build();
    }
}