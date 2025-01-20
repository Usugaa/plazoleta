package com.restaurant.plazoleta.infraestructure.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super("Usuario no encontrado");
    }
}
