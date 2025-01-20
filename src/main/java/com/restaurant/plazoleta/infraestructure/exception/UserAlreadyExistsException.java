package com.restaurant.plazoleta.infraestructure.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super("Usuario ya registrado");
    }
}
