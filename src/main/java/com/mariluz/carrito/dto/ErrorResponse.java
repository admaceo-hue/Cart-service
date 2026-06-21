package com.mariluz.carrito.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validaciones;
}