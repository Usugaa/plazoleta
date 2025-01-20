package com.microservicio.restaurant.application.mapper;

import com.microservicio.restaurant.application.dto.DishResponse;
import com.microservicio.restaurant.domain.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishResponseMapper {

    public DishResponse toResponse(Dish dish) {
        if (dish == null) {
            return null;
        }

        DishResponse response = new DishResponse();
        response.setId(dish.getId());
        response.setName(dish.getName());
        response.setDescription(dish.getDescription());
        response.setPrice(dish.getPrice());
        response.setUrlImage(dish.getUrlImage());
        response.setActive(dish.isActive());
        response.setIdCategory(dish.getIdCategory());
        response.setIdRestaurant(dish.getIdRestaurant());

        return response;
    }
}
