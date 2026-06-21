package com.mariluz.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActProductoCantRequest {

    @NotNull(message = "El productId es obligatorio")
    private Integer productId;

    @NotNull(message = "La nueva cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima a actualizar debe ser 1")
    private Integer quantity;
}