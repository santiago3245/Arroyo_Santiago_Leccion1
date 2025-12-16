package com.arroyo_santiago_leccion1.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validacion",
                errors
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFilterException.class)
    public ErrorResponse handleInvalidFilterException(InvalidFilterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("filter", ex.getMessage());
        
        return new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Filtro invalido",
                errors
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            errors.put("error", "El numero de ticket ya existe. Por favor, use uno diferente.");
        } else {
            errors.put("error", "Error de integridad de datos: " + ex.getMostSpecificCause().getMessage());
        }
        
        return new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.CONFLICT.value(),
                "Conflicto de datos",
                errors
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        
        if (ex.getMessage() != null && ex.getMessage().contains("no encontrado")) {
            errors.put("error", ex.getMessage());
            return new ErrorResponse(
                    Instant.now().toString(),
                    HttpStatus.NOT_FOUND.value(),
                    "Recurso no encontrado",
                    errors
            );
        }
        
        errors.put("error", ex.getMessage());
        return new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno",
                errors
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                errors
        );
    }
}
