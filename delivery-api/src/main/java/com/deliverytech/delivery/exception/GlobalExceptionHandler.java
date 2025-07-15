package com.deliverytech.delivery.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.deliverytech.delivery.dto.response.ErrorResponseDTO;

import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDTO> handleGenericException(
                        Exception ex, WebRequest request) {

                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Erro interno do servidor",
                                "Ocorreu um erro inesperado",
                                ((ServletWebRequest) request).getRequest().getRequestURI());

                                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(
                        EntityNotFoundException ex, WebRequest request) {

                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                HttpStatus.NOT_FOUND.value(),
                                "Não encontrado",
                                ex.getMessage(),
                                ((ServletWebRequest) request).getRequest().getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ErrorResponseDTO> handleConflictException(
                        ConflictException ex, WebRequest request) {

                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                HttpStatus.CONFLICT.value(),
                                "Conflito",
                                ex.getMessage(),
                                ((ServletWebRequest) request).getRequest().getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ValidationErrorResponse> handleBusinessException(BusinessException ex) {
                ValidationErrorResponse error = new ValidationErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de regra de negócio",
                                ex.getMessage(),
                                LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {

                Map<String, String> details = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de validação",
                                "Campos inválidos na requisição",
                                ((ServletWebRequest) request).getRequest().getRequestURI(),
                                details);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex) {
                ValidationErrorResponse error = new ValidationErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de dados inválidos",
                                ex.getMessage(),
                                LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(TransactionException.class)
        public ResponseEntity<ValidationErrorResponse> handleTransactionException(TransactionException ex) {
                ValidationErrorResponse error = new ValidationErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro transacional",
                                ex.getMessage(),
                                LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
}
