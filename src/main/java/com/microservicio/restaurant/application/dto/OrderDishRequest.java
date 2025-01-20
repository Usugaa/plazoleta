package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDishRequest {
    private Long idDish;
    private Long amount;
}