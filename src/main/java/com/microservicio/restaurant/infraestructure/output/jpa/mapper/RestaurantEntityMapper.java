package com.microservicio.restaurant.infraestructure.output.jpa.mapper;

import com.microservicio.restaurant.domain.model.Restaurant;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEntityMapper {

    public RestaurantEntity toEntity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(restaurant.getId());
        entity.setNameRestaurant(restaurant.getNameRestaurant());
        entity.setAddress(restaurant.getAddress());
        entity.setPhone(restaurant.getPhone());
        entity.setUrlLogo(restaurant.getUrlLogo());
        entity.setNit(restaurant.getNit());
        entity.setIdOwner(restaurant.getIdOwner());
        return entity;
    }

    public Restaurant toDomain(RestaurantEntity entity) {
        if (entity == null) {
            return null;
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setId(entity.getId());
        restaurant.setNameRestaurant(entity.getNameRestaurant());
        restaurant.setAddress(entity.getAddress());
        restaurant.setPhone(entity.getPhone());
        restaurant.setUrlLogo(entity.getUrlLogo());
        restaurant.setNit(entity.getNit());
        restaurant.setIdOwner(entity.getIdOwner());
        return restaurant;
    }
}