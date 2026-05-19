package com.mariluz.carrito.service;

import com.mariluz.carrito.dto.ActProductoCantRequest;
import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.exception.UnauthorizedOperationException;
import com.mariluz.carrito.model.Cart;
import com.mariluz.carrito.model.CartItem;
import com.mariluz.carrito.model.User;
import com.mariluz.carrito.repository.CartItemRepository;
import com.mariluz.carrito.repository.CartRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    /*
        TODO: 1. argregar validaciones a los metodos
              2. agregar excepciones
              3. agregar metodo para ver carrito
     */

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private User getCurrentUser() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new UnauthorizedOperationException(
                "No hay un usuario autenticado"
            );
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

    // 1. AGREGAR UN PRODUCTO, suma si ya existe
    @Transactional
    public CartResponse agregarProducto(CartItemRequest request) {
        Cart cart = obtenerOCrearCarrito();

        // Verificamos si el producto ya existe utilizando el método seguro corregido
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(
            cart.getId()
        );
        Optional<CartItem> itemExistente = itemsDelCarrito
            .stream()
            .filter(i -> i.getProduct_id().equals(request.getProductId()))
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
                .product_id(request.getProductId())
                .quantity(request.getQuantity())
                .build();
            cartItemRepository.save(nuevoItem);
        }

        return obtenerCartResponse(cart);
    }

    // 2. LÓGICA: ELIMINAR UN PRODUCTO POR COMPLETO
    @Transactional
    public CartResponse eliminarProducto(Integer productId) {
        Cart cart = obtenerOCrearCarrito();

        // Borra la fila correspondiente usando el método manual seguro libre de errores de mapeo
        cartItemRepository.borrarPorCartIdYProductoId(cart.getId(), productId);

        return obtenerCartResponse(cart);
    }

    // 3. ACTUALIZAR LA CANTIDAD (REMPLAZAR VALOR)
    @Transactional
    public CartResponse actualizarCantidad(ActProductoCantRequest request) {
        Cart cart = obtenerOCrearCarrito();

        // Buscamos los ítems usando el método corregido
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(
            cart.getId()
        );

        // Buscamos el ítem específico que se quiere modificar
        CartItem item = itemsDelCarrito
            .stream()
            .filter(i -> i.getProduct_id().equals(request.getProductId()))
            .findFirst()
            .orElseThrow(() ->
                new RuntimeException(
                    "El producto no se encuentra en el carrito"
                )
            );

        // REEMPLAZAMOS la cantidad vieja por la nueva
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return obtenerCartResponse(cart);
    }

    // Método auxiliar para construir la respuesta con los datos actualizados del carrito
    private CartResponse obtenerCartResponse(Cart cart) {
        // Corregido también aquí para evitar errores al mapear la respuesta
        List<CartItem> itemsDelCarrito = cartItemRepository.buscarPorCartId(
            cart.getId()
        );
        return CartResponse.builder()
            .id(cart.getId())
            .user_id(cart.getUserId())
            .items(itemsDelCarrito)
            .build();
    }
}
