package com.restaurant.plazoleta.infraestructure.security;

import org.springframework.security.core.userdetails.User;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends User {

    private final Long role;
    private final String documentNumber;
    private final String name;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             Long role, String documentNumber, String name) {
        super(username, password, authorities);
        this.role = role;
        this.documentNumber = documentNumber;
        this.name = name;
    }

    // Métodos para acceder a los nuevos campos
    public Long getRole() {
        return role;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getName() {
        return name;
    }
}

