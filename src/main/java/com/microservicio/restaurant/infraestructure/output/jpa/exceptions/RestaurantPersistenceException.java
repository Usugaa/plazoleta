package com.microservicio.restaurant.infraestructure.output.jpa.exceptions;

public class RestaurantPersistenceException extends RuntimeException {
    public RestaurantPersistenceException(String message) {
        super(message);
    }
}
