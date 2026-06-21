package com.mariluz.carrito.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mariluz.carrito.dto.ErrorResponse; 

@RestControllerAdvice
public class GlobalExceptionHandler {

    // toma el mensaje de error y lo devuelve con un status 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso No Encontrado")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // toma el mensaje de error y lo devuelve con un status 401 Unauthorized
    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedOperation(UnauthorizedOperationException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Operación No Autorizada")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // toma el mensaje de error y lo devuelve con un status 400 Bad Request
    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidQuantity(InvalidQuantityException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Cantidad Inválida")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // toma el mensaje de error y lo devuelve con un status 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erroresValidacion = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            erroresValidacion.put(fieldName, errorMessage);
        });

        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de Validación")
                .message("Los datos enviados no cumplen con los requisitos mínimos.")
                .validaciones(erroresValidacion) 
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // toma el mensaje de error y lo devuelve con un status 400 Bad Request
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestBody(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Cuerpo de petición faltante")
                .message("El cuerpo de la petición (JSON Body) es obligatorio.")
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // toma el mensaje de error y lo devuelve con un status 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error Interno del Servidor")
                .message("Ocurrió un error inesperado en el sistema: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}