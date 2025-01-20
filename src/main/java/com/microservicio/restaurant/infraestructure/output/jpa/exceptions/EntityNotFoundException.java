package com.microservicio.restaurant.infraestructure.output.jpa.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
