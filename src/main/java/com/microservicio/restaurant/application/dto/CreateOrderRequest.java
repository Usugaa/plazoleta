package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    private Long restaurantId;

    private List<OrderDishRequest> dishes;

}