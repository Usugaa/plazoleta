package com.microservicio.restaurant.infraestructure.output.jpa.exceptions;

public class InvalidRoleException extends RuntimeException{
    public InvalidRoleException(String message) {
        super(message);
    }
}
