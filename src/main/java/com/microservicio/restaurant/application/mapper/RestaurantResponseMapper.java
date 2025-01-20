package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.RestaurantResponse;
import com.microservicio.restaurant.domain.model.Restaurant;

import java.time.LocalDateTime;

public class RestaurantResponseMapper {

    public RestaurantResponse toResponse(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return RestaurantResponse.builder()
                .message("Restaurante guardado exitosamente")
                .nameRestaurant(restaurant.getNameRestaurant())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .urlLogo(restaurant.getUrlLogo())
                .nit(restaurant.getNit())
                .idOwner(restaurant.getIdOwner())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
