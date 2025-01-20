package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDishResponse {
    private Long id;
    private Long idOrder;
    private Long idDish;
    private Long amount;
}