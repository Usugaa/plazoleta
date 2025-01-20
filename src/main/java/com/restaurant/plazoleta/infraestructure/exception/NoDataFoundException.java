package com.restaurant.plazoleta.infraestructure.exception;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException(String message) {
        super("No se encontraron los datos");
    }
}
