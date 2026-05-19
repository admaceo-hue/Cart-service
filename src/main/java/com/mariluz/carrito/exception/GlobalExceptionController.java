package com.mariluz.carrito.exception;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    // 1. Captura el error cuando el producto no existe en el carrito (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso no encontrado");
        error.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 2. Captura las validaciones de las etiquetas @NotNull y @Min del DTO (Validaciones de Postman)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put("error", "Validación fallida en los datos enviados");
        
        // Recorremos todos los campos que fallaron y sacamos el mensaje que tú escribiste en el DTO
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // 3. Captura cualquier otro error genérico e inesperado del sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("mensaje", "Ocurrió un problema inesperado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}