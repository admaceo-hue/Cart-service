package com.mariluz.carrito.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.model.Cart;
import com.mariluz.carrito.model.CartItem;
import com.mariluz.carrito.repository.CartItemRepository;
import com.mariluz.carrito.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    // Método auxiliar para obtener o crear el carrito de la cabecera
    private Cart obtenerOCrearCarrito(Integer userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart nuevoCart = Cart.builder()
                            .userId(userId)
                            .build();
                    return cartRepository.save(nuevoCart);
                });
    }
    

    // 1. AGREGAR UN PRODUCTO, suma si ya existe
    @Transactional
    public CartResponse agregarProducto(Integer userId, CartItemRequest request) {  
        Cart cart = obtenerOCrearCarrito(userId);
        
        // Verificamos si el producto ya existe utilizando el método seguro corregido
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        Optional<CartItem> itemExistente = itemsDelCarrito.stream()
                .filter(i -> i.getProduct_id().equals(request.getProduct_id()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // Si el producto ya existe, sumamos la cantidad
            CartItem item = itemExistente.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Si el producto no existe, lo agregamos como nuevo ítem
            CartItem nuevoItem = CartItem.builder()
                    .cart_id(cart.getId())
                    .product_id(request.getProduct_id())
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(nuevoItem);
        }

        return obtenerCartResponse(cart);
    }
    

    // 2. LÓGICA: ELIMINAR UN PRODUCTO POR COMPLETO
    @Transactional
    public CartResponse eliminarProducto(Integer userId, Integer productId) {
        Cart cart = obtenerOCrearCarrito(userId);
        
        // Borra la fila correspondiente usando el método manual seguro libre de errores de mapeo
        cartItemRepository.borrarPorCartIdYProductoId(cart.getId(), productId);
        
        return obtenerCartResponse(cart);
    }

    // 3. ACTUALIZAR LA CANTIDAD (REMPLAZAR VALOR)
    @Transactional
    public CartResponse actualizarCantidad(Integer userId, Integer productId, Integer nuevaCantidad) {
        Cart cart = obtenerOCrearCarrito(userId);
        
        // Buscamos los ítems usando el método corregido
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());

        // Buscamos el ítem específico que se quiere modificar
        CartItem item = itemsDelCarrito.stream()
                .filter(i -> i.getProduct_id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en el carrito"));

        // REEMPLAZAMOS la cantidad vieja por la nueva
        item.setQuantity(nuevaCantidad);
        cartItemRepository.save(item);

        return obtenerCartResponse(cart);
    }

    // Método auxiliar para construir la respuesta con los datos actualizados del carrito
    private CartResponse obtenerCartResponse(Cart cart) {
        // Corregido también aquí para evitar errores al mapear la respuesta
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(cart.getId());
        return CartResponse.builder()
                .id(cart.getId())
                .user_id(cart.getUserId())      
                .items(itemsDelCarrito)
                .build();
    }
}