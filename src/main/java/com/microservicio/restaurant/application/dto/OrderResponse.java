package com.microservicio.restaurant.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long idClient;
    private Date date;
    private String status;
    private Long idEmployee;
    private Long idRestaurant;
    private String securityPin;
    private List<OrderDishResponse> dishes;
}