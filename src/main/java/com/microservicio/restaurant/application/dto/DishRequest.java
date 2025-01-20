package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequest {

    private String name;
    private Long idCategory;
    private String description;
    private Long price;
    private Long idRestaurant;
    private String urlImage;
}
