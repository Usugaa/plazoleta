package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateDishRequest {

    private String description;
    private Long price;

}
