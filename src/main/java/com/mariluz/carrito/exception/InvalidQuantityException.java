package com.mariluz.carrito.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String mensaje) {
        super(mensaje);
    }
}