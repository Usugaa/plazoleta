package com.microservicio.restaurant.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "restaurantes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameRestaurant;
    private String address;
    private String phone;
    private String urlLogo;
    private String nit;
    private Long idOwner;

    @OneToMany(mappedBy = "restaurant")
    private List<DishEntity> dishEntities;

    @OneToMany(mappedBy = "restaurant")
    private List<OrderEntity> orderEntities;

}