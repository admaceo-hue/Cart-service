package com.mariluz.carrito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ActProductoCantRequest {

    private Integer productId;
    private Integer quantity;
}
