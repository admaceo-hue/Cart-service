package com.mariluz.carrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String mensaje) {
        super(mensaje);
    }
}