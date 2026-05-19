package com.mariluz.carrito.dto;

import java.util.List;

import com.mariluz.carrito.model.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Integer id;        // ID del carrito
    private Integer user_id;   // ID del usuario (coincide con tu INTE... de la foto)
    private List<CartItem> items; // La lista con los productos de la tabla cart_item
}