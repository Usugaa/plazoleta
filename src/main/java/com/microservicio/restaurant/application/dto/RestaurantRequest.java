package com.microservicio.restaurant.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class RestaurantRequest {
    @NotBlank(message = "El nombre del restaurante es obligatorio")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-Z0-9\\s]+$",
            message = "El nombre del restaurante puede contener números pero no puede ser solo números")
    private String nameRestaurant;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{1,13}$",
            message = "El teléfono debe contener máximo 13 dígitos y puede incluir el símbolo + al inicio")
    private String phone;

    @NotBlank(message = "La URL del logo es obligatoria")
    @URL(message = "Debe ser una URL válida")
    private String urlLogo;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]+$",
            message = "El NIT debe contener solo números")
    private String nit;

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long idOwner;
}