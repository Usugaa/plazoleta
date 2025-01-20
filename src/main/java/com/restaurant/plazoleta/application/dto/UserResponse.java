package com.restaurant.plazoleta.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {

    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String phone;
    private String email;
    private Long idRole;
}