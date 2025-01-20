package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.DishRequest;
import com.microservicio.restaurant.domain.model.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DishRequestMapper {
    public Dish toDish(DishRequest dishRequest) {
        log.info("Converting DishRequest: {}", dishRequest);
        if (dishRequest == null) {
            return null;
        }

        Dish dish = new Dish();
        dish.setName(dishRequest.getName());
        dish.setDescription(dishRequest.getDescription());
        dish.setPrice(dishRequest.getPrice());
        dish.setUrlImage(dishRequest.getUrlImage());

        // Establece valores por defecto o lanza una excepción si son nulos
        if (dishRequest.getIdCategory() == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        dish.setIdCategory(dishRequest.getIdCategory());

        if (dishRequest.getIdRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }
        dish.setIdRestaurant(dishRequest.getIdRestaurant());

        return dish;
    }
}
