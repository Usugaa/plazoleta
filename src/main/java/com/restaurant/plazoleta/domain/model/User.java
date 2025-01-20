package com.restaurant.plazoleta.domain.model;

import java.time.LocalDate;

public class User {
    private Long id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String password;
    private Long idRole;

    public User(Long id, String name, String lastName, String documentNumber, String phone, LocalDate birthdate, String email, String password, Long idRole) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.idRole = idRole;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }
}
