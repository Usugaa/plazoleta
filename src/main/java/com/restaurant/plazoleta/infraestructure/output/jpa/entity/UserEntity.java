package com.restaurant.plazoleta.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    @NotNull
    @Size(min = 5, max = 15)
    private String documentNumber;
    private String phone;
    private LocalDate birthdate;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id")
    private RoleEntity idRole;
}