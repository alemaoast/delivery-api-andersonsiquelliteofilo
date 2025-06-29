package com.deliverytech.delivery.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    private String field;
    private String value;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }    

     public ResourceNotFoundException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }

    // Getters for field and value
    public String getField() {
        return field;
    }
    public String getValue() {
        return value;
    }
}
