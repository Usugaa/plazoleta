package com.microservicio.restaurant.infraestructure.configuration;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                return new IllegalArgumentException("Usuario no encontrado");
            case 400:
                return new IllegalArgumentException("Solicitud inválida");
            default:
                return new RuntimeException("Error en la comunicación con el servicio");
        }
    }
    
}
