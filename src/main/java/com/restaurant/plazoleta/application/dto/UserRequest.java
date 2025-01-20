package com.restaurant.plazoleta.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotNull(message = "El apellido no puede ser nulo")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Pattern(regexp = "^[0-9]+$", message = "El documento de identidad debe contener solo números")
    @NotNull(message = "El documento de identidad no puede ser nulo")
    private String documentNumber;

    @Pattern(regexp = "^\\+?[0-9]{1,13}$", message = "El teléfono debe contener solo números y opcionalmente el símbolo '+'")
    @NotNull(message = "El teléfono no puede ser nulo")
    @Size(max = 13, message = "El teléfono no puede exceder 13 caracteres")
    private String phone;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private String birthdate;

    @Email(message = "El email debe tener un formato válido")
    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}