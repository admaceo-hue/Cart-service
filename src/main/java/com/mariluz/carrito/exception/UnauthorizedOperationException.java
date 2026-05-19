package com.mariluz.carrito.exception;

// excepcion para manejo de permisos
public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
