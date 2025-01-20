package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.RestaurantRequest;
import com.microservicio.restaurant.domain.model.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantRequestMapper {

    public Restaurant toDomain(RestaurantRequest restaurantRequest) {
        if (restaurantRequest == null) {
            return null;
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setNameRestaurant(restaurantRequest.getNameRestaurant());
        restaurant.setAddress(restaurantRequest.getAddress());
        restaurant.setPhone(restaurantRequest.getPhone());
        restaurant.setUrlLogo(restaurantRequest.getUrlLogo());
        restaurant.setNit(restaurantRequest.getNit());
        restaurant.setIdOwner(restaurantRequest.getIdOwner());
        return restaurant;
    }

    public RestaurantRequest toRequest(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        RestaurantRequest request = new RestaurantRequest();
        request.setNameRestaurant(restaurant.getNameRestaurant());
        request.setAddress(restaurant.getAddress());
        request.setPhone(restaurant.getPhone());
        request.setUrlLogo(restaurant.getUrlLogo());
        request.setNit(restaurant.getNit());
        request.setIdOwner(restaurant.getIdOwner());
        return request;
    }
}
