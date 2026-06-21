package com.mariluz.carrito.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mariluz.carrito.dto.ActProductoCantRequest;
import com.mariluz.carrito.dto.CartItemRequest;
import com.mariluz.carrito.dto.CartResponse;
import com.mariluz.carrito.exception.ResourceNotFoundException;
import com.mariluz.carrito.exception.UnauthorizedOperationException;
import com.mariluz.carrito.security.JwtUtil;
import com.mariluz.carrito.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(cartController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva filtro JWT y seguridad para ejecutar el test
public class cartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper; // para mapear objetos/clases a json

    @MockitoBean
    private CartService service;

    @MockitoBean
    private JwtUtil jwtUtil; // importante para que funcione el contexto de seguridad

    // metodo de apoyo para construir un CartResponse
    private CartResponse cartResponse() {
        return CartResponse.builder()
            .products(java.util.List.of(
                CartResponse.ProductItemDto.builder()
                    .id(1)
                    .quantity(2)
                    .build()
            ))
            .build();
    }

    // -------------- 1. AGREGAR PRODUCTO --------------
    // 200
    @Test
    public void testAgregarProducto() throws Exception {
        CartItemRequest request = new CartItemRequest(1, 2);

        when(service.agregarProducto(request)).thenReturn(cartResponse());

        mockMvc
            .perform(
                post("/cart/agregar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk());
    }

    // 400
    @Test
    public void testAgregarProductoBadRequest() throws Exception {
        CartItemRequest request = new CartItemRequest(null, null);

        mockMvc
            .perform(
                post("/cart/agregar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    // 401
    @Test
    public void testAgregarProductoUnauthorized() throws Exception {
        CartItemRequest request = new CartItemRequest(1, 2);

        when(service.agregarProducto(request)).thenThrow(
            new UnauthorizedOperationException(
                "No hay un usuario autenticado o token inválido"
            )
        );

        mockMvc
            .perform(
                post("/cart/agregar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isUnauthorized());
    }

    // -------------- 2. ELIMINAR PRODUCTO --------------
    // 200
    @Test
    public void testEliminarProducto() throws Exception {
        when(service.eliminarProducto(1)).thenReturn(cartResponse());

        mockMvc
            .perform(delete("/cart/eliminar/1"))
            .andExpect(status().isOk());
    }

    // 404
    @Test
    public void testEliminarProductoNotFound() throws Exception {
        when(service.eliminarProducto(99)).thenThrow(
            new ResourceNotFoundException(
                "El producto con ID 99 no se encuentra en tu carrito"
            )
        );

        mockMvc
            .perform(delete("/cart/eliminar/99"))
            .andExpect(status().isNotFound());
    }

    // -------------- 3. ACTUALIZAR CANTIDAD --------------
    // 200
    @Test
    public void testActualizarCantidad() throws Exception {
        ActProductoCantRequest request = new ActProductoCantRequest(1, 5);

        when(service.actualizarCantidad(request)).thenReturn(cartResponse());

        mockMvc
            .perform(
                put("/cart/actualizar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk());
    }

    // 400
    @Test
    public void testActualizarCantidadBadRequest() throws Exception {
        ActProductoCantRequest request = new ActProductoCantRequest(null, null);

        mockMvc
            .perform(
                put("/cart/actualizar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());
    }

    // -------------- 4. VER CARRITO --------------
    // 200
    @Test
    public void testVerCarrito() throws Exception {
        when(service.obtenerCarritoUsuario()).thenReturn(cartResponse());

        mockMvc
            .perform(get("/cart/ver"))
            .andExpect(status().isOk());
    }
}
