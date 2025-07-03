package com.deliverytech.delivery.exception;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErroResponse(String erro, String detalhe, LocalDateTime data) {
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErroResponse("Erro interno do servidor", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErroResponse> handleInvalidParameterException(InvalidParameterException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String detalhe = ex.getField() + " = " + ex.getValue();
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), detalhe, LocalDateTime.now()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String detalhe = java.util.Collections.singletonMap("messages", messages).toString();

        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), detalhe, LocalDateTime.now()));
    }
}
