package com.mariluz.carrito.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedOperationException("No hay un usuario autenticado o token inválido");
        }
        return user;
    }

    // Obtiene el carrito del usuario autenticado o crea uno nuevo si no existe.
    private Cart obtenerOCrearCarrito() {
        User user = getCurrentUser();
        return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart nuevoCart = Cart.builder().userId(user.getId()).build();
            return cartRepository.save(nuevoCart);
        });
    }

    
    @Transactional
    public CartResponse obtenerCarritoUsuario() {
        Cart cart = obtenerOCrearCarrito();
        return obtenerCartResponse(cart);
    }

    // Agrega un producto al carrito del usuario. Si el producto ya existe, incrementa la cantidad.
    @Transactional
    public CartResponse agregarProducto(CartItemRequest request) {
        
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidQuantityException("La cantidad para agregar al carrito debe ser mayor o igual a 1");
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

    // Elimina un producto específico del carrito del usuario.
    @Transactional
    public CartResponse eliminarProducto(Integer productId) {
        Cart cart = obtenerOCrearCarrito();

        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        boolean existe = itemsDelCarrito.stream().anyMatch(i -> i.getProduct_id().equals(productId));
        
        if (!existe) {
            throw new ResourceNotFoundException("El producto con ID " + productId + " no se encuentra en tu carrito");
        }

        cartItemRepository.borrarPorCartIdYProductoId(cart.getId(), productId);
        return obtenerCartResponse(cart);
    }

    // Actualiza la cantidad de un producto ya presente en el carrito. 
    @Transactional
    public CartResponse actualizarCantidad(ActProductoCantRequest request) {
        
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new InvalidQuantityException("La cantidad ingresada debe ser mayor o igual a 1");
        }

        Cart cart = obtenerOCrearCarrito();

        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());

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

    // Método privado para construir un CartResponse 
    private CartResponse obtenerCartResponse(Cart cart) {
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        
        List<CartResponse.ProductItemDto> listaProductosDto = itemsDelCarrito.stream()
                .map(item -> CartResponse.ProductItemDto.builder()
                        .id(item.getProduct_id())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .products(listaProductosDto)
                .build();
    }
}