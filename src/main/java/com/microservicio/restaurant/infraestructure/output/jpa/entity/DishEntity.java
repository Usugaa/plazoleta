package com.microservicio.restaurant.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "platos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String urlImage;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "id_restaurant", referencedColumnName = "id")
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "dish")
    private List<OrderDishesEntity> orderDishesEntities;

}