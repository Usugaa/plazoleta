package com.microservicio.restaurant.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RestaurantResponse {
    private String message;
    private String nameRestaurant;
    private String address;
    private String phone;
    private String urlLogo;
    private String nit;
    private Long idOwner;
    private LocalDateTime timestamp;
}