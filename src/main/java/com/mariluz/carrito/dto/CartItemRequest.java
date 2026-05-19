package com.mariluz.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotNull(message = "El product_id es obligatorio")
    private Integer product_id;

    @NotNull(message = "La cantidad (quantity) es obligatoria")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    private Integer quantity;
}