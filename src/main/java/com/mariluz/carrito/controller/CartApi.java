package com.mariluz.carrito.controller;

import com.mariluz.carrito.dto.ActProductoCantRequest;
import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "Carrito",
    description = "Gestión del carrito de compras del usuario autenticado"
)
public interface CartApi {

    // 1. agregar producto al carrito
    @Operation(
        summary = "Agregar producto",
        description = "Agrega un producto al carrito del usuario autenticado. Si el producto ya existe, incrementa la cantidad."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto agregado correctamente."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (campos obligatorios faltantes o cantidad menor a 1).",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 400,
                        "error": "Datos inválidos",
                        "message": "Error de validación en los datos enviados",
                        "validaciones": { "quantity": "La cantidad mínima debe ser 1" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido/expirado o usuario no autenticado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "error": "No autenticado",
                        "message": "No hay un usuario autenticado o token inválido"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Recurso no encontrado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 404,
                        "error": "No encontrado",
                        "message": "El recurso solicitado no existe"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "error": "Error interno del servidor",
                        "message": "Error inesperado"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<CartResponse> agregarProducto(
        @Valid CartItemRequest request
    );

    // 2. eliminar producto del carrito
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto específico del carrito del usuario autenticado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto eliminado correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido/expirado o usuario no autenticado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "error": "No autenticado",
                        "message": "No hay un usuario autenticado o token inválido"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El producto no se encuentra en el carrito.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 404,
                        "error": "No encontrado",
                        "message": "El producto con ID 99 no se encuentra en tu carrito"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "error": "Error interno del servidor",
                        "message": "Error inesperado"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<CartResponse> eliminarProducto(Integer productId);

    // 3. actualizar cantidad de un producto
    @Operation(
        summary = "Actualizar cantidad",
        description = "Actualiza la cantidad de un producto ya presente en el carrito del usuario autenticado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cantidad actualizada correctamente."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (campos obligatorios faltantes o cantidad menor a 1).",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 400,
                        "error": "Datos inválidos",
                        "message": "Error de validación en los datos enviados",
                        "validaciones": { "quantity": "La cantidad mínima a actualizar debe ser 1" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido/expirado o usuario no autenticado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "error": "No autenticado",
                        "message": "No hay un usuario autenticado o token inválido"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El producto no se encuentra en el carrito.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 404,
                        "error": "No encontrado",
                        "message": "No se puede actualizar: el producto no se encuentra en el carrito"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "error": "Error interno del servidor",
                        "message": "Error inesperado"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<CartResponse> actualizarCantidad(
        @Valid ActProductoCantRequest request
    );

    // 4. ver carrito
    @Operation(
        summary = "Ver carrito",
        description = "Devuelve el carrito del usuario autenticado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrito obtenido correctamente."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT inválido/expirado o usuario no autenticado.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 401,
                        "error": "No autenticado",
                        "message": "No hay un usuario autenticado o token inválido"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2026-06-12T05:11:58",
                        "status": 500,
                        "error": "Error interno del servidor",
                        "message": "Error inesperado"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<CartResponse> verCarrito();
}
